package pl.cyfrowypolsat.cpdata.api.common.extensions

import okhttp3.CacheControl
import okhttp3.Headers


fun Headers?.getCacheMaxAgeMillisecondsOrNull(): Int? {
    val cacheMaxAge = this?.let {
        CacheControl.parse(it).maxAgeSeconds
    }
    return if (cacheMaxAge == null || cacheMaxAge == -1) {
        null
    } else {
        cacheMaxAge * 1000
    }
}