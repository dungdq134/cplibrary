package pl.cyfrowypolsat.cpdata.api.common.jsonrpc


class JsonRPCException(userMessage: String?,
                       val backendError: BackendError,
                       val type: String) : RuntimeException(userMessage) {

    companion object {

        fun create(jsonRPCError: JsonRPCError,
                   methodName: String,
                   serviceUrl: String): JsonRPCException {

            val existingAccounts = jsonRPCError.data?.existingAccounts ?: listOf()
            val email = jsonRPCError.data?.email ?: ""
            val type = jsonRPCError.data?.type ?: ""
            val rulesType = jsonRPCError.data?.rulesType ?: ""

            val backendError = BackendError(errorCode = jsonRPCError.code.toInt(),
                    message = jsonRPCError.data?.message ?: jsonRPCError.message,
                    messageId = jsonRPCError.data?.messageId ?: "",
                    methodName = methodName,
                    serviceUrl = serviceUrl,
                    type = type,
                    rulesType = rulesType,
                    existingAccounts = existingAccounts,
                    email = email)

            return JsonRPCException(jsonRPCError.data?.userMessage, backendError, type)
        }

    }
}
