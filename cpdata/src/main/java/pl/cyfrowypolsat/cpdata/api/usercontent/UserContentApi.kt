package pl.cyfrowypolsat.cpdata.api.usercontent

import io.reactivex.rxjava3.core.Observable
import pl.cyfrowypolsat.cpdata.api.common.jsonrpc.JsonRPC
import pl.cyfrowypolsat.cpdata.api.common.model.Result
import pl.cyfrowypolsat.cpdata.api.system.response.UserContentConfig
import pl.cyfrowypolsat.cpdata.api.usercontent.request.*
import pl.cyfrowypolsat.cpdata.api.usercontent.response.GetLatelyWatchedContentResult
import pl.cyfrowypolsat.cpdata.api.usercontent.request.GetWatchedContentParams
import pl.cyfrowypolsat.cpdata.api.usercontent.response.GetWatchedContentResult
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Url


interface UserContentApi {

    // GetWatchedContentData
    @POST
    @JsonRPC(UserContentConfig.GET_WATCHED_CONTENT_DATA)
    fun getWatchedContentData(@Url url: String, @Body params: GetWatchedContentParams): Observable<GetWatchedContentResult>

    @POST
    @JsonRPC(UserContentConfig.GET_WATCHED_CONTENT_DATA_LIST)
    fun getWatchedContentDataList(@Url url: String, @Body params: GetWatchedContentDataListParams): Observable<List<GetWatchedContentResult>>

    @POST
    @JsonRPC(UserContentConfig.GET_LATELY_WATCHED_CONTENT_DATA_LIST)
    fun getLatelyWatchedContentDataList(@Url url: String, @Body params: GetLatelyWatchedContentDataListParams): Observable<GetLatelyWatchedContentResult>

    @POST
    @JsonRPC(UserContentConfig.ADD_TO_FAVORITES)
    fun addToFavorites(@Url url: String, @Body params: AddToFavoritesParams): Observable<Result>

    @POST
    @JsonRPC(UserContentConfig.DELETE_FROM_FAVORITES)
    fun deleteFromFavorites(@Url url: String, @Body params: DeleteFromFavoritesParams): Observable<Result>

    @POST
    @JsonRPC(UserContentConfig.CHECK_FAVORITE)
    fun checkFavorite(@Url url: String, @Body params: CheckFavoriteParams): Observable<Result>
}
