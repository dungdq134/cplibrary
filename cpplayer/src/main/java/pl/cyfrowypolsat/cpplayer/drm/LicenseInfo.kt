package pl.cyfrowypolsat.cpplayer.drm

data class LicenseInfo(val licenseId: String?,
                       val sellModel: String?,
                       val cacInfo: CacInfo?)

data class CacInfo(val serviceUrl: String,
                   val authToken: String,
                   val authTokenServiceUrl: String)