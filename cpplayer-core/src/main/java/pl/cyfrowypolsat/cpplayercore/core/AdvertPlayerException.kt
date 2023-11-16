package pl.cyfrowypolsat.cpplayercore.core

import com.google.ads.interactivemedia.v3.api.AdError


class AdvertPlayerException(cause: Throwable,
                            val code: Int) : Exception(cause) {

    companion object {

        fun create(e: AdError): AdvertPlayerException {
            return AdvertPlayerException(cause = e,
                    code = getErrorCode(e))
        }

        private fun getErrorCode(e: AdError): Int {
            return e.errorCodeNumber
        }
    }
}
