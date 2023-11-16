package pl.cyfrowypolsat.cpstats.application.gemiusaudienceviews

import android.content.Context
import com.gemius.sdk.audience.AudienceEvent
import com.gemius.sdk.audience.BaseEvent
import pl.cyfrowypolsat.cpstats.application.ApplicationEvent
import pl.cyfrowypolsat.cpstats.application.EventType

class GemiusAudienceViewsMapper(val context: Context) {

    fun mapEvent(event: ApplicationEvent): GemiusAudienceViewsHit? {
        return when (event.eventType) {
            EventType.NAVIGATION -> buildPageViewHit(BaseEvent.EventType.PARTIAL_PAGEVIEW)
            else -> null
        }
    }

    private fun buildPageViewHit(eventType: BaseEvent.EventType): GemiusAudienceViewsHit {
        val event = AudienceEvent(context)
        event.eventType = eventType
        return GemiusAudienceViewsHit(event)
    }
}