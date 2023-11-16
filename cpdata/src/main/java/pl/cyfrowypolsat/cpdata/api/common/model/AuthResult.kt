package pl.cyfrowypolsat.cpdata.api.common.model

data class AuthResult(val status: Int,
                      val session: Session,
                      val user: User,
                      val profileId: String?,
                      val accessGroups: List<String>?)

data class Session(val id: String,
                   val key: String,
                   val keyExpirationTime: Int)

data class User(val id: Int,
                val accounts: Accounts?,
                val email: String?,
                val nick: String?)

data class Accounts(val native: NativeAccount?,
                    val plus: PlusAccount?,
                    val facebook: FacebookAccount?,
                    val icok: IcokAccount?,
                    val sso: SsoAccount?)

data class NativeAccount(val login: String,
                         val passwordGenerated: Boolean?)

data class PlusAccount(val msisdn: String)

data class FacebookAccount(val fbId: String)

data class IcokAccount(val userIdCP: String)

data class SsoAccount(val externalAccountId: String)