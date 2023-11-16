package pl.cyfrowypolsat.cpdata.api.navigation.response.media

import pl.cyfrowypolsat.cpdata.api.common.model.cpidobject.CpidObject

data class GetMediaRelatedContentResult(val count: Int,
                                        val offset: Int,
                                        val results: List<CpidObject>)