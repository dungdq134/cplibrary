package pl.cyfrowypolsat.cpdata.common.manager

import pl.cyfrowypolsat.cpdata.CpData
import pl.cyfrowypolsat.cpdata.api.auth.request.login.LoginAuthData
import pl.cyfrowypolsat.cpdata.api.auth.response.ProfileResult
import pl.cyfrowypolsat.cpdata.api.common.model.AuthResult
import pl.cyfrowypolsat.cpdata.api.common.model.Session
import pl.cyfrowypolsat.cpdata.local.SharedPrefs
import pl.cyfrowypolsat.cpdata.local.billing.BillingOrder
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountManager
@Inject constructor() {

    @Inject
    lateinit var sharedPrefs: SharedPrefs

    init {
        CpData.getInstance().component.inject(this)
    }


    fun isUserLogged(): Boolean {
        return sharedPrefs.authResult != null
    }

    fun isConnectedWithPlus(): Boolean {
        val msisdn = getConnectedMsisdn()
        return msisdn != null && msisdn.isNotBlank()
    }

    fun isPhoneNumberVerified(): Boolean {
        return sharedPrefs.authResult?.accessGroups?.contains("verified:true") ?: return false
    }

    fun isPasswordGenerated(): Boolean {
        return sharedPrefs.authResult?.user?.accounts?.native?.passwordGenerated ?: false
    }

    fun getAccountName(): String {
        val authResult = sharedPrefs.authResult ?: return ""
        val selectedProfileName = getSelectedProfile()?.name
        return if (selectedProfileName?.isNotBlank() == true) {
            selectedProfileName
        } else if (!authResult.user.nick.isNullOrEmpty()) {
            authResult.user.nick
        } else if (!authResult.user.email.isNullOrEmpty()) {
            authResult.user.email
        } else {
            ""
        }
    }

    fun getLoginAuthData(): LoginAuthData? {
        return sharedPrefs.loginAuthData
    }

    fun getEmail(): String? {
        return sharedPrefs.authResult?.user?.email
    }

    fun getConnectedMsisdn(): String? {
        return sharedPrefs.authResult?.user?.accounts?.plus?.msisdn
    }

    fun getSession(): Session? {
        return sharedPrefs.authResult?.session
    }

    fun getUserId(): Int? {
        return sharedPrefs.authResult?.user?.id
    }

    fun getFacebookId(): String? {
        return sharedPrefs.authResult?.user?.accounts?.facebook?.fbId
    }

    fun getPolsatId(): String? {
        return sharedPrefs.authResult?.user?.accounts?.icok?.userIdCP
    }

    fun getSsoExternalAccountId(): String? {
        return sharedPrefs.authResult?.user?.accounts?.sso?.externalAccountId
    }


    fun getSelectedProfileId(): String? {
        return sharedPrefs.selectedProfileId
    }

    fun getSelectedProfileAvatarId(): String? {
        return getSelectedProfile()?.avatarId
    }

    private fun getSelectedProfile(): ProfileResult? {
        return sharedPrefs.profilesResult?.firstOrNull { it.id == sharedPrefs.selectedProfileId }
    }

    fun getLastProfileSelectTime(): Long {
        return sharedPrefs.lastProfileSelectTime
    }

    fun isSingleProfile(): Boolean {
        return sharedPrefs.profilesResult?.let { it.size <= 1 } ?: true
    }

    fun hasNativeAccount(): Boolean {
        return sharedPrefs.authResult?.user?.accounts?.native != null
    }

    fun getBillingOrder(): BillingOrder? {
        return sharedPrefs.billingOrder
    }

    fun getChipId(): String? {
        return sharedPrefs.chipId
    }


    // Save
    fun saveAuthResult(authResult: AuthResult) {
        sharedPrefs.authResult = authResult
    }

    fun saveLoginAuthData(loginAuthData: LoginAuthData) {
        sharedPrefs.loginAuthData = loginAuthData
    }

    fun updatePassword(password: String) {
        val loginAuthData = sharedPrefs.loginAuthData ?: return
        sharedPrefs.loginAuthData = loginAuthData.copy(password = password)
    }

    fun saveProfiles(profiles: List<ProfileResult>) {
        sharedPrefs.profilesResult = profiles
    }

    fun saveSelectedProfileId(profileId: String) {
        sharedPrefs.selectedProfileId = profileId
    }

    fun saveLastProfileSelectTime() {
        sharedPrefs.lastProfileSelectTime = Date().time
    }

    fun saveBillingOrder(order: BillingOrder) {
        sharedPrefs.billingOrder = order
    }

    fun saveChipId(chipId: String) {
        sharedPrefs.chipId = chipId
    }


    // Clear
    fun clearAccountData() {
        sharedPrefs.authResult = null
        sharedPrefs.loginAuthData = null
        sharedPrefs.profilesResult = null
        sharedPrefs.selectedProfileId = null
        sharedPrefs.lastProfileSelectTime = -1
        clearBillingOrder()
    }

    fun clearSelectedProfileId() {
        sharedPrefs.selectedProfileId = null
    }

    fun clearBillingOrder() {
        sharedPrefs.billingOrder = null
    }

}