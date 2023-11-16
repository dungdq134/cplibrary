package pl.cyfrowypolsat.cpdata.api.drm.request

import pl.cyfrowypolsat.cpdata.api.common.jsonrpc.JsonRPCParams

data class GetUserPlaybacksParams(val cpid: Int,
                                  val mediaId: String) : JsonRPCParams()