package pl.cyfrowypolsat.cpdata.api.auth.response.login

data class RequestLoginResult(val status: Int,
                              val statusDescription: String,
                              val loginRequestId: String,
                              val acceptanceData: AcceptanceDataResult)

data class AcceptanceDataResult(val code: String,
                                val url: String,
                                val acceptanceUrl: String)