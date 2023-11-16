package pl.cyfrowypolsat.cpdata.api.auth.request.editaccount

import pl.cyfrowypolsat.cpdata.api.common.jsonrpc.JsonRPCParams

data class DeleteUserParams(val password: String) : JsonRPCParams()