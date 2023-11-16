package pl.cyfrowypolsat.cpdata.api.auth.request.login

import pl.cyfrowypolsat.cpdata.api.auth.request.common.Captcha
import pl.cyfrowypolsat.cpdata.api.common.jsonrpc.JsonRPCParams

class LoginParams(authData: LoginAuthData,
                  val captcha: Captcha?,
                  val flowContext: String? = null) : JsonRPCParams(authData)