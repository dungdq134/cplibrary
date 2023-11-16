package pl.cyfrowypolsat.cpplayercore.configuration

import android.os.Parcelable
import androidx.media3.common.C
import androidx.media3.exoplayer.drm.MediaDrmCallback
import kotlinx.parcelize.Parcelize
import pl.cyfrowypolsat.cpcommon.domain.model.enums.MediaBadgeType
import pl.cyfrowypolsat.cpplayercore.core.seealso.SeeAlsoItem
import pl.cyfrowypolsat.cpplayercore.core.startover.StartOverProvider
import pl.cyfrowypolsat.cpplayercore.events.stats.PlayerAnalyticsListener
import java.util.*

data class PlayerConfig(val id: String,
                        val url: String,
                        val mediaType: MediaType,
                        val title: String,
                        val ageGroup: Int? = null,
                        val mediaBadges: List<MediaBadgeType>? = null,
                        var startPosition: PlayerStartPosition = MsStartPosition(DEFAULT_START_POSITION),
                        var durationMs: Long = 0,
                        val userAgent: String,
                        val mediaDrmCallback: MediaDrmCallback? = null,
                        val subtitles: List<SubtitleInfo> = listOf(),
                        val adsUrl: String? = null,
                        val introTimeline: PlaybackTimeline? = null,
                        val creditsTimeline: PlaybackTimeline? = null,
                        val startOverProvider: StartOverProvider? = null,
                        val playerAnalyticsListeners: List<PlayerAnalyticsListener> = listOf(),
                        val maxQuality: Int = Int.MAX_VALUE,
                        val lockPlayerUI: Boolean = false,
                        val autoplay: Boolean = false,
                        val seeAlsoList: List<SeeAlsoItem> = listOf(),
                        val hasSuccessor: Boolean = false,
                        val autoplayNextEpisodeTimerEnabled: Boolean = false,
                        val overlays: List<OverlayInfo> = listOf()) {

    companion object {
        const val DEFAULT_START_POSITION = C.TIME_UNSET
    }

    fun isLive(): Boolean {
        return mediaType == MediaType.LIVE || mediaType == MediaType.CHANNEL
    }

    fun isOverlayEnabled(overlayType: OverlayType): Boolean {
        return overlays.firstOrNull { it.type == overlayType }?.enabled == true
    }

    fun isOverlayAutostart(overlayType: OverlayType): Boolean {
        return overlays.firstOrNull { it.type == overlayType }?.autostart == true
    }
}

sealed class PlayerStartPosition: Parcelable {

    fun startPositionMs(): Long? {
        return when(this) {
            is MsStartPosition -> this.startPositionMs
            else -> null
        }
    }

    fun startPositionDate(): Date? {
        return when(this) {
            is DateStartPosition -> this.startPositionDate
            else -> null
        }
    }
}

@Parcelize
class DateStartPosition(val startPositionDate: Date): PlayerStartPosition()

@Parcelize
class MsStartPosition(val startPositionMs: Long): PlayerStartPosition()
