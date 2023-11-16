package pl.cyfrowypolsat.cpdata.api.common.session

import io.reactivex.rxjava3.core.Observable
import pl.cyfrowypolsat.cpdata.api.auth.request.profiles.SetSessionProfileParams
import pl.cyfrowypolsat.cpdata.api.common.jsonrpc.JsonRPC
import pl.cyfrowypolsat.cpdata.api.common.jsonrpc.JsonRPCParams
import pl.cyfrowypolsat.cpdata.api.common.model.AuthResult
import pl.cyfrowypolsat.cpdata.api.common.model.Result
import pl.cyfrowypolsat.cpdata.api.system.response.AuthServiceConfig
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Url

internal interface AutoLoginApi {

    @POST
    @JsonRPC(AuthServiceConfig.LOGIN)
    fun login(@Url url: String, @Body jsonRPCParams: JsonRPCParams): Observable<AuthResult>

    @POST
    @JsonRPC(AuthServiceConfig.SET_SESSION_PROFILE)
    fun setSessionProfile(@Url url: String, @Body setSessionProfileParams: SetSessionProfileParams): Observable<Result>

}
