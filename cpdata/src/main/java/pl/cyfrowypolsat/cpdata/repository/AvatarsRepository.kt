package pl.cyfrowypolsat.cpdata.repository

import io.reactivex.rxjava3.core.Observable
import pl.cyfrowypolsat.cpdata.CpData
import pl.cyfrowypolsat.cpdata.api.system.SystemService
import pl.cyfrowypolsat.cpdata.api.system.response.AvatarResult
import pl.cyfrowypolsat.cpdata.local.SharedPrefs
import timber.log.Timber
import javax.inject.Inject

class AvatarsRepository(private val configurationRepository: ConfigurationRepository,
                        private val systemService: SystemService) {

    @Inject
    lateinit var sharedPrefs: SharedPrefs

    init {
        CpData.getInstance().component.inject(this)
    }

    fun getAvatars(): Observable<List<AvatarResult>> {
        return sharedPrefs.avatars?.let { Observable.just(it) } ?: getAvatarsAndSave()
    }

    fun getAvatarsAndSave(): Observable<List<AvatarResult>> {
        return configurationRepository.getConfiguration()
                .flatMap { configuration ->
                    val getAvatarsUrls = configuration.services.system.getAvatars.firstVersion.url
                    systemService.getAvatars(getAvatarsUrls)
                            .doOnNext { sharedPrefs.avatars = it }
                            .doOnError(Timber::e)
                            .onErrorReturn { sharedPrefs.avatars?.toList() ?: listOf() }
                }
    }

}