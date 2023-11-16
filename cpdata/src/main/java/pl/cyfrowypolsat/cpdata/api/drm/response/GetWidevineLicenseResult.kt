package pl.cyfrowypolsat.cpdata.api.drm.response

import pl.cyfrowypolsat.cpdata.api.navigation.response.preplaydata.VersionedService

data class GetWidevineLicenseResult(val id: String?,
                                    val `object`: LicenseObject,
                                    val reporting: Reporting?,
                                    val accessPolicy: AccessPolicy?)

data class LicenseObject(val license: String)

data class AccessPolicy(val cac: Cac?)

data class Cac(val service: String,
               val authToken: String,
               val authTokenService: VersionedService)

