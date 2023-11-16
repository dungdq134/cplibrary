package pl.cyfrowypolsat.cpdata.api.drm

import io.reactivex.rxjava3.core.Observable
import pl.cyfrowypolsat.cpdata.api.common.jsonrpc.JsonRPC
import pl.cyfrowypolsat.cpdata.api.common.model.Result
import pl.cyfrowypolsat.cpdata.api.drm.request.*
import pl.cyfrowypolsat.cpdata.api.drm.response.*
import pl.cyfrowypolsat.cpdata.api.system.response.DrmServiceConfig
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Url

interface DrmApi {

    @POST
    @JsonRPC(DrmServiceConfig.CHECK_PRODUCT_ACCESS)
    fun checkProductAccess(@Url url: String,
                           @Body checkProductAccessParams: CheckProductAccessParams): Observable<CheckProductAccessResult>

    @POST
    @JsonRPC(DrmServiceConfig.CHECK_PRODUCTS_ACCESS)
    fun checkProductsAccess(@Url url: String,
                            @Body checkProductsAccessParams: CheckProductsAccessParams): Observable<List<CheckProductsAccessResult>>

    @POST
    @JsonRPC(DrmServiceConfig.GET_PSEUDO_LICENSE)
    fun getPseudoLicense(@Url url: String,
                         @Body params: GetPseudoLicenseParams): Observable<GetPseudoLicenseResult>

    @POST
    @JsonRPC(DrmServiceConfig.GET_WIDEVINE_LICENSE)
    fun getWidevineLicense(@Url url: String,
                           @Body params: GetWidevineLicenseParams): Observable<GetWidevineLicenseResult>

    @POST
    @JsonRPC(DrmServiceConfig.STOP_PLAYBACK)
    fun stopPlayback(@Url url: String, @Body params: StopPlaybackParams): Observable<Result>

    @POST
    @JsonRPC(DrmServiceConfig.GET_USER_PLAYBACKS)
    fun getUserPlaybacks(@Url url: String,
                         @Body params: GetUserPlaybacksParams): Observable<List<GetUserPlaybackResult>>

    @POST
    @JsonRPC(DrmServiceConfig.CHECK_PRODUCT_EXTERNAL_ACTIVATION)
    fun checkProductExternalActivation(@Url url: String, @Body params: CheckProductExternalActivationParams): Observable<Result>

    @POST
    @JsonRPC(DrmServiceConfig.GET_PRODUCT_EXTERNAL_ACTIVATION_DATA)
    fun getProductExternalActivationData(@Url url: String, @Body params: GetProductExternalActivationDataParams): Observable<GetProductExternalActivationDataResult>

}
