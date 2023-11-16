package pl.cyfrowypolsat.cpplayerexternal.domain

import androidx.media3.common.MimeTypes
import pl.cyfrowypolsat.cpplayercore.configuration.MediaType
import pl.cyfrowypolsat.cpplayercore.configuration.PlaybackTimeline
import pl.cyfrowypolsat.cpplayercore.configuration.PlayerConfig
import pl.cyfrowypolsat.cpplayercore.configuration.SubtitleInfo
import pl.cyfrowypolsat.cpplayerexternal.data.model.MediaSource
import pl.cyfrowypolsat.cpplayerexternal.data.model.MediaSource.Companion.DASH_ACCESS_METHOD
import pl.cyfrowypolsat.cpplayerexternal.data.model.MediaSource.Companion.DIRECT_ACCESS_METHOD
import pl.cyfrowypolsat.cpplayerexternal.data.model.MediaSource.Companion.HLS_ACCESS_METHOD
import pl.cyfrowypolsat.cpplayerexternal.data.model.MediaSource.Companion.HLS_TIMESHIFT_ACCESS_METHOD
import pl.cyfrowypolsat.cpplayerexternal.data.model.PrePlayData
import timber.log.Timber


class PlayerConfigMapper {

    companion object {
        private const val VTT_SUBTITLE_FORMAT = "vtt"
        private const val SRT_SUBTITLE_FORMAT = "srt"
        private const val DEFAULT_USER_AGENT = "mobile_android_native_cpplayer"
    }

    fun map(prePlayData: PrePlayData, adsUrl: String?): PlayerConfig {
        Timber.d("AdsUrl: $adsUrl")
        val mediaSource = findBestMediaSource(prePlayData.mediaItem.playback.mediaSources)
        return PlayerConfig(id = prePlayData.mediaItem.id,
                url = mediaSource!!.url!!,
                mediaType = MediaType.getFromString(prePlayData.mediaItem.playback.mediaType),
                title = prePlayData.mediaItem.displayInfo.title,
                ageGroup = prePlayData.mediaItem.displayInfo.ageGroup,
                userAgent = prePlayData.userAgent ?: DEFAULT_USER_AGENT,
                subtitles = getSubtitleInfoList(prePlayData),
                adsUrl = adsUrl,
                durationMs = getDuration(prePlayData),
                introTimeline = getIntroTimeline(prePlayData))
    }

    private fun findBestMediaSource(sources: List<MediaSource>): MediaSource? {
        val dashSources = sources.filter { it.accessMethod == DASH_ACCESS_METHOD }
        if (dashSources.isNotEmpty()) {
            return dashSources.last()
        }

        val directSources = sources.filter { it.accessMethod == DIRECT_ACCESS_METHOD }
        if (directSources.isNotEmpty()) {
            return directSources.last()
        }

        val hlsSources = sources.filter { it.accessMethod == HLS_ACCESS_METHOD || it.accessMethod == HLS_TIMESHIFT_ACCESS_METHOD }
        if (hlsSources.isNotEmpty()) {
            return hlsSources.last()
        }

        return sources.lastOrNull()
    }

    private fun getSubtitleInfoList(prePlayData: PrePlayData): List<SubtitleInfo> {
        val subtitles = prePlayData.mediaItem.displayInfo.subtitles ?: return listOf()
        val vttSubtitles = subtitles.filter { it.format == VTT_SUBTITLE_FORMAT }
        val srtSubtitles = subtitles.filter { it.format == SRT_SUBTITLE_FORMAT }
        return if (vttSubtitles.isNotEmpty()) {
            vttSubtitles.map {
                SubtitleInfo(url = it.src,
                        mimeType = MimeTypes.TEXT_VTT,
                        language = it.name)
            }
        } else {
            srtSubtitles.map {
                SubtitleInfo(url = it.src,
                        mimeType = MimeTypes.APPLICATION_SUBRIP,
                        language = it.name)
            }
        }
    }

    private fun getDuration(prePlayData: PrePlayData): Long {
        val duration = prePlayData.mediaItem.playback.duration ?: 0
        return duration * 1000L
    }

    private fun getIntroTimeline(prePlayData: PrePlayData): PlaybackTimeline? {
        val head = prePlayData.mediaItem.playback.timeline?.find { it.type == "head" }
                ?: return null
        return if (head.stop > head.start) {
            PlaybackTimeline(start = head.start,
                    stop = head.stop)
        } else {
            null
        }
    }

}