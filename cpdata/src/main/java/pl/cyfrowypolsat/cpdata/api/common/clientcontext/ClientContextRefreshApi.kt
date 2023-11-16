package pl.cyfrowypolsat.cpdata.api.common.clientcontext

import io.reactivex.rxjava3.core.Observable
import pl.cyfrowypolsat.cpdata.api.common.jsonrpc.JsonRPC
import pl.cyfrowypolsat.cpdata.api.system.request.GetClientContextTokenParams
import pl.cyfrowypolsat.cpdata.api.system.response.SystemServiceConfig
import retrofit2.adapter.rxjava3.Result
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Url

interface ClientContextRefreshApi {

    @POST
    @JsonRPC(SystemServiceConfig.GET_CLIENT_CONTEXT_TOKEN)
    fun getClientContextToken(@Url url: String,
                              @Body getClientContextTokenParams: GetClientContextTokenParams): Observable<Result<String>>

}
