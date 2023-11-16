package pl.cyfrowypolsat.cpdata.api.navigation.request.tvchannel

import pl.cyfrowypolsat.cpdata.api.common.jsonrpc.JsonRPCParams
import pl.cyfrowypolsat.cpdata.api.navigation.request.common.InputCollection
import pl.cyfrowypolsat.cpdata.api.navigation.request.common.InputFilter

data class GetTvChannelsParams(
        val filters: List<InputFilter>? = null,
        val collection: InputCollection? = null,
        val offset: Int = 0,
        val limit: Int = 200
) : JsonRPCParams()