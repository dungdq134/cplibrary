package pl.cyfrowypolsat.cpdata.api.drm.request

import pl.cyfrowypolsat.cpdata.api.common.jsonrpc.JsonRPCParams
import pl.cyfrowypolsat.cpdata.api.common.model.DeviceId

data class GetWidevineLicenseParams(val cpid: Int,
                                    val mediaId: String,
                                    val sourceId: String,
                                    val keyId: String,
                                    val deviceId: DeviceId,
                                    var `object`: String,
                                    val offline: Boolean = false) : JsonRPCParams()
