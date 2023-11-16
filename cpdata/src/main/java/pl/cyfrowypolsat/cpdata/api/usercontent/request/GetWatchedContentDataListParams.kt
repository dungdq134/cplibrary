package pl.cyfrowypolsat.cpdata.api.usercontent.request

import pl.cyfrowypolsat.cpdata.api.common.jsonrpc.JsonRPCParams
import pl.cyfrowypolsat.cpdata.api.usercontent.response.MediaId

data class GetWatchedContentDataListParams(val mediaIds: List<MediaId>): JsonRPCParams()