package pl.cyfrowypolsat.cpdata.api.auth.request.verification

import pl.cyfrowypolsat.cpdata.api.common.jsonrpc.JsonRPCParams

data class VerifyParams(val msisdn: String) : JsonRPCParams()