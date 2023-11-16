package pl.cyfrowypolsat.cpdata.api.auth.response.registration

class RequestRegisterCodeResult(val statusDescription: String,
                                val status: Int,
                                val statusUserMessage: String?,
                                val registerCodeId: String)