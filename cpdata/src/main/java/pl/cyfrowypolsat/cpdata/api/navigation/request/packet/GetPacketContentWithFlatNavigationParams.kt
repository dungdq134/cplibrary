package pl.cyfrowypolsat.cpdata.api.navigation.request.packet

import pl.cyfrowypolsat.cpdata.api.common.jsonrpc.JsonRPCParams
import pl.cyfrowypolsat.cpdata.api.navigation.request.common.InputCollection
import pl.cyfrowypolsat.cpdata.api.navigation.request.common.InputFilter

data class GetPacketContentWithFlatNavigationParams(val packetCode: String,
                                                    val offset: Int,
                                                    val limit: Int = 20,
                                                    val filters: List<InputFilter>? = null,
                                                    val collection: InputCollection? = null) : JsonRPCParams()