package pl.cyfrowypolsat.cpdata.api.navigation.request.media

import pl.cyfrowypolsat.cpdata.api.common.jsonrpc.JsonRPCParams

data class GetMediaParams(val mediaId: String,
                          val cpid: Int) : JsonRPCParams()