package pl.cyfrowypolsat.cpdata.api.auth

import io.reactivex.rxjava3.core.Observable
import pl.cyfrowypolsat.cpdata.api.auth.request.changeEmail.ChangeEmailParams
import pl.cyfrowypolsat.cpdata.api.auth.request.connect.ConfirmConnectionParams
import pl.cyfrowypolsat.cpdata.api.auth.request.connect.ConnectParams
import pl.cyfrowypolsat.cpdata.api.auth.request.connect.DisconnectParams
import pl.cyfrowypolsat.cpdata.api.auth.request.editaccount.ChangePasswordParams
import pl.cyfrowypolsat.cpdata.api.auth.request.editaccount.DeleteUserParams
import pl.cyfrowypolsat.cpdata.api.auth.request.login.GetLoginRequestStatusParams
import pl.cyfrowypolsat.cpdata.api.auth.request.profiles.CreateProfileParams
import pl.cyfrowypolsat.cpdata.api.auth.request.profiles.DeleteProfileParams
import pl.cyfrowypolsat.cpdata.api.auth.request.profiles.SetSessionProfileParams
import pl.cyfrowypolsat.cpdata.api.auth.request.profiles.UpdateProfileParams
import pl.cyfrowypolsat.cpdata.api.auth.request.registration.AcceptRegisterCodeParams
import pl.cyfrowypolsat.cpdata.api.auth.request.registration.ConfirmRegistrationParams
import pl.cyfrowypolsat.cpdata.api.auth.request.registration.RegistrationParams
import pl.cyfrowypolsat.cpdata.api.auth.request.registration.RequestRegisterCodeParams
import pl.cyfrowypolsat.cpdata.api.auth.request.rules.AcceptRulesParams
import pl.cyfrowypolsat.cpdata.api.auth.request.rules.GetRulesParams
import pl.cyfrowypolsat.cpdata.api.auth.request.rules.GetRulesStatusParams
import pl.cyfrowypolsat.cpdata.api.auth.request.rules.UnacceptRulesParams
import pl.cyfrowypolsat.cpdata.api.auth.request.resetpassword.RequestPasswordResetParams
import pl.cyfrowypolsat.cpdata.api.auth.request.verification.ConfirmVerificationParams
import pl.cyfrowypolsat.cpdata.api.auth.request.verification.VerifyParams
import pl.cyfrowypolsat.cpdata.api.auth.response.ProfileResult
import pl.cyfrowypolsat.cpdata.api.common.model.RuleResult
import pl.cyfrowypolsat.cpdata.api.auth.response.RuleStatusResult
import pl.cyfrowypolsat.cpdata.api.auth.response.login.GetLoginRequestStatusResult
import pl.cyfrowypolsat.cpdata.api.common.jsonrpc.JsonRPC
import pl.cyfrowypolsat.cpdata.api.common.jsonrpc.JsonRPCParams
import pl.cyfrowypolsat.cpdata.api.common.model.AuthResult
import pl.cyfrowypolsat.cpdata.api.auth.response.login.RequestLoginResult
import pl.cyfrowypolsat.cpdata.api.auth.response.registration.RequestRegisterCodeResult
import pl.cyfrowypolsat.cpdata.api.common.model.Result
import pl.cyfrowypolsat.cpdata.api.system.response.AuthServiceConfig
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Url

interface AuthApi {

    // Session
    @POST
    @JsonRPC(AuthServiceConfig.GET_SESSION)
    fun getSession(@Url url: String, @Body jsonRPCParams: JsonRPCParams): Observable<AuthResult>


    // Login
    @POST
    @JsonRPC(AuthServiceConfig.REQUEST_LOGIN)
    fun requestLogin(@Url url: String, @Body jsonRPCParams: JsonRPCParams): Observable<RequestLoginResult>

    @POST
    @JsonRPC(AuthServiceConfig.GET_LOGIN_REQUEST_STATUS)
    fun getLoginRequestStatus(@Url url: String, @Body jsonRPCParams: GetLoginRequestStatusParams): Observable<GetLoginRequestStatusResult>

    @POST
    @JsonRPC(AuthServiceConfig.LOGIN)
    fun login(@Url url: String, @Body jsonRPCParams: JsonRPCParams): Observable<AuthResult>


    // Logout
    @POST
    @JsonRPC(AuthServiceConfig.LOGOUT)
    fun logout(@Url url: String, @Body jsonRPCParams: JsonRPCParams): Observable<Result>

    // Reset password
    @POST
    @JsonRPC(AuthServiceConfig.REQUEST_PASSWORD_RESET)
    fun requestPasswordReset(@Url url: String, @Body requestPasswordResetParams: RequestPasswordResetParams): Observable<Result>


