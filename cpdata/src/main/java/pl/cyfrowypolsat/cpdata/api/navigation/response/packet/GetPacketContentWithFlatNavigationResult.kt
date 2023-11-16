package pl.cyfrowypolsat.cpdata.api.navigation.response.packet

import pl.cyfrowypolsat.cpdata.api.common.model.cpidobject.CpidObject
import pl.cyfrowypolsat.cpdata.api.navigation.response.common.FlatNavigationResult

data class GetPacketContentWithFlatNavigationResult(val offset: Int,
                                                    val count: Int,
                                                    val results: List<CpidObject>,
                                                    val flatNavigation: FlatNavigationResult?)