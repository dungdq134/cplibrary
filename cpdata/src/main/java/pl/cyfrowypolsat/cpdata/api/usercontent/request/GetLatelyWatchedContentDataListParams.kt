package pl.cyfrowypolsat.cpdata.api.usercontent.request

import pl.cyfrowypolsat.cpdata.api.common.jsonrpc.JsonRPCParams

data class GetLatelyWatchedContentDataListParams(private val catid: Int,
                                                 private val limit: Int? = null): JsonRPCParams()