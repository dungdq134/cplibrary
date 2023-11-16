package pl.cyfrowypolsat.cpdata.api.navigation.request.live

import pl.cyfrowypolsat.cpdata.api.common.jsonrpc.JsonRPCParams
import pl.cyfrowypolsat.cpdata.api.navigation.request.common.InputCollection
import pl.cyfrowypolsat.cpdata.api.navigation.request.common.SingleValueInputFilter

class GetLiveChannelsParams (
        val filters: List<SingleValueInputFilter>? = null,
        val offset: Int,
        val limit: Int,
        val collection: InputCollection? = null
) : JsonRPCParams()

