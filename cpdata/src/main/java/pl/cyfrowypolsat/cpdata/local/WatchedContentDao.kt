package pl.cyfrowypolsat.cpdata.local

import pl.cyfrowypolsat.cpdata.api.usercontent.response.GetWatchedContentResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WatchedContentDao @Inject constructor() {

    private val watchedContent = HashMap<String, GetWatchedContentResult>()

    @Synchronized
    fun getList(): List<GetWatchedContentResult> {
        return watchedContent.values.toList()
    }

    @Synchronized
    fun updateAll(newData: List<GetWatchedContentResult>) {
        newData.forEach { watchedContent[it.mediaId.id] = it }
    }

    @Synchronized
    fun update(watchedContentResult: GetWatchedContentResult) {
        watchedContent[watchedContentResult.mediaId.id] = watchedContentResult
    }

    @Synchronized
    fun clear() {
        watchedContent.clear()
    }
}