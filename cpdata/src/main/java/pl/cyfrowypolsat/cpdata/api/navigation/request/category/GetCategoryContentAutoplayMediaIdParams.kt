package pl.cyfrowypolsat.cpdata.api.navigation.request.category

import pl.cyfrowypolsat.cpdata.api.common.jsonrpc.JsonRPCParams

data class GetCategoryContentAutoplayMediaIdParams(val catid: Int) : JsonRPCParams()