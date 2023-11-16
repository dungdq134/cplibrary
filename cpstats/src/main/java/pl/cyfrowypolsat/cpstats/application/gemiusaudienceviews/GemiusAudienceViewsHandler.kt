package pl.cyfrowypolsat.cpstats.application.gemiusaudienceviews

import android.content.Context
import android.os.Handler
import android.os.Looper
import com.gemius.sdk.Config
import com.gemius.sdk.audience.AudienceConfig
import pl.cyfrowypolsat.cpstats.application.ApplicationEvent
import pl.cyfrowypolsat.cpstats.application.ApplicationEventHandler
import pl.cyfrowypolsat.cpstats.application.EventType
import timber.log.Timber

private const val TAG = "GemiusAudienceViews"

class GemiusAudienceViewsHandler(private val context: Context,
                                 private val gemiusAudienceViewsConfig: GemiusAudienceViewsConfig) : ApplicationEventHandler {
    private val mapper: GemiusAudienceViewsMapper = GemiusAudienceViewsMapper(context)

    init {
        configureGemiusAudience()
    }

    override fun handleEvent(event: ApplicationEvent) {
        val gemiusAudienceStreamHit = mapper.mapEvent(event)
        gemiusAudienceStreamHit?.let {
            Timber.tag(TAG).i(it.event.eventType.name)
            it.event.sendEvent()
        }
    }

    private fun configureGemiusAudience() {
        AudienceConfig.getSingleton().hitCollectorHost = gemiusAudienceViewsConfig.service
        AudienceConfig.getSingleton().scriptIdentifier = gemiusAudienceViewsConfig.account
        Config.setAppInfo(gemiusAudienceViewsConfig.appName, gemiusAudienceViewsConfig.versionName)
    }
}