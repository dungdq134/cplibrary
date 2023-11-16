package pl.cyfrowypolsat.cpdata.api.system

import io.reactivex.rxjava3.core.Observable
import pl.cyfrowypolsat.cpdata.api.common.jsonrpc.JsonRPC
import pl.cyfrowypolsat.cpdata.api.common.jsonrpc.JsonRPCParams
import pl.cyfrowypolsat.cpdata.api.common.model.Result
import pl.cyfrowypolsat.cpdata.api.system.request.GetClientContextTokenParams
import pl.cyfrowypolsat.cpdata.api.system.request.GetClientIdParams
import pl.cyfrowypolsat.cpdata.api.system.response.AvatarResult
import pl.cyfrowypolsat.cpdata.api.system.response.ConfigurationResponse
import pl.cyfrowypolsat.cpdata.api.system.response.SystemServiceConfig
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Url

interface SystemApi {

    companion object {
        const val CONFIGURATION_URL = "system/"
        const val CONFIGURATION_METHOD_NAME = "getConfiguration"
    }

    @POST(CONFIGURATION_URL)
    @JsonRPC(CONFIGURATION_METHOD_NAME)
    fun getConfiguration(@Body params: JsonRPCParams): Observable<ConfigurationResponse>

    @POST
    @JsonRPC(SystemServiceConfig.GET_CLIENT_ID)
    fun getClientId(@Url url: String,
                    @Body getClientIdParams: GetClientIdParams): Observable<String>

    @POST
    @JsonRPC(SystemServiceConfig.GET_UPDATE_STATUS)
    fun getUpdateStatus(@Url url: String, @Body params: JsonRPCParams): Observable<Result>

    @POST
    @JsonRPC(SystemServiceConfig.GET_ACTIVITY_EVENTS_AUTH_TOKEN)
    fun getActivityEventsAuthToken(@Url url: String,
                                   @Body params: JsonRPCParams): Observable<String>

    @POST
    @JsonRPC(SystemServiceConfig.GET_PLAYER_EVENTS_AUTH_TOKEN)
    fun getPlayerEventsAuthToken(@Url url: String, @Body params: JsonRPCParams): Observable<String>

    @POST
    @JsonRPC(SystemServiceConfig.GET_CAC_AUTH_TOKEN)
    fun getCacAuthToken(@Url url: String, @Body params: JsonRPCParams): Observable<String>

    @POST
    @JsonRPC(SystemServiceConfig.GET_AVATARS)
    fun getAvatars(@Url url: String, @Body params: JsonRPCParams): Observable<List<AvatarResult>>

}