package pl.cyfrowypolsat.cpdata.api.navigation.response.category

import pl.cyfrowypolsat.cpdata.api.common.model.cpidobject.CpidObject

data class GetCategoryContentResult(val count: Int,
                                    val offset: Int,
                                    val total: Int,
                                    val results: List<CpidObject>)