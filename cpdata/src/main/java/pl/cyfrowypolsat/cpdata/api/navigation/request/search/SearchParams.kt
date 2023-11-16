package pl.cyfrowypolsat.cpdata.api.navigation.request.search

import pl.cyfrowypolsat.cpdata.api.common.jsonrpc.JsonRPCParams

data class SearchParams(val query: String,
                        val offset: Int,
                        val limit: Int) : JsonRPCParams()