package pl.cyfrowypolsat.cpdata.common.utils

import android.os.SystemClock
import pl.cyfrowypolsat.cpdata.local.SharedPrefs
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurrentTimeProvider
@Inject constructor(private val sharedPrefs: SharedPrefs) {

    fun getCurrentTimeInMillis(): Long {
        val configurationTime = sharedPrefs.configurationTime
        val deviceTime = sharedPrefs.deviceTime
        return if (configurationTime != null && deviceTime != null) {
            val difference = SystemClock.elapsedRealtime() - deviceTime
            configurationTime * 1000 + difference
        } else {
            System.currentTimeMillis()
        }
    }

}