package pl.cyfrowypolsat.cpdata.api.navigation.request.media

import pl.cyfrowypolsat.cpdata.api.common.jsonrpc.JsonRPCParams
import pl.cyfrowypolsat.cpdata.api.usercontent.response.MediaId

data class GetMediaListParams(val mediaIds: List<MediaId>) : JsonRPCParams()