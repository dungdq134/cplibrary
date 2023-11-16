package pl.cyfrowypolsat.cpdata.api.drm.request

import pl.cyfrowypolsat.cpdata.api.common.jsonrpc.JsonRPCParams
import pl.cyfrowypolsat.cpdata.api.common.model.DeviceId

data class GetPseudoLicenseParams(val cpid: Int,
                                  val mediaId: String,
                                  val deviceId: DeviceId,
                                  val sourceId: String,
                                  val offline: Boolean = false) : JsonRPCParams()
