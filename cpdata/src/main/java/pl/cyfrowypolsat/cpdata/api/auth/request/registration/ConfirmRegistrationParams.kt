package pl.cyfrowypolsat.cpdata.api.auth.request.registration

import pl.cyfrowypolsat.cpdata.api.common.jsonrpc.JsonRPCParams

data class ConfirmRegistrationParams(val verificationCode: String) : JsonRPCParams()