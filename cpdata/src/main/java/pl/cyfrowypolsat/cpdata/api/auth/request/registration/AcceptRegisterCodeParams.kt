package pl.cyfrowypolsat.cpdata.api.auth.request.registration

import pl.cyfrowypolsat.cpdata.api.common.jsonrpc.JsonRPCParams

data class AcceptRegisterCodeParams(val email: String,
                                    val code: String) : JsonRPCParams()

