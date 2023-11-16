package pl.cyfrowypolsat.cpdata.api.navigation.request.channelprogram

import pl.cyfrowypolsat.cpdata.api.common.jsonrpc.JsonRPCParams

data class GetTvChannelProgramItemParams(val tvChannelProgramItemId: String) : JsonRPCParams()