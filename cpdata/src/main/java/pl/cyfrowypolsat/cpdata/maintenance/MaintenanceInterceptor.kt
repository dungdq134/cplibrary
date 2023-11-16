package pl.cyfrowypolsat.cpdata.maintenance

import android.content.Context
import android.content.Intent
import io.reactivex.rxjava3.core.Observable
import okhttp3.Interceptor
import okhttp3.Response
import pl.cyfrowypolsat.cpdata.common.manager.AppDataManager
import pl.cyfrowypolsat.cpdata.local.SharedPrefs
import pl.cyfrowypolsat.cpdata.maintenance.MaintenanceActivity.Companion.MAINTENANCE_RESULT_KEY
import pl.cyfrowypolsat.cpdata.maintenance.model.MaintenanceResult
import java.io.IOException
import java.util.*
import javax.inject.Inject

internal class MaintenanceInterceptor
@Inject constructor(private val context: Context,
                    private val sharedPrefs: SharedPrefs,
                    private val appDataManager: AppDataManager) : Interceptor {

    companion object {
        private const val MODE_HEADER = "X-PORTAL-MODE"
        private const val PORTAL_MODE_MAINTENANCE = "maintenance"
        private const val DEFAULT_JSON_REFRESH_INTERVAL = 60000L
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        var response = chain.proceed(request)

        try {
            while (appDataManager.maintenanceModeDisabled.not()
                    && response.headers[MODE_HEADER] == PORTAL_MODE_MAINTENANCE) {
                val maintenanceInfo = getMaintenanceInfo().blockingFirst()

                if (MaintenanceActivity.instance == null) {
                    val intent = Intent(context, MaintenanceActivity::class.java)
                    intent.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                    intent.putExtra(MAINTENANCE_RESULT_KEY, maintenanceInfo)
                    context.startActivity(intent)
                } else {
                    MaintenanceActivity.instance?.updateData(maintenanceInfo)
                }

                Thread.sleep(getSleepInterval(maintenanceInfo))

                if (shouldRetryRequest(maintenanceInfo)) {
                    response = chain.proceed(request)
                }
            }
        } catch (_: IOException) {
        } // catch timeout events, so maintenance activity could finish

        MaintenanceActivity.instance?.maintenanceFinished()

        return response
    }

    private fun getSleepInterval(maintenanceResult: MaintenanceResult): Long {
        return if (maintenanceResult.autoRefreshInterval > 0) {
            maintenanceResult.autoRefreshInterval.toLong() * 1000
        } else if (maintenanceResult.jsonRefreshInterval > 0) {
            maintenanceResult.jsonRefreshInterval.toLong() * 1000
        } else {
            DEFAULT_JSON_REFRESH_INTERVAL
        }
    }

    private fun shouldRetryRequest(maintenanceResult: MaintenanceResult): Boolean {
        if (maintenanceResult.autoRefreshInterval <= 0 && (maintenanceResult.endData?.plannedEnd != null && maintenanceResult.endData.plannedEnd.after(Date())))
            return false
        return true
    }

    private fun getMaintenanceInfo(): Observable<MaintenanceResult> {
        return sharedPrefs.configuration?.maintenanceMode?.url?.let { url ->
            MaintenanceClient.maintenanceApi.getMaintenanceData(url)
                    .onErrorReturn { MaintenanceResult.defaultValues(context) }
        } ?: Observable.just(MaintenanceResult.defaultValues(context))
    }

}