package pl.cyfrowypolsat.cpdata.api.common.session

import android.util.Base64
import pl.cyfrowypolsat.cpdata.api.common.model.Session
import timber.log.Timber
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionTokenGenerator
@Inject constructor() {

    fun generate(method: String, methodNamespace: String, session: Session?): String {
        session ?: return ""

        var sessionToken = ""
        try {
            val sessionRpcMethodString = session.id + "|" + session.keyExpirationTime.toString() + "|" + methodNamespace + "|" + method

            val sha256Hmac = Mac.getInstance("HmacSHA256")
            val secretKey = SecretKeySpec(Base64.decode(session.key, Base64.URL_SAFE or Base64.NO_WRAP), "HmacSHA256")
            sha256Hmac.init(secretKey)

            val sessionRpcMethodDigest = sha256Hmac.doFinal(sessionRpcMethodString.toByteArray(charset("UTF-8")))
            sessionToken = sessionRpcMethodString + '|' + Base64.encodeToString(sessionRpcMethodDigest, Base64.URL_SAFE or Base64.NO_WRAP)
        } catch (e: Exception) {
            Timber.e(e)
            // TODO log to crashlytics
        }

        return sessionToken
    }

}