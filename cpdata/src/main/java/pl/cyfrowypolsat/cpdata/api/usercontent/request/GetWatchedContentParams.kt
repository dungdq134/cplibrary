package pl.cyfrowypolsat.cpdata.api.usercontent.request

import pl.cyfrowypolsat.cpdata.api.common.jsonrpc.JsonRPCParams

data class GetWatchedContentParams(private val cpid: Int,
                                   private val mediaId: String): JsonRPCParams()