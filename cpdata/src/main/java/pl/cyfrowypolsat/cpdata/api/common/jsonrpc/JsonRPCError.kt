package pl.cyfrowypolsat.cpdata.api.common.jsonrpc

data class JsonRPCError(val code: Long,
                        val message: String,
                        val data: ErrorData?) {

    companion object {
        const val RPC_ERROR_UNAUTHORIZED_ACCESS: Long = 13401
        const val RPC_ERROR_NOT_FOUND = 13404
        const val RPC_ERROR_AGREEMENT_NOT_ACCEPTED: Long = 13431
        const val RPC_ERROR_USER_EXISTS: Long = 13450
        const val RPC_ERROR_EMAIL_REQUIRED: Long = 13456
        const val RPC_ERROR_CAPTCHA_REQUIRED: Long = 13458
        const val RPC_ERROR_WRONG_CAPTCHA_VALUE: Long = 13452

        const val RPC_ERROR_CONFIRM_REGISTRATION_EMAIL_INVALID_CODE: Long = 12108
        const val RPC_ERROR_CONFIRM_REGISTRATION_EMAIL_USED_CODE: Long = 12112
        const val RPC_ERROR_CONFIRM_REGISTRATION_EMAIL_EXPIRED_CODE: Long = 12110

        const val RPC_ERROR_CONCURRENCY_ACCESS_CONTROL_LIMIT: Long = 13442
        const val RPC_ERROR_CONFIRM_REGISTRATION_EMAIL_REQUIRED: Long = 13454

        const val RPC_ERROR_DEVICE_LIMIT_EXCEEDED: Long = 13441

        const val RPC_ERROR_EXCEPTION_TYPE_RULES_EXCEPTION: String = "RulesException"
        const val RPC_ERROR_EXCEPTION_RULES_TYPE_RODO: String = "rodo"
    }
}

data class ErrorData(val code: Long,
                     val message: String,
                     val messageId: String,
                     val userMessage: String?,
                     val type: String?,
                     val rulesType: String?,
                     val existingAccounts: List<String>?,
                     val email: String?)