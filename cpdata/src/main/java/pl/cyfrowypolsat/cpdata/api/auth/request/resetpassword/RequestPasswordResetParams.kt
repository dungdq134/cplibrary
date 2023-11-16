package pl.cyfrowypolsat.cpdata.api.auth.request.resetpassword

import pl.cyfrowypolsat.cpdata.api.common.jsonrpc.JsonRPCParams

data class RequestPasswordResetParams(val email: String) : JsonRPCParams()