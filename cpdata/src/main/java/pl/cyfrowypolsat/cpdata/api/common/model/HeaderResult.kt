package pl.cyfrowypolsat.cpdata.api.common.model

import okhttp3.Headers
import pl.cyfrowypolsat.cpdata.api.common.extensions.getCacheMaxAgeMillisecondsOrNull
import pl.cyfrowypolsat.cpdata.api.common.interceptor.GetApiHeaderInterceptor
import pl.cyfrowypolsat.cpdata.common.utils.parseIsoDateTime
import java.util.*

data class HeaderResult(val maxAgeMilliseconds: Int?) {

    companion object {
        fun from(headers: Headers?) = HeaderResult(maxAgeMilliseconds = headers.getCacheMaxAgeMillisecondsOrNull())
    }
}
