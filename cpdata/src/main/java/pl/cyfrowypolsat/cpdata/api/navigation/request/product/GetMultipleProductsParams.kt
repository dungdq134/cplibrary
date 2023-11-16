package pl.cyfrowypolsat.cpdata.api.navigation.request.product

import pl.cyfrowypolsat.cpdata.api.common.jsonrpc.JsonRPCParams

data class GetMultipleProductsParams(val subType: String) : JsonRPCParams()