package pl.cyfrowypolsat.cpdata.api.auth.request.registration

import pl.cyfrowypolsat.cpdata.api.auth.request.common.Captcha
import pl.cyfrowypolsat.cpdata.api.common.jsonrpc.JsonRPCParams

data class RequestRegisterCodeParams(val email: String,
                                     val captcha: Captcha) : JsonRPCParams()

