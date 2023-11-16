package pl.cyfrowypolsat.cpdata.repository

import io.reactivex.rxjava3.core.Observable
import pl.cyfrowypolsat.cpdata.CpData
import pl.cyfrowypolsat.cpdata.api.usercontent.UserContentService
import pl.cyfrowypolsat.cpdata.api.usercontent.request.GetWatchedContentDataListParams
import pl.cyfrowypolsat.cpdata.api.usercontent.response.GetWatchedContentResult
import pl.cyfrowypolsat.cpdata.api.usercontent.request.GetWatchedContentParams
import pl.cyfrowypolsat.cpdata.local.WatchedContentDao
import javax.inject.Inject


class WatchedContentDataRepository(private val userContentService: UserContentService) {

    @Inject lateinit var watchedContentDao: WatchedContentDao

    init {
        CpData.getInstance().component.inject(this)
    }

    fun getWatchedContentDataFromApi(params: GetWatchedContentParams): Observable<GetWatchedContentResult> {
        return userContentService.getWatchedContentData(params)
                .doOnNext {
                    watchedContentDao.update(it)
                }
    }

    fun getWatchedContentDataListFromApi(params: GetWatchedContentDataListParams): Observable<List<GetWatchedContentResult>> {
        return if (params.mediaIds.isNullOrEmpty()) {
            Observable.just(listOf())
        } else {
            userContentService.getWatchedContentDataList(params)
                    .doOnNext {
                        watchedContentDao.updateAll(it)
                    }
        }
    }

    fun getWatchedContentDataListFromCache(): List<GetWatchedContentResult> {
        return watchedContentDao.getList()
    }

    fun clearCache() {
        watchedContentDao.clear()
    }

}