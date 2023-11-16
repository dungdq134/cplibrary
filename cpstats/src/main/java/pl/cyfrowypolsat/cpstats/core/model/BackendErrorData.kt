package pl.cyfrowypolsat.cpstats.core.model

data class BackendErrorData(val type: String,
                            val serviceUrl: String,
                            val methodName: String)