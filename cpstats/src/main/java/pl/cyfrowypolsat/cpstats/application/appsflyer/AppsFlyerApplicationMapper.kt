package pl.cyfrowypolsat.cpstats.application.appsflyer

import android.content.Context
import com.appsflyer.AFInAppEventType
import pl.cyfrowypolsat.cpstats.application.ApplicationEvent
import pl.cyfrowypolsat.cpstats.application.ApplicationLoginEvent
import pl.cyfrowypolsat.cpstats.application.ApplicationRegisterEvent
import pl.cyfrowypolsat.cpstats.common.appsflyer.AppsFlyerHit

class AppsFlyerApplicationMapper(val context: Context) {

    fun mapEvent(event: ApplicationEvent): AppsFlyerHit? {
        return when (event) {
            is ApplicationRegisterEvent -> mapRegisterEvent(event)
            is ApplicationLoginEvent -> mapLoginEvent(event)
            else -> null
        }
    }

    private fun mapRegisterEvent(event: ApplicationRegisterEvent): AppsFlyerHit {
        val values: HashMap<String, Any> = hashMapOf()
        return AppsFlyerHit(AFInAppEventType.COMPLETE_REGISTRATION, values)
    }

    private fun mapLoginEvent(event: ApplicationLoginEvent): AppsFlyerHit {
        val values: HashMap<String, Any> = hashMapOf()
        return AppsFlyerHit(AFInAppEventType.LOGIN, values)
    }
}