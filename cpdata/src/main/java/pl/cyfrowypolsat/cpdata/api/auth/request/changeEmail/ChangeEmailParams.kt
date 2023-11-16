package pl.cyfrowypolsat.cpdata.api.auth.request.changeEmail

import pl.cyfrowypolsat.cpdata.api.auth.request.common.Captcha
import pl.cyfrowypolsat.cpdata.api.common.jsonrpc.JsonRPCParams

data class ChangeEmailParams(val email: String,
                             val captcha: Captcha) : JsonRPCParams()