    // Verification
    @POST
    @JsonRPC(AuthServiceConfig.VERIFY)
    fun verify(@Url url: String, @Body verifyParams: VerifyParams): Observable<Result>

    @POST
    @JsonRPC(AuthServiceConfig.CONFIRM_VERIFICATION)
    fun confirmVerification(@Url url: String, @Body confirmVerificationParams: ConfirmVerificationParams): Observable<Result>


    // Account connection
    @POST
    @JsonRPC(AuthServiceConfig.CONNECT)
    fun connect(@Url url: String, @Body connectParams: ConnectParams): Observable<Result>

    @POST
    @JsonRPC(AuthServiceConfig.CONFIRM_CONNECTION)
    fun confirmConnection(@Url url: String, @Body confirmConnectionParams: ConfirmConnectionParams): Observable<Result>

    @POST
    @JsonRPC(AuthServiceConfig.DISCONNECT)
    fun disconnect(@Url url: String, @Body disconnectParams: DisconnectParams): Observable<Result>


    // Registration
    @POST
    @JsonRPC(AuthServiceConfig.REGISTER)
    fun register(@Url url: String, @Body registrationParams: RegistrationParams): Observable<Result>

    @POST
    @JsonRPC(AuthServiceConfig.CONFIRM_REGISTRATION)
    fun confirmRegistration(@Url url: String, @Body confirmRegistrationParams: ConfirmRegistrationParams): Observable<Result>

    @POST
    @JsonRPC(AuthServiceConfig.REQUEST_REGISTER_CODE)
    fun requestRegisterCode(@Url url: String, @Body requestRegisterCodeParams: RequestRegisterCodeParams): Observable<RequestRegisterCodeResult>

    @POST
    @JsonRPC(AuthServiceConfig.ACCEPT_REGISTER_CODE)
    fun acceptRegisterCode(@Url url: String, @Body acceptRegisterCodeParams: AcceptRegisterCodeParams): Observable<Result>


    // Rules
    @POST
    @JsonRPC(AuthServiceConfig.GET_ALL_RULES)
    fun getAllRules(@Url url: String, @Body getRulesParams: GetRulesParams): Observable<List<RuleResult>>

    @POST
    @JsonRPC(AuthServiceConfig.GET_RULES_STATUS)
    fun getRulesStatus(@Url url: String, @Body getRulesStatusParams: GetRulesStatusParams): Observable<List<RuleStatusResult>>

    @POST
    @JsonRPC(AuthServiceConfig.ACCEPT_RULES)
    fun acceptRules(@Url url: String, @Body acceptRulesParams: AcceptRulesParams): Observable<Result>

    @POST
    @JsonRPC(AuthServiceConfig.UNACCEPT_RULES)
    fun unacceptRules(@Url url: String, @Body unacceptRulesParams: UnacceptRulesParams): Observable<Result>

    @POST
    @JsonRPC(AuthServiceConfig.GET_UNACCEPTED_RULES)
    fun getUnacceptedRules(@Url url: String, @Body getRulesParams: GetRulesParams): Observable<List<RuleResult>>

    // Change email
    @POST
    @JsonRPC(AuthServiceConfig.CHANGE_EMAIL)
    fun changeEmail(@Url url: String, @Body changeEmailParams: ChangeEmailParams): Observable<Result>

    // Change password
    @POST
    @JsonRPC(AuthServiceConfig.CHANGE_PASSWORD)
    fun changePassword(@Url url: String, @Body changePasswordParams: ChangePasswordParams): Observable<Result>

    // Delete user
    @POST
    @JsonRPC(AuthServiceConfig.DELETE_USER)
    fun deleteUser(@Url url: String, @Body deleteUserParams: DeleteUserParams): Observable<Result>

    // Profiles
    @POST
    @JsonRPC(AuthServiceConfig.GET_PROFILES)
    fun getProfiles(@Url url: String, @Body jsonRPCParams: JsonRPCParams): Observable<List<ProfileResult>>

    @POST
    @JsonRPC(AuthServiceConfig.SET_SESSION_PROFILE)
    fun setSessionProfile(@Url url: String, @Body setSessionProfileParams: SetSessionProfileParams): Observable<Result>

    @POST
    @JsonRPC(AuthServiceConfig.UPDATE_PROFILE)
    fun updateProfile(@Url url: String, @Body updateProfileParams: UpdateProfileParams): Observable<Result>

    @POST
    @JsonRPC(AuthServiceConfig.CREATE_PROFILE)
    fun createProfile(@Url url: String, @Body createProfileParams: CreateProfileParams): Observable<Result>

    @POST
    @JsonRPC(AuthServiceConfig.DELETE_PROFILE)
    fun deleteProfile(@Url url: String, @Body deleteProfileParams: DeleteProfileParams): Observable<Result>

}
