package pl.cyfrowypolsat.cpdata.api.common.jsonrpc

data class JsonRPCRequest(val jsonrpc: String,
                          val id: Long,
                          val method: String,
                          val params: Any)