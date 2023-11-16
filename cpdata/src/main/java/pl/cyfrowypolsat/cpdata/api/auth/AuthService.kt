package pl.cyfrowypolsat.cpdata.api.auth

import io.reactivex.rxjava3.core.Observable
import pl.cyfrowypolsat.cpdata.CpData
import pl.cyfrowypolsat.cpdata.api.auth.request.changeEmail.ChangeEmailParams
import pl.cyfrowypolsat.cpdata.api.auth.request.connect.ConfirmConnectionParams
import pl.cyfrowypolsat.cpdata.api.auth.request.connect.ConnectParams
import pl.cyfrowypolsat.cpdata.api.auth.request.connect.DisconnectParams
import pl.cyfrowypolsat.cpdata.api.auth.request.editaccount.ChangePasswordParams
import pl.cyfrowypolsat.cpdata.api.auth.request.editaccount.DeleteUserParams
import pl.cyfrowypolsat.cpdata.api.auth.request.login.GetLoginRequestStatusParams
import pl.cyfrowypolsat.cpdata.api.auth.request.login.LoginParams
import pl.cyfrowypolsat.cpdata.api.auth.request.profiles.CreateProfileParams
import pl.cyfrowypolsat.cpdata.api.auth.request.profiles.DeleteProfileParams
import pl.cyfrowypolsat.cpdata.api.auth.request.profiles.SetSessionProfileParams
import pl.cyfrowypolsat.cpdata.api.auth.request.profiles.UpdateProfileParams
import pl.cyfrowypolsat.cpdata.api.auth.request.registration.*
import pl.cyfrowypolsat.cpdata.api.auth.request.resetpassword.RequestPasswordResetParams
import pl.cyfrowypolsat.cpdata.api.auth.request.rules.AcceptRulesParams
import pl.cyfrowypolsat.cpdata.api.auth.request.rules.GetRulesParams
import pl.cyfrowypolsat.cpdata.api.auth.request.rules.GetRulesStatusParams
import pl.cyfrowypolsat.cpdata.api.auth.request.rules.UnacceptRulesParams
import pl.cyfrowypolsat.cpdata.api.auth.request.verification.ConfirmVerificationParams
import pl.cyfrowypolsat.cpdata.api.auth.request.verification.VerifyParams
import pl.cyfrowypolsat.cpdata.api.auth.response.ProfileResult
import pl.cyfrowypolsat.cpdata.api.auth.response.RuleStatusResult
import pl.cyfrowypolsat.cpdata.api.auth.response.login.GetLoginRequestStatusResult
import pl.cyfrowypolsat.cpdata.api.auth.response.login.RequestLoginResult
import pl.cyfrowypolsat.cpdata.api.auth.response.registration.RequestRegisterCodeResult
import pl.cyfrowypolsat.cpdata.api.common.jsonrpc.JsonRPCParams
import pl.cyfrowypolsat.cpdata.api.common.model.AuthResult
import pl.cyfrowypolsat.cpdata.api.common.model.Result
import pl.cyfrowypolsat.cpdata.api.common.model.RuleResult
import pl.cyfrowypolsat.cpdata.common.utils.DeviceIdGenerator
import pl.cyfrowypolsat.cpdata.di.CpDataQualifier
import pl.cyfrowypolsat.cpdata.local.SharedPrefs
import pl.cyfrowypolsat.cpdata.repository.ConfigurationRepository
import javax.inject.Inject


class AuthService(private val configurationRepository: ConfigurationRepository) {

    @CpDataQualifier
    @Inject
    lateinit var authApi: AuthApi

    @Inject
    lateinit var deviceIdGenerator: DeviceIdGenerator

    @Inject
    lateinit var sharedPrefs: SharedPrefs

    init {
        CpData.getInstance().component.inject(this)
    }

    // Session
    fun getSession(): Observable<AuthResult> {
        return configurationRepository.getConfiguration()
                .flatMap { configurationResponse ->
                    val url = configurationResponse.services.auth.getSession.firstVersion.url
                    authApi.getSession(url, JsonRPCParams())
                }
    }


    // Logout
    fun logout(sessionToken: String): Observable<Result> {
        return configurationRepository.getConfiguration()
                .flatMap { configurationResponse ->
                    val url = configurationResponse.services.auth.logout.firstVersion.url
                    authApi.logout(url, JsonRPCParams(JsonRPCParams.AuthData(sessionToken)))
                }
    }


    // Login
    fun login(loginParams: LoginParams): Observable<AuthResult> {
        return configurationRepository.getConfiguration()
                .flatMap { configurationResponse ->
                    val url = configurationResponse.services.auth.login.firstVersion.url
                    authApi.login(url, loginParams)
                }
    }

