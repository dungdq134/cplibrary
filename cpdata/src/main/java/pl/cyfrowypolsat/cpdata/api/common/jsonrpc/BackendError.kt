package pl.cyfrowypolsat.cpdata.api.common.jsonrpc

data class BackendError(val errorCode: Int,
                        val serviceUrl: String = "",
                        val methodName: String = "",
                        val message: String = "",
                        val messageId: String = "",
                        val type: String = "",
                        val rulesType: String = "",
                        val existingAccounts: List<String> = listOf(),
                        val email: String = "")