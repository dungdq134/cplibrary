package pl.cyfrowypolsat.cpdata.repository

import io.reactivex.rxjava3.core.Observable
import pl.cyfrowypolsat.cpdata.CpData
import pl.cyfrowypolsat.cpdata.api.navigation.NavigationService
import pl.cyfrowypolsat.cpdata.api.navigation.request.common.InputFilter
import pl.cyfrowypolsat.cpdata.api.navigation.request.tvchannel.GetTvChannelsParams
import pl.cyfrowypolsat.cpdata.api.navigation.response.tvchannel.TvChannelListItemResult
import java.util.*


class TvChannelsRepository(private val navigationService: NavigationService,
                           private val itemsPerPage: Int) {

    companion object {
        private const val TV_CHANNELS_CACHE_DEFAULT_MAX_AGE_MILLISECONDS = 30 * 60 * 1000
    }

    init {
        CpData.getInstance().component.inject(this)
    }

    private var tvChannelsCacheMaxAge = TV_CHANNELS_CACHE_DEFAULT_MAX_AGE_MILLISECONDS
    private val tvChannels = HashMap<Int, Pair<Date, List<TvChannelListItemResult>>>()


    fun clearCache() {
        tvChannels.clear()
    }

    fun getTvChannels(page: Int,
                      filters: List<InputFilter> = listOf()): Observable<List<TvChannelListItemResult>> {
        return if (filters.isNullOrEmpty()) {
            maybeGetPageFromCache(page)
        } else {
            navigationService.getTvChannels(getChannelsParams(page, filters))
                    .map { it.second.results }
        }
    }

    fun getTvChannels(channelId: String): Observable<Pair<Int, List<TvChannelListItemResult>>> {
        val pageWithChannelId = findPage(channelId)
        return if (pageWithChannelId != null) {
            maybeGetPageFromCache(pageWithChannelId)
                    .map { Pair(pageWithChannelId, it) }
        } else {
            var page = 0
            var channels: List<TvChannelListItemResult> = listOf()
            Observable.range(0, 10)
                    .concatMap {
                        page = it
                        getPageFromApiAndSave(page)
                    }
                    .doOnNext { channels = it }
                    .map { Pair(page, it) }
                    .takeUntil { channels.isEmpty() || findPage(channelId) != null }
                    .lastOrError()
                    .toObservable()
        }
    }

    private fun maybeGetPageFromCache(page: Int): Observable<List<TvChannelListItemResult>> {
        val cache = tvChannels[page]
        return if (cache != null && cacheExpired(cache).not()) {
            Observable.just(cache.second)
        } else {
            getPageFromApiAndSave(page)
                    .onErrorResumeNext { it: Throwable ->
                        if (cache != null) {
                            Observable.just(cache.second)
                        } else {
                            Observable.error(it)
                        }
                    }
        }
    }

    private fun getPageFromApiAndSave(page: Int): Observable<List<TvChannelListItemResult>> {
        return navigationService.getTvChannels(getChannelsParams(page))
                .doOnNext {
                    it.first?.maxAgeMilliseconds?.let { maxAge -> tvChannelsCacheMaxAge = maxAge }
                    tvChannels[page] = Pair(Date(), it.second.results)
                }
                .map { it.second.results }
    }

    private fun findPage(channelId: String): Int? {
        return tvChannels.filterValues { cache -> cache.second.map { it.id }.contains(channelId) }
                .keys.firstOrNull()
    }

    private fun cacheExpired(cache: Pair<Date, List<TvChannelListItemResult>>): Boolean {
        return Date().time - tvChannelsCacheMaxAge >= cache.first.time
    }

    private fun getChannelsParams(page: Int,
                                  filters: List<InputFilter> = listOf()): GetTvChannelsParams {
        return GetTvChannelsParams(offset = page * itemsPerPage,
                limit = itemsPerPage,
                filters = filters)
    }

}