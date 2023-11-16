package pl.cyfrowypolsat.cpdata.api.auth.request.connect

import pl.cyfrowypolsat.cpdata.api.common.jsonrpc.JsonRPCParams

data class ConfirmConnectionParams(val verificationCode: String) : JsonRPCParams()