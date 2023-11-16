package pl.cyfrowypolsat.cpdata.api.auth.response.login

import pl.cyfrowypolsat.cpdata.api.common.model.AuthProvider

data class GetLoginRequestStatusResult(val status: Int,
                                       val statusDescription: String,
                                       val authData: AuthDataResult) {
    companion object {
        const val SUCCESS_STATUS = 0
    }
}

data class AuthDataResult(val authProvider: AuthProvider,
                          val authToken: String)

