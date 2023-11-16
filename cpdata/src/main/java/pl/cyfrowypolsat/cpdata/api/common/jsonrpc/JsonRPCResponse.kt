package pl.cyfrowypolsat.cpdata.api.common.jsonrpc

data class JsonRPCResponse<T>(val id: Long,
                              val result: T?,
                              val error: JsonRPCError?)