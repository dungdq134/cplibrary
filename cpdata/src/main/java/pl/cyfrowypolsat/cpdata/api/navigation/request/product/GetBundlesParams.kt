package pl.cyfrowypolsat.cpdata.api.navigation.request.product

import pl.cyfrowypolsat.cpdata.api.common.jsonrpc.JsonRPCParams

data class GetBundlesParams(val bundleIds: List<String>) : JsonRPCParams()