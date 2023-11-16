package pl.cyfrowypolsat.cpdata.api.navigation.response.search

import pl.cyfrowypolsat.cpdata.api.common.model.cpidobject.CpidObject

data class SearchContentResult(val results: List<CpidObject>,
                               val offset: Int,
                               val count: Int,
                               val limit: Int?,
                               val total: Int?)