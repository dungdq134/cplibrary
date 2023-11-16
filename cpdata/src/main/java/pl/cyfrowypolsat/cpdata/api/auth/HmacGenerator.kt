package pl.cyfrowypolsat.cpdata.api.auth

import android.util.Base64
import pl.cyfrowypolsat.cpdata.api.common.model.DeviceId
import timber.log.Timber
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

class HmacGenerator {

    companion object {
        fun generate(deviceId: DeviceId,
                     authProtocolVersion: String,
                     timestamp: Long,
                     secretKey: String): String {
            var hmac = ""
            try {
                val hmacString = deviceId.type + "|" + deviceId.value + "|" + authProtocolVersion + "|" + timestamp

                val sha256Hmac = Mac.getInstance("HmacSHA256")
                val secretKeySpec = SecretKeySpec(secretKey.toByteArray(), "HmacSHA256")
                sha256Hmac.init(secretKeySpec)

                val hmacDigest = sha256Hmac.doFinal(hmacString.toByteArray())
                hmac = Base64.encodeToString(hmacDigest, Base64.URL_SAFE or Base64.NO_WRAP)
            } catch (e: Exception) {
                Timber.e(e)
            }

            return hmac
        }
    }
}