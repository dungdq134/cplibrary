package pl.cyfrowypolsat.cpdata.api.navigation.request.favorites

import pl.cyfrowypolsat.cpdata.api.common.jsonrpc.JsonRPCParams
import pl.cyfrowypolsat.cpdata.api.navigation.request.common.InputCollection
import pl.cyfrowypolsat.cpdata.api.navigation.request.common.InputFilter

data class GetFavoritesTreeNavigationParams(val cpids: List<Int>? = null) : JsonRPCParams()