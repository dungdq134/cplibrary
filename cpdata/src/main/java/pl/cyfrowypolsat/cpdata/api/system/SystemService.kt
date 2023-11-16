package pl.cyfrowypolsat.cpdata.api.system

import io.reactivex.rxjava3.core.Observable
import pl.cyfrowypolsat.cpdata.CpData
import pl.cyfrowypolsat.cpdata.api.common.jsonrpc.JsonRPCParams
import pl.cyfrowypolsat.cpdata.api.common.model.Result
import pl.cyfrowypolsat.cpdata.api.system.request.GetClientContextTokenParams
import pl.cyfrowypolsat.cpdata.api.system.request.GetClientIdParams
import pl.cyfrowypolsat.cpdata.api.system.response.AvatarResult
import pl.cyfrowypolsat.cpdata.api.system.response.ConfigurationResponse
import pl.cyfrowypolsat.cpdata.common.utils.DeviceIdGenerator
import pl.cyfrowypolsat.cpdata.di.CpDataQualifier
import javax.inject.Inject


class SystemService {

    @CpDataQualifier
    @Inject
    lateinit var systemApi: SystemApi
    @Inject
    lateinit var deviceIdGenerator: DeviceIdGenerator

    init {
        CpData.getInstance().component.inject(this)
    }

    fun getConfiguration(): Observable<ConfigurationResponse> {
        return systemApi.getConfiguration(JsonRPCParams())
    }

    fun getClientId(url: String): Observable<String> {
        val params = GetClientIdParams(deviceIdGenerator.generateDeviceId())
        return systemApi.getClientId(url, params)
    }

    fun getUpdateStatus(url: String): Observable<Result> {
        return systemApi.getUpdateStatus(url, JsonRPCParams())
    }

    fun getActivityEventsAuthToken(url: String): Observable<String> {
        return systemApi.getActivityEventsAuthToken(url, JsonRPCParams())
    }

    fun getPlayerEventsAuthToken(url: String): Observable<String> {
        return systemApi.getPlayerEventsAuthToken(url, JsonRPCParams())
    }

    fun getCacAuthToken(url: String): Observable<String> {
        return systemApi.getCacAuthToken(url, JsonRPCParams())
    }

    fun getAvatars(url: String): Observable<List<AvatarResult>> {
        return systemApi.getAvatars(url, JsonRPCParams())
    }
}