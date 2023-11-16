package pl.cyfrowypolsat.cpdata.api.navigation.request.packet

import pl.cyfrowypolsat.cpdata.api.common.jsonrpc.JsonRPCParams

data class GetPacketTreeNavigationParams(val packetCode: String) : JsonRPCParams()