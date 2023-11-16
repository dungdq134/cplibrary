package pl.cyfrowypolsat.cpchromecast.domain.mapper

import pl.cyfrowypolsat.cpchromecast.data.model.received.Error
import pl.cyfrowypolsat.cpchromecast.domain.model.error.ChromecastError
import javax.inject.Inject

class ChromecastErrorMapper @Inject constructor() {

    fun map(error: Error): ChromecastError? {
        return if (error.data?.userInfo == null) {
            return null
        } else {
            ChromecastError(error.data.userInfo)
        }

    }
}