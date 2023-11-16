package pl.cyfrowypolsat.cpdata.api.usercontent

import io.reactivex.rxjava3.core.Observable
import pl.cyfrowypolsat.cpdata.CpData
import pl.cyfrowypolsat.cpdata.api.common.model.Result
import pl.cyfrowypolsat.cpdata.api.usercontent.request.*
import pl.cyfrowypolsat.cpdata.api.usercontent.response.GetLatelyWatchedContentResult
import pl.cyfrowypolsat.cpdata.api.usercontent.request.GetWatchedContentParams
import pl.cyfrowypolsat.cpdata.api.usercontent.response.GetWatchedContentResult
import pl.cyfrowypolsat.cpdata.di.CpDataQualifier
import pl.cyfrowypolsat.cpdata.repository.ConfigurationRepository
import javax.inject.Inject


class UserContentService(private val configurationRepository: ConfigurationRepository) {

    @CpDataQualifier
    @Inject lateinit var userContentApi: UserContentApi

    init {
        CpData.getInstance().component.inject(this)
    }


    // GetWatchedContentData
    fun getWatchedContentData(params: GetWatchedContentParams): Observable<GetWatchedContentResult> {
        return configurationRepository.getConfiguration()
                .flatMap { configurationResponse ->
                    val url = configurationResponse.services.userContent.getWatchedContentData.firstVersion.url
                    userContentApi.getWatchedContentData(url, params)
                }
    }

    fun getWatchedContentDataList(params: GetWatchedContentDataListParams): Observable<List<GetWatchedContentResult>> {
        return configurationRepository.getConfiguration()
                .flatMap { configurationResponse ->
                    val url = configurationResponse.services.userContent.getWatchedContentDataList.firstVersion.url
                    userContentApi.getWatchedContentDataList(url, params)
                }
    }

    fun getLatelyWatchedContentDataList(params: GetLatelyWatchedContentDataListParams): Observable<GetLatelyWatchedContentResult> {
        return configurationRepository.getConfiguration()
                .flatMap { configurationResponse ->
                    val url = configurationResponse.services.userContent.getLatelyWatchedContentDataList.firstVersion.url
                    userContentApi.getLatelyWatchedContentDataList(url, params)
                }
    }

    // Favorites
    fun addToFavorites(params: AddToFavoritesParams): Observable<Result> {
        return configurationRepository.getConfiguration()
                .flatMap { configurationResponse ->
                    val url = configurationResponse.services.userContent.addToFavorites.firstVersion.url
                    userContentApi.addToFavorites(url, params)
                }
    }

    fun deleteFromFavorites(params: DeleteFromFavoritesParams): Observable<Result> {
        return configurationRepository.getConfiguration()
                .flatMap { configurationResponse ->
                    val url = configurationResponse.services.userContent.deleteFromFavorites.firstVersion.url
                    userContentApi.deleteFromFavorites(url, params)
                }
    }

    fun checkFavorite(params: CheckFavoriteParams): Observable<Result> {
        return configurationRepository.getConfiguration()
                .flatMap { configurationResponse ->
                    val url = configurationResponse.services.userContent.checkFavorite.firstVersion.url
                    userContentApi.checkFavorite(url, params)
                }
    }

}