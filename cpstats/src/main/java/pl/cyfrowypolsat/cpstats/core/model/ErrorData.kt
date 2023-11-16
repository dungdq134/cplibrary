package pl.cyfrowypolsat.cpstats.core.model

data class ErrorData(val errorCode: String,
                     val throwable: Throwable,
                     val backendErrorData: BackendErrorData? = null) {
    companion object {
        const val UNKNOWN_ERROR_CODE = "-1"
    }
}