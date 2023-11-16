package pl.cyfrowypolsat.cpdata.api.common.model

data class Result(val statusDescription: String,
                  val status: Int,
                  val statusUserMessage: String?,
                  val reason: Reason?) {

    companion object {
        const val SUCCESS_STATUS = 0
    }

}

data class Reason(val type: String)