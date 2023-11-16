package pl.cyfrowypolsat.cpdata.api.navigation.request.preplaydata

import pl.cyfrowypolsat.cpdata.api.common.jsonrpc.JsonRPCParams

data class PrePlayDataParams(val mediaId: String,
                             val cpid: Int) : JsonRPCParams()
