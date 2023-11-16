package pl.cyfrowypolsat.cpstats.application.appsflyer

import android.content.Context
import android.os.Handler
import android.os.Looper
import com.appsflyer.AppsFlyerLib
import pl.cyfrowypolsat.cpstats.application.ApplicationEvent
import pl.cyfrowypolsat.cpstats.application.ApplicationEventHandler
import pl.cyfrowypolsat.cpstats.common.appsflyer.AppsFlyerConfig
import pl.cyfrowypolsat.cpstats.common.appsflyer.BaseAppsFlyerHandler

class AppsFlyerApplicationHandler(context: Context,
                                  appsFlyerConfig: AppsFlyerConfig) : BaseAppsFlyerHandler(context, appsFlyerConfig), ApplicationEventHandler {
    private val mapper: AppsFlyerApplicationMapper = AppsFlyerApplicationMapper(context)

    override fun handleEvent(event: ApplicationEvent) {
        Handler(Looper.getMainLooper()).post {
            try {
                val appsFlyerHit = mapper.mapEvent(event)
                appsFlyerHit?.let {
                    AppsFlyerLib.getInstance().logEvent(context,
                            it.eventType, it.eventValues)
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }
}