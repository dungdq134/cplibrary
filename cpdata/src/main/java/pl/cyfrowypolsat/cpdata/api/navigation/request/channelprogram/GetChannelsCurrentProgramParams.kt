package pl.cyfrowypolsat.cpdata.api.navigation.request.channelprogram

import pl.cyfrowypolsat.cpdata.api.common.jsonrpc.JsonRPCParams
import java.util.*

data class GetChannelsCurrentProgramParams(val channelIds: List<String>,
                                           val limit: Int) : JsonRPCParams()