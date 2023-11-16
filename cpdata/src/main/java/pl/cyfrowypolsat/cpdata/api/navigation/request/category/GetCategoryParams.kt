package pl.cyfrowypolsat.cpdata.api.navigation.request.category

import pl.cyfrowypolsat.cpdata.api.common.jsonrpc.JsonRPCParams
import pl.cyfrowypolsat.cpdata.api.navigation.request.common.InputCollection
import pl.cyfrowypolsat.cpdata.api.navigation.request.common.InputFilter

data class GetCategoryParams(val catid: Int) : JsonRPCParams()