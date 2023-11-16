package pl.cyfrowypolsat.cpdata.api.auth.request.login

import pl.cyfrowypolsat.cpdata.api.common.jsonrpc.JsonRPCParams

data class GetLoginRequestStatusParams(val loginRequestId: String) : JsonRPCParams()