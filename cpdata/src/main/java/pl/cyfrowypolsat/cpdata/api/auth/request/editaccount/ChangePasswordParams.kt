package pl.cyfrowypolsat.cpdata.api.auth.request.editaccount

import pl.cyfrowypolsat.cpdata.api.common.jsonrpc.JsonRPCParams

data class ChangePasswordParams(val oldPassword: String, val newPassword: String) : JsonRPCParams()