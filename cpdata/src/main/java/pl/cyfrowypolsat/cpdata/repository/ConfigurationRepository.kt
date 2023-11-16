package pl.cyfrowypolsat.cpdata.repository

import android.os.SystemClock
import io.reactivex.rxjava3.core.Observable
import pl.cyfrowypolsat.cpdata.CpData
import pl.cyfrowypolsat.cpdata.api.system.SystemService
import pl.cyfrowypolsat.cpdata.api.system.response.ConfigurationResponse
import pl.cyfrowypolsat.cpdata.local.SharedPrefs
import javax.inject.Inject


class ConfigurationRepository(private val systemService: SystemService) {

    @Inject
    lateinit var sharedPrefs: SharedPrefs

    init {
        CpData.getInstance().component.inject(this)
    }

    private var configurationResponse: ConfigurationResponse? = null

    fun getConfiguration(): Observable<ConfigurationResponse> {
        return configurationResponse?.let {
            Observable.just(it)
        } ?: run {
            setConfigurationFromLocal()
            getConfigurationFromApiAndSave()
                    .onErrorResumeNext { e: Throwable ->
                        sharedPrefs.configuration?.let {
                            Observable.just(it)
                        } ?: Observable.error(e)
                    }
        }
    }

    fun getCachedConfiguration(): ConfigurationResponse? {
        return configurationResponse ?: sharedPrefs.configuration
    }

    fun getConfigurationFromApiAndSave(): Observable<ConfigurationResponse> {
        return systemService.getConfiguration()
                .doOnNext {
                    sharedPrefs.configuration = it
                    sharedPrefs.configurationTime = it.time
                    sharedPrefs.deviceTime = SystemClock.elapsedRealtime()
                    sharedPrefs.userAgent = it.userAgent
                    configurationResponse = it
                }
    }

    fun setConfigurationFromLocal() {
        configurationResponse = sharedPrefs.configuration
    }

}