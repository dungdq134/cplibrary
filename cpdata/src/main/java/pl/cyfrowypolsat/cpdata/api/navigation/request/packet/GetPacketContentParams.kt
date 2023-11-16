package pl.cyfrowypolsat.cpdata.api.navigation.request.packet

import pl.cyfrowypolsat.cpdata.api.common.jsonrpc.JsonRPCParams
import pl.cyfrowypolsat.cpdata.api.navigation.request.common.InputFilter

data class GetPacketContentParams(val packetCode: String,
                                  val filters: List<InputFilter>? = null,
                                  val offset: Int,
                                  val limit: Int = 30) : JsonRPCParams()
