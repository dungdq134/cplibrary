package pl.cyfrowypolsat.cpdata.api.common.clientcontext

import io.reactivex.rxjava3.core.Observable
import pl.cyfrowypolsat.cpdata.CpData
import pl.cyfrowypolsat.cpdata.api.common.extensions.handleError
import pl.cyfrowypolsat.cpdata.api.system.request.GetClientContextTokenParams
import pl.cyfrowypolsat.cpdata.common.utils.DeviceIdGenerator
import pl.cyfrowypolsat.cpdata.di.CpDataQualifier
import pl.cyfrowypolsat.cpdata.local.SharedPrefs
import javax.inject.Inject

class ClientContextRepository @Inject constructor() {

    companion object {
        const val SESSION_UPDATE_DATE_HEADER = "X-Session-Update-Date"
    }

    @Inject
    lateinit var deviceIdGenerator: DeviceIdGenerator
    @Inject
    lateinit var sharedPrefs: SharedPrefs
    @CpDataQualifier @Inject
    lateinit var clientContextRefreshApi: ClientContextRefreshApi

    init {
        CpData.getInstance().component.inject(this)
    }

    fun getClientContextTokenAndSave(): Observable<String> {
        val url = sharedPrefs.configuration!!.services.system.getClientContextToken.firstVersion.url
        val params = GetClientContextTokenParams(deviceIdGenerator.generateDeviceId())
        return clientContextRefreshApi.getClientContextToken(url, params)
                .map { it.apply { handleError() } }
                .map { it ->
                    val token = it.response()?.body()!!
                    val sessionHeader = it.response()?.headers()?.get(SESSION_UPDATE_DATE_HEADER)?.toLong()
                    sharedPrefs.clientContextToken = token
                    sharedPrefs.sessionUpdateTime = sessionHeader ?: -1
                    token
                }
    }

    fun isClientContextExpired(sessionUpdateTime: Long): Boolean {
        return sharedPrefs.sessionUpdateTime < sessionUpdateTime
    }
}