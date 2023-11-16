package pl.cyfrowypolsat.cpdata.api.auth.request.profiles

import pl.cyfrowypolsat.cpdata.api.common.jsonrpc.JsonRPCParams

data class DeleteProfileParams(val profileId: String) : JsonRPCParams()