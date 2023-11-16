package pl.cyfrowypolsat.cpdata.common.manager

import pl.cyfrowypolsat.cpdata.CpData
import pl.cyfrowypolsat.cpdata.local.SharedPrefs
import javax.inject.Inject

class LaunchCountManager {

    @Inject lateinit var sharedPrefs: SharedPrefs


    init {
        CpData.getInstance().component.inject(this)
    }

    fun launchCount(): Int {
        return sharedPrefs.launchCount
    }

    fun isFirstLaunch(): Boolean {
        return launchCount() == 1
    }

    fun incrementLaunchCount() {
        sharedPrefs.launchCount++
    }
}