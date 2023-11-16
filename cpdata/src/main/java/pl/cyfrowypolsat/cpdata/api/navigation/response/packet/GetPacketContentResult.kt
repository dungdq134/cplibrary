package pl.cyfrowypolsat.cpdata.api.navigation.response.packet

import pl.cyfrowypolsat.cpdata.api.common.model.cpidobject.CpidObject

data class GetPacketContentResult(val offset: Int,
                                  val count: Int,
                                  val results: List<CpidObject>)