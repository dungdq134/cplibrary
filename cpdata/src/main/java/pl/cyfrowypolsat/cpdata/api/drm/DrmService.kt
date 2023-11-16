package pl.cyfrowypolsat.cpdata.api.drm

import io.reactivex.rxjava3.core.Observable
import pl.cyfrowypolsat.cpdata.CpData
import pl.cyfrowypolsat.cpdata.api.common.model.Result
import pl.cyfrowypolsat.cpdata.api.drm.request.*
import pl.cyfrowypolsat.cpdata.api.drm.response.*
import pl.cyfrowypolsat.cpdata.di.CpDataQualifier
import pl.cyfrowypolsat.cpdata.repository.ConfigurationRepository
import javax.inject.Inject


class DrmService(private val configurationRepository: ConfigurationRepository) {

    @CpDataQualifier
    @Inject
    lateinit var drmApi: DrmApi

    init {
        CpData.getInstance().component.inject(this)
    }

    fun checkProductAccess(checkProductAccessParams: CheckProductAccessParams): Observable<CheckProductAccessResult> {
        return configurationRepository.getConfiguration()
                .flatMap { configurationResponse ->
                    val url = configurationResponse.services.drm.checkProductAccess.firstVersion.url
                    drmApi.checkProductAccess(url, checkProductAccessParams)
                }
    }

    fun checkProductsAccess(checkProductsAccessParams: CheckProductsAccessParams): Observable<List<CheckProductsAccessResult>> {
        return configurationRepository.getConfiguration()
                .flatMap { configurationResponse ->
                    val url = configurationResponse.services.drm.checkProductsAccess.firstVersion.url
                    drmApi.checkProductsAccess(url, checkProductsAccessParams)
                }
    }

    fun getPseudoLicense(url: String,
                         params: GetPseudoLicenseParams): Observable<GetPseudoLicenseResult> {
        return drmApi.getPseudoLicense(url, params)
    }

    fun getWidevineLicense(url: String,
                           params: GetWidevineLicenseParams): Observable<GetWidevineLicenseResult> {
        return drmApi.getWidevineLicense(url, params)
    }

    fun stopPlayback(stopPlaybackParams: StopPlaybackParams): Observable<Result> {
        return configurationRepository.getConfiguration()
                .flatMap { configurationResponse ->
                    val url = configurationResponse.services.drm.stopPlayback.firstVersion.url
                    drmApi.stopPlayback(url, stopPlaybackParams)
                }
    }

    fun getUserPlaybacks(params: GetUserPlaybacksParams): Observable<List<GetUserPlaybackResult>> {
        return configurationRepository.getConfiguration()
                .flatMap { configurationResponse ->
                    val url = configurationResponse.services.drm.getUserPlaybacks.firstVersion.url
                    drmApi.getUserPlaybacks(url, params)
                }
    }

    fun checkProductExternalActivation(checkProductExternalActivationParams: CheckProductExternalActivationParams): Observable<Result> {
        return configurationRepository.getConfiguration()
            .flatMap { configurationResponse ->
                val url = configurationResponse.services.drm.checkProductExternalActivation.firstVersion.url
                drmApi.checkProductExternalActivation(url, checkProductExternalActivationParams)
            }
    }

    fun getProductExternalActivationData(getProductExternalActivationDataParams: GetProductExternalActivationDataParams): Observable<GetProductExternalActivationDataResult> {
        return configurationRepository.getConfiguration()
            .flatMap { configurationResponse ->
                val url = configurationResponse.services.drm.getProductExternalActivationData.firstVersion.url
                drmApi.getProductExternalActivationData(url, getProductExternalActivationDataParams)
            }
    }

}
