package pl.cyfrowypolsat.cpdata.repository

import io.reactivex.rxjava3.core.Observable
import pl.cyfrowypolsat.cpdata.CpData
import pl.cyfrowypolsat.cpdata.api.navigation.NavigationService
import pl.cyfrowypolsat.cpdata.api.navigation.response.homemenu.HomeMenuItemResult
import pl.cyfrowypolsat.cpdata.local.SharedPrefs
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class HomeMenuRepository(private val navigationService: NavigationService) {

    @Inject
    lateinit var sharedPrefs: SharedPrefs

    init {
        CpData.getInstance().component.inject(this)
    }

    fun initializeHomeMenu(): Observable<Boolean> {
        return navigationService.getHomeMenu()
                .timeout(5, TimeUnit.SECONDS)
                .flatMap {
                    sharedPrefs.homeMenu = it
                    Observable.just(true)
                }
                .onErrorResumeNext { t: Throwable ->
                    if (sharedPrefs.homeMenu == null) {
                        throw t
                    } else {
                        Observable.just(true)
                    }
                }
    }

    fun getHomeMenu(): Observable<List<HomeMenuItemResult>> {
        return Observable.just(sharedPrefs.homeMenu ?: listOf())
    }

}