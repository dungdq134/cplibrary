package pl.cyfrowypolsat.cpstats.core.model

data class AccountData(
        val userType: UserType,
        val login: String?,
        val msisdn: String?,
        val facebookId: String?,
        val ssoExternalAccountId: String?
)