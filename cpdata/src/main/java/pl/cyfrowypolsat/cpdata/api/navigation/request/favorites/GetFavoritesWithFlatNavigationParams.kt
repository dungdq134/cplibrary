package pl.cyfrowypolsat.cpdata.api.navigation.request.favorites

import pl.cyfrowypolsat.cpdata.api.common.jsonrpc.JsonRPCParams
import pl.cyfrowypolsat.cpdata.api.navigation.request.common.InputCollection
import pl.cyfrowypolsat.cpdata.api.navigation.request.common.InputFilter

data class GetFavoritesWithFlatNavigationParams(val offset: Int,
                                                val limit: Int? = 20,
                                                val filters: List<InputFilter>? = null,
                                                val collection: InputCollection? = null,
                                                val cpids: List<Int>? = null) : JsonRPCParams()