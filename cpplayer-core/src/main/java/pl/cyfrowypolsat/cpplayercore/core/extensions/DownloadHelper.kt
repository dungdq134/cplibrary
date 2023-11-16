package pl.cyfrowypolsat.cpplayercore.core.extensions

import androidx.media3.common.C
import androidx.media3.common.Format
import androidx.media3.exoplayer.offline.DownloadHelper
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import androidx.media3.exoplayer.trackselection.MappingTrackSelector
import androidx.media3.common.TrackSelectionOverride
import pl.cyfrowypolsat.cpplayercore.BuildConfig
import timber.log.Timber

/** from ExoPlayer sample:
 * Returns the first {@link androidx.media3.common.Format} with a non-null {@link Format#drmInitData} found in the
 * content's tracks, or null if none is found.
 */
fun DownloadHelper.getFirstFormatWithDrmInitData(): Format? {
    for (periodIndex in 0 until this.periodCount) {
        val mappedTrackInfo = this.getMappedTrackInfo(periodIndex)
        for (rendererIndex in 0 until mappedTrackInfo.rendererCount) {
            val trackGroups = mappedTrackInfo.getTrackGroups(rendererIndex)
            for (trackGroupIndex in 0 until trackGroups.length) {
                val trackGroup = trackGroups[trackGroupIndex]
                for (formatIndex in 0 until trackGroup.length) {
                    val format = trackGroup.getFormat(formatIndex)
                    if (format.drmInitData != null) {
                        return format
                    }
                }
            }
        }
    }
    return null
}

private data class VideoTrack(val rendererIndex: Int,
                              val groupIndex: Int,
                              val trackIndices: List<Int>,
                              val quality: Int,
                              val isDisabled: Boolean)

private data class AudioTrack(val rendererIndex: Int,
                              val groupIndex: Int,
                              val trackIndices: List<Int>,
                              val language: String?,
                              val bitrate: Int,
                              val isDisabled: Boolean)

fun DownloadHelper.setAudioTracksSelection(trackSelector: DefaultTrackSelector) {
    if (this.manifest == null || this.periodCount <= 0) {  //mp4 doesn't have manifest
        return
    }

    val mappedTrackInfo = this.getMappedTrackInfo(0)

    val audioTrackList = getAvailableAudioTracks(mappedTrackInfo)
    val bestAudioTracks = findBestAudioTracks(audioTrackList)

    for (audioTrack in bestAudioTracks) {
        if (!audioTrack.isDisabled) {
            val builder = trackSelector.parameters.buildUpon()

            val trackGroupArray = mappedTrackInfo.getTrackGroups(audioTrack.rendererIndex)
            val trackGroup = trackGroupArray[audioTrack.groupIndex]
            val trackIndices = audioTrack.trackIndices
            builder.addOverride(TrackSelectionOverride(trackGroup, trackIndices))

            addTrackSelection(0, builder.build())
        }
    }
}

fun DownloadHelper.setVideoTracksSelection(trackSelector: DefaultTrackSelector, maxQuality: Int) {
    if (this.manifest == null || this.periodCount <= 0) {  //mp4 doesn't have manifest
        return
    }

    val mappedTrackInfo = this.getMappedTrackInfo(0)

    val videoTrackList = getAvailableVideoTracks(mappedTrackInfo)
    val bestVideoTrack = findBestVideoTrack(videoTrackList, maxQuality)

    val builder = trackSelector.parameters.buildUpon()

    bestVideoTrack?.let {
        builder.clearOverridesOfType(C.TRACK_TYPE_VIDEO)
        builder.setRendererDisabled(it.rendererIndex, it.isDisabled)
        if (!it.isDisabled) {
            val trackGroupArray = mappedTrackInfo.getTrackGroups(it.rendererIndex)
            val trackGroup = trackGroupArray[it.groupIndex]
            val trackIndices = it.trackIndices
            builder.addOverride(TrackSelectionOverride(trackGroup, trackIndices))
        }
        trackSelector.setParameters(builder)
    }
    replaceTrackSelections(0, trackSelector.parameters)
}