    fun requestLogin(): Observable<RequestLoginResult> {
        return configurationRepository.getConfiguration()
                .flatMap { configurationResponse ->
                    val url = configurationResponse.services.auth.requestLogin.firstVersion.url
                    authApi.requestLogin(url, JsonRPCParams())
                }
    }

    fun getLoginRequestStatus(loginRequestId: String): Observable<GetLoginRequestStatusResult> {
        return configurationRepository.getConfiguration()
                .flatMap { configurationResponse ->
                    val url = configurationResponse.services.auth.getLoginRequestStatus.firstVersion.url
                    val params = GetLoginRequestStatusParams(loginRequestId)
                    authApi.getLoginRequestStatus(url, params)
                }
    }

    // Reset password
    fun requestPasswordReset(requestPasswordResetParams: RequestPasswordResetParams): Observable<Result> {
        return configurationRepository.getConfiguration()
                .flatMap { configurationResponse ->
                    val url = configurationResponse.services.auth.requestPasswordReset.firstVersion.url
                    authApi.requestPasswordReset(url, requestPasswordResetParams)
                }
    }

    // Verification
    fun verify(verifyParams: VerifyParams): Observable<Result> {
        return configurationRepository.getConfiguration()
                .flatMap { configurationResponse ->
                    val url = configurationResponse.services.auth.verify.firstVersion.url
                    authApi.verify(url, verifyParams)
                }
    }

    fun confirmVerification(confirmVerificationParams: ConfirmVerificationParams): Observable<Result> {
        return configurationRepository.getConfiguration()
                .flatMap { configurationResponse ->
                    val url = configurationResponse.services.auth.confirmVerification.firstVersion.url
                    authApi.confirmVerification(url, confirmVerificationParams)
                }
    }


    // Account connection
    fun connect(connectParams: ConnectParams): Observable<Result> {
        return configurationRepository.getConfiguration()
                .flatMap { configurationResponse ->
                    val url = configurationResponse.services.auth.connect.firstVersion.url
                    authApi.connect(url, connectParams)
                }
    }

    fun confirmConnection(confirmConnectionParams: ConfirmConnectionParams): Observable<Result> {
        return configurationRepository.getConfiguration()
            .flatMap { configurationResponse ->
                val url = configurationResponse.services.auth.confirmPlusConnection.firstVersion.url
                authApi.confirmConnection(url, confirmConnectionParams)
            }
    }

    fun disconnect(disconnectParams: DisconnectParams): Observable<Result> {
        return configurationRepository.getConfiguration()
                .flatMap { configurationResponse ->
                    val url = configurationResponse.services.auth.disconnect.firstVersion.url
                    authApi.disconnect(url, disconnectParams)
                }
    }


    // Registration
    fun confirmRegistration(confirmRegistrationParams: ConfirmRegistrationParams): Observable<Result> {
        return configurationRepository.getConfiguration()
                .flatMap { configurationResponse ->
                    val url = configurationResponse.services.auth.confirmRegistration.firstVersion.url
                    authApi.confirmRegistration(url, confirmRegistrationParams)
                }
    }

    fun register(registrationParams: RegistrationParams): Observable<Result> {
        return configurationRepository.getConfiguration()
                .flatMap { configurationResponse ->
                    val url = configurationResponse.services.auth.login.firstVersion.url
                    authApi.register(url, registrationParams)
                }
    }

    fun requestRegisterCode(requestRegisterCodeParams: RequestRegisterCodeParams): Observable<RequestRegisterCodeResult> {
        return configurationRepository.getConfiguration()
                .flatMap { configurationResponse ->
                    val url = configurationResponse.services.auth.requestRegisterCode.firstVersion.url
                    authApi.requestRegisterCode(url, requestRegisterCodeParams)
                }
    }

    fun acceptRegisterCode(acceptRegisterCodeParams: AcceptRegisterCodeParams): Observable<Result> {
        return configurationRepository.getConfiguration()
                .flatMap { configurationResponse ->
                    val url = configurationResponse.services.auth.acceptRegisterCode.firstVersion.url
                    authApi.acceptRegisterCode(url, acceptRegisterCodeParams)
                }
    }

    // Rules
    fun getAllRules(rulesType: String? = null,
                    context: String? = null): Observable<List<RuleResult>> {
        return configurationRepository.getConfiguration()
                .flatMap { configurationResponse ->
                    val url = configurationResponse.services.auth.getAllRules.firstVersion.url
                    val getRulesParams = GetRulesParams(rulesType, context)
                    authApi.getAllRules(url, getRulesParams)
                }
    }

    fun getRulesStatus(getRulesStatusParams: GetRulesStatusParams): Observable<List<RuleStatusResult>> {
        return configurationRepository.getConfiguration()
                .flatMap { configurationResponse ->
                    val url = configurationResponse.services.auth.getRulesStatus.firstVersion.url
                    authApi.getRulesStatus(url, getRulesStatusParams)
                }
    }

