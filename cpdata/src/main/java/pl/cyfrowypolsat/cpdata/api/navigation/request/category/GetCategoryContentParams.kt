package pl.cyfrowypolsat.cpdata.api.navigation.request.category

import pl.cyfrowypolsat.cpdata.api.common.jsonrpc.JsonRPCParams
import pl.cyfrowypolsat.cpdata.api.navigation.request.common.InputCollection
import pl.cyfrowypolsat.cpdata.api.navigation.request.common.InputFilter

data class GetCategoryContentParams(val catid: Int,
                                    val filters: List<InputFilter>? = null,
                                    val collection: InputCollection? = null,
                                    val offset: Int,
                                    val limit: Int = 30) : JsonRPCParams()