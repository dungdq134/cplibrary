package pl.cyfrowypolsat.cpdata.repository

import io.reactivex.rxjava3.core.Observable
import pl.cyfrowypolsat.cpdata.CpData
import pl.cyfrowypolsat.cpdata.api.common.model.cpidobject.ChannelProgramItemResult
import pl.cyfrowypolsat.cpdata.api.navigation.NavigationService
import pl.cyfrowypolsat.cpdata.api.navigation.request.channelprogram.GetChannelsProgramParams
import java.util.*


class ChannelsProgramRepository(private val navigationService: NavigationService) {

    companion object {
        private const val TV_PROGRAM_CACHE_DEFAULT_MAX_AGE_MILLISECONDS = 30 * 60 * 1000
    }

    init {
        CpData.getInstance().component.inject(this)
    }

    private var tvProgramCacheMaxAge = TV_PROGRAM_CACHE_DEFAULT_MAX_AGE_MILLISECONDS
    private val channelPrograms = HashMap<Pair<String, String>, Pair<Date, List<ChannelProgramItemResult>>>()

    fun getChannelProgramForDay(channelId: String,
                                day: Date): Observable<List<ChannelProgramItemResult>> {
        val formattedDate = GetChannelsProgramParams.getFormattedDate(day)
        val cache = channelPrograms[Pair(channelId, formattedDate)]
        return if (cache != null && cacheExpired(cache).not()) {
            Observable.just(cache.second)
        } else {
            getProgramFromApiAndSave(channelId, formattedDate)
        }
    }

    fun clearCache() {
        channelPrograms.clear()
    }

    private fun getProgramFromApiAndSave(channelId: String,
                                         formattedDate: String): Observable<List<ChannelProgramItemResult>> {
        val params = GetChannelsProgramParams.create(channelId, formattedDate)
        return navigationService.getChannelsProgram(params)
                .onErrorReturn {
                    Pair(null, mapOf(Pair(channelId, listOf())))
                }
                .doOnNext {
                    it.first?.maxAgeMilliseconds?.let { maxAge -> tvProgramCacheMaxAge = maxAge }
                    channelPrograms[Pair(channelId, formattedDate)] = Pair(Date(), it.second[channelId]
                            ?: listOf())
                }
                .map { it.second[channelId] ?: listOf() }
    }

    private fun cacheExpired(cache: Pair<Date, List<ChannelProgramItemResult>>): Boolean {
        return Date().time - tvProgramCacheMaxAge >= cache.first.time
    }

}