    fun acceptRules(rulesIds: List<Int>): Observable<Result> {
        return acceptRules(authData = null, rulesIds = rulesIds)
    }

    fun acceptRules(authData: JsonRPCParams.AuthData? = null,
                    rulesIds: List<Int>): Observable<Result> {
        return configurationRepository.getConfiguration()
                .flatMap { configurationResponse ->
                    val url = configurationResponse.services.auth.acceptRules.firstVersion.url
                    val acceptRulesParams = AcceptRulesParams(rulesIds)
                    acceptRulesParams.authData = authData
                    authApi.acceptRules(url, acceptRulesParams)
                }
    }

    fun unacceptRules(rulesIds: List<Int>): Observable<Result> {
        return unacceptRules(authData = null, rulesIds = rulesIds)
    }

    fun unacceptRules(authData: JsonRPCParams.AuthData? = null,
                              rulesIds: List<Int>): Observable<Result> {
        return configurationRepository.getConfiguration()
                .flatMap { configurationResponse ->
                    val url = configurationResponse.services.auth.unacceptRules.firstVersion.url
                    val unacceptRulesParams = UnacceptRulesParams(rulesIds)
                    unacceptRulesParams.authData = authData
                    authApi.unacceptRules(url, unacceptRulesParams)
                }
    }

    fun getUnacceptedRules(rulesType: String? = null,
                           context: String? = null): Observable<List<RuleResult>> {
        return getUnacceptedRules(authData = null, rulesType = rulesType, context = context)
    }

    fun getUnacceptedRules(authData: JsonRPCParams.AuthData? = null,
                                   rulesType: String? = null,
                                   context: String? = null): Observable<List<RuleResult>> {
        return configurationRepository.getConfiguration()
                .flatMap { configurationResponse ->
                    val url = configurationResponse.services.auth.getUnacceptedRules.firstVersion.url
                    val getRulesParams = GetRulesParams(rulesType, context)
                    getRulesParams.authData = authData
                    authApi.getUnacceptedRules(url, getRulesParams)
                }
    }

    // Change email
    fun changeEmail(changeEmailParams: ChangeEmailParams): Observable<Result> {
        return configurationRepository.getConfiguration()
                .flatMap { configurationResponse ->
                    val url = configurationResponse.services.auth.changeEmail.secondVersion.url
                    authApi.changeEmail(url, changeEmailParams)
                }
    }

    // ChangePassword
    fun changePassword(changePasswordParams: ChangePasswordParams): Observable<Result> {
        return configurationRepository.getConfiguration()
                .flatMap { configurationResponse ->
                    val url = configurationResponse.services.auth.changePassword.firstVersion.url
                    authApi.changePassword(url, changePasswordParams)
                }
    }

    // Delete user
    fun deleteUser(deleteUserParams: DeleteUserParams): Observable<Result> {
        return configurationRepository.getConfiguration()
                .flatMap { configurationResponse ->
                    val url = configurationResponse.services.auth.deleteUser.firstVersion.url
                    authApi.deleteUser(url, deleteUserParams)
                }
    }

    // Profiles
    fun getProfiles(): Observable<List<ProfileResult>> {
        return configurationRepository.getConfiguration()
                .flatMap { configurationResponse ->
                    val url = configurationResponse.services.auth.getProfiles.firstVersion.url
                    authApi.getProfiles(url, JsonRPCParams())
                }
    }

    fun setSessionProfile(profileId: String): Observable<Result> {
        return configurationRepository.getConfiguration()
                .flatMap { configurationResponse ->
                    val url = configurationResponse.services.auth.setSessionProfile.firstVersion.url
                    authApi.setSessionProfile(url, SetSessionProfileParams(profileId))
                }
    }

    fun updateProfile(updateProfileParams: UpdateProfileParams): Observable<Result> {
        return configurationRepository.getConfiguration()
                .flatMap { configurationResponse ->
                    val url = configurationResponse.services.auth.updateProfile.secondVersion.url
                    authApi.updateProfile(url, updateProfileParams)
                }
    }

    fun createProfile(createProfileParams: CreateProfileParams): Observable<Result> {
        return configurationRepository.getConfiguration()
                .flatMap { configurationResponse ->
                    val url = configurationResponse.services.auth.createProfile.firstVersion.url
                    authApi.createProfile(url, createProfileParams)
                }
    }

    fun deleteProfile(deleteProfileParams: DeleteProfileParams): Observable<Result> {
        return configurationRepository.getConfiguration()
                .flatMap { configurationResponse ->
                    val url = configurationResponse.services.auth.deleteProfile.firstVersion.url
                    authApi.deleteProfile(url, deleteProfileParams)
                }
    }

}