package pl.cyfrowypolsat.cpstats.player.appsflyer

import android.content.Context
import android.os.Handler
import android.os.Looper
import com.appsflyer.AppsFlyerLib
import pl.cyfrowypolsat.cpstats.common.appsflyer.AppsFlyerConfig
import pl.cyfrowypolsat.cpstats.common.appsflyer.BaseAppsFlyerHandler
import pl.cyfrowypolsat.cpstats.player.PlayerEvent
import pl.cyfrowypolsat.cpstats.player.PlayerEventHandler

class AppsFlyerPlayerHandler(context: Context,
                             appsFlyerConfig: AppsFlyerConfig) : BaseAppsFlyerHandler(context, appsFlyerConfig), PlayerEventHandler {
    private val mapper: AppsFlyerPlayerMapper = AppsFlyerPlayerMapper(context)

    override fun handleEvent(event: PlayerEvent) {
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