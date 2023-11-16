package pl.cyfrowypolsat.cpstats.common.appsflyer

import android.content.Context
import com.appsflyer.AppsFlyerLib
import com.appsflyer.attribution.AppsFlyerRequestListener
import pl.cyfrowypolsat.cpstats.BuildConfig

abstract class BaseAppsFlyerHandler(protected val context: Context,
                                    protected val appsFlyerConfig: AppsFlyerConfig) {
    private var isInitialized = false

    init {
        configureAppsFlyer()
    }

    private fun configureAppsFlyer() {
        AppsFlyerLib.getInstance().setDebugLog(BuildConfig.DEBUG)
        AppsFlyerLib.getInstance().init(appsFlyerConfig.afDevKey, null, context)
        AppsFlyerLib.getInstance().start(context, appsFlyerConfig.afDevKey, object : AppsFlyerRequestListener {
            override fun onSuccess() {
                isInitialized = true
            }

            override fun onError(errorCode: Int, errorDescription: String) {
            }
        })
    }
}