private fun getAvailableVideoTracks(mappedTrackInfo: MappingTrackSelector.MappedTrackInfo): List<VideoTrack> {
    val rendererIndex = findRendererIndex(mappedTrackInfo, C.TRACK_TYPE_VIDEO)
            ?: return listOf()
    val trackGroups = mappedTrackInfo.getTrackGroups(rendererIndex)
    val videoSelectionItems = mutableListOf<VideoTrack>()

    for (groupIndex in 0 until trackGroups.length) {
        val group = trackGroups.get(groupIndex)
        if (group.length == 0) continue
        for (trackIndex in 0 until group.length) {
            val trackSupport = mappedTrackInfo.getTrackSupport(rendererIndex, groupIndex, trackIndex)
            if (trackSupport != C.FORMAT_HANDLED) continue
            val format = group.getFormat(trackIndex)

            val videoSelectionItem = VideoTrack(rendererIndex = rendererIndex,
                    groupIndex = groupIndex,
                    trackIndices = listOf(trackIndex),
                    quality = format.height,
                    isDisabled = false
            )
            videoSelectionItems.add(videoSelectionItem)
        }
    }
    return videoSelectionItems
}

private fun getAvailableAudioTracks(mappedTrackInfo: MappingTrackSelector.MappedTrackInfo): List<AudioTrack> {
    val rendererIndex = findRendererIndex(mappedTrackInfo, C.TRACK_TYPE_AUDIO)
            ?: return listOf()
    val trackGroups = mappedTrackInfo.getTrackGroups(rendererIndex)
    val audioSelectionItems = mutableListOf<AudioTrack>()

    for (groupIndex in 0 until trackGroups.length) {
        val group = trackGroups.get(groupIndex)
        if (group.length == 0) continue
        for (trackIndex in 0 until group.length) {
            val trackSupport = mappedTrackInfo.getTrackSupport(rendererIndex, groupIndex, trackIndex)
            if (trackSupport != C.FORMAT_HANDLED) continue
            val format = group.getFormat(trackIndex)

            val audioSelectionItem = AudioTrack(rendererIndex = rendererIndex,
                    groupIndex = groupIndex,
                    trackIndices = listOf(trackIndex),
                    language = format.language,
                    bitrate = format.bitrate,
                    isDisabled = false
            )
            audioSelectionItems.add(audioSelectionItem)
        }
    }
    return audioSelectionItems
}

private fun findBestVideoTrack(list: List<VideoTrack>, maxQuality: Int): VideoTrack? {
    if (list.isEmpty()) {
        return null
    }

    val sortedList = list.sortedBy { it.quality }
    var bestTrack = sortedList.first()
    for (track in sortedList) {
        if (track.quality <= maxQuality && track.quality > bestTrack.quality) {
            bestTrack = track
        }
    }
    return bestTrack
}

private fun findBestAudioTracks(list: List<AudioTrack>): List<AudioTrack> {
    val bestTracks = mutableListOf<AudioTrack>()

    if (list.isEmpty()) {
        return bestTracks
    }

    val groupIndexes = list.map { it.groupIndex }.distinct()
    for (index in groupIndexes) {
        val track = list.filter { it.groupIndex == index }.maxByOrNull { it.bitrate }
        track?.let {
            bestTracks.add(it)
        }
    }

    return bestTracks
}

private fun findRendererIndex(mappedTrackInfo: MappingTrackSelector.MappedTrackInfo, trackTypeToFind: Int): Int? {
    for (rendererIndex in 0 until mappedTrackInfo.rendererCount) {
        val trackType = mappedTrackInfo.getRendererType(rendererIndex)
        if (trackType == trackTypeToFind) {
            return rendererIndex
        }
    }
    return null
}

fun DownloadHelper.logTrackSelections() {
    if (!BuildConfig.DEBUG) {
        return
    }

    try {
        val mappedTrackInfo = this.getMappedTrackInfo(0)
        val videoRendererIndex = findRendererIndex(mappedTrackInfo, C.TRACK_TYPE_VIDEO)
        videoRendererIndex?.let {
            Timber.d("VideoTracksSelections")
            for (ts in this.getTrackSelections(0, it)) {
                Timber.d("format: ${ts.selectedFormat}, selectedIndex: ${ts.selectedIndex} selectedIndexInTrackGroup: ${ts.selectedIndexInTrackGroup}")
            }
        }

        val audioRendererIndex = findRendererIndex(mappedTrackInfo, C.TRACK_TYPE_AUDIO)
        audioRendererIndex?.let {
            Timber.d("AudioTracksSelections")
            for (ts in this.getTrackSelections(0, it)) {
                Timber.d("format: ${ts.selectedFormat}, selectedIndex: ${ts.selectedIndex} selectedIndexInTrackGroup: ${ts.selectedIndexInTrackGroup}")
            }
        }
    } catch (t: Throwable) {
        //do nothing
    }
}