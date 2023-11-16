package pl.cyfrowypolsat.cpdata.api.auth.request.login

import pl.cyfrowypolsat.cpdata.api.auth.HmacGenerator
import pl.cyfrowypolsat.cpdata.api.common.jsonrpc.JsonRPCParams
import pl.cyfrowypolsat.cpdata.api.common.model.AuthProvider
import pl.cyfrowypolsat.cpdata.api.common.model.DeviceId

data class LoginAuthData(val authProvider: AuthProvider,
                         val login: String? = null,
                         val password: String? = null,
                         val msisdn: String? = null,
                         val authToken: String? = null,
                         val authProtocolVersion: String? = null,
                         val timestamp: Long? = null,
                         val hmac: String? = null,
                         val deviceId: DeviceId) : JsonRPCParams.AuthData(null) {

    companion object {
        fun native(login: String?, password: String?, deviceId: DeviceId): LoginAuthData {
            return LoginAuthData(authProvider = AuthProvider.NATIVE,
                    login = login,
                    password = password,
                    deviceId = deviceId)
        }

        fun plus(msisdn: String?, password: String?, deviceId: DeviceId): LoginAuthData {
            return LoginAuthData(authProvider = AuthProvider.PLUS,
                    msisdn = msisdn,
                    password = password,
                    deviceId = deviceId)
        }

        fun icok(login: String?, password: String?, deviceId: DeviceId): LoginAuthData {
            return LoginAuthData(authProvider = AuthProvider.ICOK,
                    login = login,
                    password = password,
                    deviceId = deviceId)
        }

        fun facebook(authToken: String?, deviceId: DeviceId): LoginAuthData {
            return authToken(AuthProvider.FACEBOOK, authToken, deviceId)
        }

        fun netia(login: String?, password: String?, deviceId: DeviceId): LoginAuthData {
            return LoginAuthData(authProvider = AuthProvider.NETIA,
                    login = login,
                    password = password,
                    deviceId = deviceId)
        }

        fun sso(login: String?, password: String?, deviceId: DeviceId): LoginAuthData {
            return LoginAuthData(authProvider = AuthProvider.SSO,
                login = login,
                password = password,
                deviceId = deviceId)
        }

        fun stb(authProvider: AuthProvider,
                deviceId: DeviceId,
                authProtocolVersion: String,
                secretKey: String): LoginAuthData {
            val timestamp = System.currentTimeMillis()
            return LoginAuthData(authProvider = authProvider,
                    deviceId = deviceId,
                    authProtocolVersion = authProtocolVersion,
                    timestamp = timestamp,
                    hmac = HmacGenerator.generate(deviceId, authProtocolVersion, timestamp, secretKey))
        }

        fun authToken(authProvider: AuthProvider,
                      authToken: String?,
                      deviceId: DeviceId): LoginAuthData {
            return LoginAuthData(authProvider = authProvider,
                    authToken = authToken,
                    deviceId = deviceId)
        }
    }
}