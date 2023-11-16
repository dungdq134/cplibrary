package pl.cyfrowypolsat.cpchromecast.domain.model.auth

data class ChromecastUserAuthData(val isUserLogged: Boolean,
                                  val userId: String?,
                                  val sessionId: String?,
                                  val sessionKey: String?,
                                  val sessionKeyExpirationTime: Int,
                                  val deviceId: String?,
                                  val clientId: String?,
                                  val profileId: String?)