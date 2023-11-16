package pl.cyfrowypolsat.cpdata.api.navigation.request.live

import pl.cyfrowypolsat.cpdata.api.common.jsonrpc.JsonRPCParams
import pl.cyfrowypolsat.cpdata.api.navigation.request.common.InputCollection
import pl.cyfrowypolsat.cpdata.api.navigation.request.common.InputFilter

data class GetLiveChannelsWithTreeNavigationParams(val profileId: String? = null,
                                                   val offset: Int,
                                                   val limit: Int,
                                                   val filters: List<InputFilter>? = null,
                                                   val collection: InputCollection? = null): JsonRPCParams()