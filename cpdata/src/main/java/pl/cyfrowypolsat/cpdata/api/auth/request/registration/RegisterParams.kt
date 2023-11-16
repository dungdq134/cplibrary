package pl.cyfrowypolsat.cpdata.api.auth.request.registration

import pl.cyfrowypolsat.cpdata.api.auth.HmacGenerator
import pl.cyfrowypolsat.cpdata.api.auth.request.common.Captcha
import pl.cyfrowypolsat.cpdata.api.common.jsonrpc.JsonRPCParams
import pl.cyfrowypolsat.cpdata.api.common.model.AuthProvider
import pl.cyfrowypolsat.cpdata.api.common.model.DeviceId

data class RegistrationParams(val registrationData: RegistrationData,
                              val captcha: Captcha?,
                              val flowContext: String? = null,
                              val deviceId: DeviceId? = null) : JsonRPCParams()

open class RegistrationData(val authProvider: AuthProvider?)

class RegistrationNativeProviderData(val email: String,
                                     val password: String,
                                     val captcha: Captcha) : RegistrationData(AuthProvider.NATIVE)

class RegistrationExternalProviderData(authProvider: AuthProvider,
                                       val login: String? = null,
                                       val password: String? = null,
                                       val authToken: String? = null,
                                       val email: String? = null,
                                       val deviceId: DeviceId? = null,
                                       val authProtocolVersion: String? = null,
                                       val timestamp: Long? = null,
                                       val hmac: String? = null) : RegistrationData(authProvider) {

    companion object {
        fun stb(authProvider: AuthProvider,
                deviceId: DeviceId,
                authProtocolVersion: String,
                secretKey: String,
                email: String? = null): RegistrationExternalProviderData {
            val timestamp = System.currentTimeMillis()
            return RegistrationExternalProviderData(authProvider = authProvider,
                    deviceId = deviceId,
                    email = email,
                    authProtocolVersion = authProtocolVersion,
                    timestamp = timestamp,
                    hmac = HmacGenerator.generate(deviceId, authProtocolVersion, timestamp, secretKey))
        }
    }
}

class CodeRegistrationData(val email: String,
                           val password: String,
                           val registerCodeId: String) : RegistrationData(null)