package pl.cyfrowypolsat.cpdata.api.auth.request.profiles

import pl.cyfrowypolsat.cpdata.api.common.jsonrpc.JsonRPCParams

data class CreateProfileParams(val name: String,
                               val avatarId: String) : JsonRPCParams()