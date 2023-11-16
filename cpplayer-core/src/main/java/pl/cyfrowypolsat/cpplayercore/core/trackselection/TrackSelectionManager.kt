package pl.cyfrowypolsat.cpplayercore.core.trackselection

import android.content.Context
import androidx.media3.common.C
import androidx.media3.common.TrackGroup
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import androidx.media3.exoplayer.trackselection.MappingTrackSelector
import androidx.media3.common.TrackSelectionOverride
import pl.cyfrowypolsat.cpplayercore.R
import pl.cyfrowypolsat.cpplayercore.core.sharedprefs.PlayerSettingsSharedPrefs


class TrackSelectionManager(private val mappedTrackInfo: MappingTrackSelector.MappedTrackInfo,
                            private val trackSelector: DefaultTrackSelector,
                            context: Context) {

    companion object {
        fun build(trackSelector: DefaultTrackSelector?, context: Context): TrackSelectionManager? {
            val mappedTrackInfo = trackSelector?.currentMappedTrackInfo ?: return null
            return TrackSelectionManager(mappedTrackInfo, trackSelector, context)
        }
    }

    private val subtitlesDisabledName = context.getString(R.string.cppl_cr_subtitles_disabled_name)
    private val sharedPrefs = PlayerSettingsSharedPrefs(context)

    fun getSubtitlesAndAudioTrackTypes(): List<Int> {
        val trackTypes = mutableListOf<Int>()
        if (isAudioSelectionAvailable()) {
            trackTypes.add(C.TRACK_TYPE_AUDIO)
        }
        if (isSubtitleSelectionAvailable()) {
            trackTypes.add(C.TRACK_TYPE_TEXT)
        }
        return trackTypes
    }

    fun trackItemSelected(track: TrackSelectionItem) {
        val builder = trackSelector.parameters.buildUpon()
        builder.clearOverridesOfType(track.trackType)
        builder.setRendererDisabled(track.rendererIndex, track.isDisabled)
        if (!track.isDisabled) {
            val trackGroupArray = mappedTrackInfo.getTrackGroups(track.rendererIndex)
            val trackGroup = trackGroupArray[track.groupIndex]
            val trackIndices = track.trackIndices
            builder.addOverride(TrackSelectionOverride(trackGroup, trackIndices))
        }
        trackSelector.setParameters(builder)
        saveTrackPreferences(track)
    }


    // Audio
    fun isAudioSelectionAvailable(): Boolean {
        return getAudioSelectionItems().size > 1
    }

    fun getAudioSelectionItems(): List<TrackSelectionItem> {
        val rendererIndex = findRendererIndex(C.TRACK_TYPE_AUDIO) ?: return listOf()
        val trackGroups = mappedTrackInfo.getTrackGroups(rendererIndex)
        val audioSelectionItems = mutableListOf<TrackSelectionItem>()

        for (groupIndex in 0 until trackGroups.length) {
            val group = trackGroups.get(groupIndex)
            if (group.length == 0) continue
            val firstTrackIndex = 0
            val trackSupport = mappedTrackInfo.getTrackSupport(rendererIndex, groupIndex, firstTrackIndex)
            if (trackSupport != C.FORMAT_HANDLED) continue
            val format = group.getFormat(firstTrackIndex)
            val audioSelectionItem = TrackSelectionItem(name = TrackNameProvider.getAudioName(format),
                    trackType = C.TRACK_TYPE_AUDIO,
                    isDisabled = false,
                    rendererIndex = rendererIndex,
                    groupIndex = groupIndex,
                    trackIndices = getAllTrackIndices(group),
                    language = format.language
            )
            audioSelectionItems.add(audioSelectionItem)
        }
        return audioSelectionItems
    }

    fun findSelectedAudioItemIndex(audioItems: List<TrackSelectionItem>): Int? {
        val rendererIndex = findRendererIndex(C.TRACK_TYPE_AUDIO) ?: return null
        val trackGroups = mappedTrackInfo.getTrackGroups(rendererIndex)

        var selectedAudio: TrackSelectionItem? = null
        audioItems.forEach {
            if (it.groupIndex >= 0) {
                val selectionOverride = trackSelector.parameters.overrides[trackGroups[it.groupIndex]]
                if (selectionOverride != null) {
                    selectedAudio = it
                }
            }
        }

        return if (selectedAudio != null) {
            val index = audioItems.indexOf(selectedAudio)
            if (index > -1) index else null
        } else {
            findTrackItemIndexForLanguage(sharedPrefs.audioLanguage, audioItems)
        }
    }


    // Subtitle
    fun isSubtitleSelectionAvailable(): Boolean {
        return getSubtitleSelectionItems().size > 1
    }

    fun getSubtitleSelectionItems(): List<TrackSelectionItem> {
        val rendererIndex = findRendererIndex(C.TRACK_TYPE_TEXT) ?: return listOf()
        val trackGroups = mappedTrackInfo.getTrackGroups(rendererIndex)
        val subtitleSelectionItems = mutableListOf<TrackSelectionItem>()

        subtitleSelectionItems.add(getSubtitlesDisabledItem(rendererIndex))

        for (groupIndex in 0 until trackGroups.length) {
            val group = trackGroups.get(groupIndex)
            if (group.length == 0) continue
            val firstTrackIndex = 0
            val trackSupport = mappedTrackInfo.getTrackSupport(rendererIndex, groupIndex, firstTrackIndex)
            if (trackSupport != C.FORMAT_HANDLED) continue
            val format = group.getFormat(firstTrackIndex)
            val name = TrackNameProvider.getSubtitleName(format)
            if (name.isBlank()) continue
            val subtitleSelectionItem = TrackSelectionItem(name = name,
                    trackType = C.TRACK_TYPE_TEXT,
                    isDisabled = false,
                    rendererIndex = rendererIndex,
                    groupIndex = groupIndex,
                    trackIndices = getAllTrackIndices(group),
                    language = format.language
            )
            subtitleSelectionItems.add(subtitleSelectionItem)
        }
        return subtitleSelectionItems
    }

    fun findSelectedSubtitleItemIndex(subtitleItems: List<TrackSelectionItem>): Int? {
        val rendererIndex = findRendererIndex(C.TRACK_TYPE_TEXT) ?: return null
        val trackGroups = mappedTrackInfo.getTrackGroups(rendererIndex)

        var selectedSubtitle: TrackSelectionItem? = null
        subtitleItems.forEach {
            if (it.groupIndex >= 0) {
                val selectionOverride = trackSelector.parameters.overrides[trackGroups[it.groupIndex]]
                if (selectionOverride != null) {
                    selectedSubtitle = it
                }
            }
        }

        return if (selectedSubtitle != null) {
            val index = subtitleItems.indexOf(selectedSubtitle)
            if (index > -1) index else null
        } else {
            findTrackItemIndexForLanguage(sharedPrefs.subtitleLanguage, subtitleItems)
        }
    }

    private fun getSubtitlesDisabledItem(rendererIndex: Int): TrackSelectionItem {
        return TrackSelectionItem(name = subtitlesDisabledName,
                trackType = C.TRACK_TYPE_TEXT,
                isDisabled = true,
                rendererIndex = rendererIndex,
                groupIndex = -1,
                trackIndices = listOf())
    }


    // Common
    private fun saveTrackPreferences(track: TrackSelectionItem) {
        when (track.trackType) {
            C.TRACK_TYPE_AUDIO -> sharedPrefs.audioLanguage = track.language
            C.TRACK_TYPE_TEXT -> sharedPrefs.subtitleLanguage = track.language
        }
    }

    private fun findRendererIndex(trackTypeToFind: Int): Int? {
        for (rendererIndex in 0 until mappedTrackInfo.rendererCount) {
            val trackType = mappedTrackInfo.getRendererType(rendererIndex)
            if (trackType == trackTypeToFind) {
                return rendererIndex
            }
        }
        return null
    }

    private fun getAllTrackIndices(group: TrackGroup): List<Int> {
        val trackIndices = mutableListOf<Int>()
        for (trackIndex in 0 until group.length) {
            trackIndices.add(trackIndex)
        }
        return trackIndices
    }

    private fun findTrackItemIndexForLanguage(language: String?,
                                              items: List<TrackSelectionItem>): Int? {
        if (language != null) {
            items.forEachIndexed { index, trackSelectionItem ->
                if (language == trackSelectionItem.language) {
                    return index
                }
            }
        }
        return null
    }

}