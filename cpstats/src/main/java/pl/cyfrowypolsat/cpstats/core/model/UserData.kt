package pl.cyfrowypolsat.cpstats.core.model

data class UserData(
        val clientId: String,
        val userType: UserType,
        val userId: Int?,
        val login: String?,
        val msisdn: String?,
        val facebookId: String?,
        val profileId: String?,
        val isUserLogged: Boolean,
        val ssoExternalAccountId: String?
)