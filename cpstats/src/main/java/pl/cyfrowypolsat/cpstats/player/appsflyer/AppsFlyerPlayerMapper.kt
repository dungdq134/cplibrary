package pl.cyfrowypolsat.cpstats.player.appsflyer

import android.content.Context
import com.appsflyer.AFInAppEventParameterName
import pl.cyfrowypolsat.cpstats.common.appsflyer.AFInAppCustomEventParameterName
import pl.cyfrowypolsat.cpstats.common.appsflyer.AFInAppCustomEventType
import pl.cyfrowypolsat.cpstats.common.appsflyer.AppsFlyerHit
import pl.cyfrowypolsat.cpstats.player.PlayerEvent
import pl.cyfrowypolsat.cpstats.player.PlayerPlaybackStartedEvent

class AppsFlyerPlayerMapper(val context: Context) {

    fun mapEvent(event: PlayerEvent): AppsFlyerHit? {
        return when (event) {
            is PlayerPlaybackStartedEvent -> mapPlaybackStartedEvent(event)
            else -> null
        }
    }

    private fun mapPlaybackStartedEvent(event: PlayerPlaybackStartedEvent): AppsFlyerHit {
        val values: HashMap<String, Any> = hashMapOf()
        values[AFInAppEventParameterName.CONTENT_ID] = event.playerData.mediaId.id
        values[AFInAppEventParameterName.CONTENT_TYPE] = event.playerData.mediaId.type
        values[AFInAppEventParameterName.CONTENT] = event.playerData.mediaTitle
        values[AFInAppCustomEventParameterName.MEDIA_DURATION] = event.playerData.durationMs
        return AppsFlyerHit(AFInAppCustomEventType.MEDIA_PLAY, values)
    }
}