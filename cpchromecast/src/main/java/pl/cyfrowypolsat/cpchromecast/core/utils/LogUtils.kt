package pl.cyfrowypolsat.cpchromecast.core.utils

import timber.log.Timber

object LogUtils {
    fun logLongString(tag: String, str: String) {
        if (str.length > 4000) {
            Timber.tag(tag).i(str.substring(0, 4000))
            logLongString(tag, str.substring(4000))
        } else {
            Timber.tag(tag).i(str)
        }
    }
}