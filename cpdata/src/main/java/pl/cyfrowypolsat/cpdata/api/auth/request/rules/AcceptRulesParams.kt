package pl.cyfrowypolsat.cpdata.api.auth.request.rules

import pl.cyfrowypolsat.cpdata.api.common.jsonrpc.JsonRPCParams

data class AcceptRulesParams(val rulesIds: List<Int>) : JsonRPCParams()