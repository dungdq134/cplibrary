package pl.cyfrowypolsat.cpdata.api.navigation.request.category

import pl.cyfrowypolsat.cpdata.api.common.jsonrpc.JsonRPCParams

data class GetSubCategoriesWithBasicNavigationParams(val catid: Int) : JsonRPCParams()