package pl.cyfrowypolsat.cpdata.api.auth.request.rules

import pl.cyfrowypolsat.cpdata.api.common.jsonrpc.JsonRPCParams

data class GetRulesParams(val rulesType: String?, val context: String?) : JsonRPCParams()