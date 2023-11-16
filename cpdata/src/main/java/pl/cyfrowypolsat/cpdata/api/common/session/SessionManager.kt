package pl.cyfrowypolsat.cpdata.api.common.session

import io.reactivex.rxjava3.core.Observable
import pl.cyfrowypolsat.cpdata.api.auth.request.profiles.SetSessionProfileParams
import pl.cyfrowypolsat.cpdata.api.common.jsonrpc.JsonRPCParams
import pl.cyfrowypolsat.cpdata.api.common.model.AuthResult
import pl.cyfrowypolsat.cpdata.api.system.response.AuthServiceConfig
import pl.cyfrowypolsat.cpdata.api.system.response.Services
import pl.cyfrowypolsat.cpdata.common.manager.AccountManager
import pl.cyfrowypolsat.cpdata.common.utils.CurrentTimeProvider
import pl.cyfrowypolsat.cpdata.common.utils.DeviceIdGenerator
import pl.cyfrowypolsat.cpdata.di.CpDataQualifier
import pl.cyfrowypolsat.cpdata.local.SharedPrefs
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
internal class SessionManager
@Inject constructor(@CpDataQualifier private val autoLoginApi: AutoLoginApi,
                    private val accountManager: AccountManager,
                    private val sharedPrefs: SharedPrefs,
                    private val currentTimeProvider: CurrentTimeProvider,
                    private val sessionTokenGenerator: SessionTokenGenerator,
                    private val deviceIdGenerator: DeviceIdGenerator) {

    class NoLoginAuthDataException : Exception()

    fun getSessionAuthData(method: String, methodNamespace: String): JsonRPCParams.AuthData {
        val sessionToken = createSessionToken(method, methodNamespace)
        return JsonRPCParams.AuthData(sessionToken)
    }

    fun sessionExists(): Boolean {
        return accountManager.isUserLogged()
    }

    fun autoLogin(): Observable<AuthResult> {
        return Observable.fromCallable {}.flatMap {
            val loginUrl = sharedPrefs.configuration!!.services.auth.login.firstVersion.url
            val profileId = accountManager.getSelectedProfileId()
            autoLoginApi.login(loginUrl, JsonRPCParams(getAutoLoginAuthData()))
                    .doOnNext { accountManager.saveAuthResult(it) }
                    .flatMap { authResult ->
                        if (profileId != null) {
                            val setSessionProfileUrl = sharedPrefs.configuration!!.services.auth.setSessionProfile.firstVersion.url
                            val params = SetSessionProfileParams(profileId)
                            params.authData = getSessionAuthData(AuthServiceConfig.SET_SESSION_PROFILE, Services.AUTH_NAMESPACE)
                            autoLoginApi.setSessionProfile(setSessionProfileUrl, params)
                                    .map { authResult }
                        } else {
                            Observable.just(authResult)
                        }
                    }
        }
    }

    fun createSessionToken(method: String, methodNamespace: String): String {
        return sessionTokenGenerator.generate(method, methodNamespace, accountManager.getSession())
    }

    private fun getAutoLoginAuthData(): JsonRPCParams.AuthData {
        val deviceId = deviceIdGenerator.generateDeviceId()
        val loginAuthData = accountManager.getLoginAuthData() ?: throw NoLoginAuthDataException()
        return loginAuthData.copy(deviceId = deviceId)
    }

    fun getSessionId(): String? {
        return accountManager.getSession()?.id
    }

}
