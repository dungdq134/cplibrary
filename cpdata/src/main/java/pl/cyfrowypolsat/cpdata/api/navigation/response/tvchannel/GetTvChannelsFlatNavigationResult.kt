package pl.cyfrowypolsat.cpdata.api.navigation.response.tvchannel

import pl.cyfrowypolsat.cpdata.api.common.model.ImageSourceResult
import pl.cyfrowypolsat.cpdata.api.navigation.response.common.CollectionResult
import pl.cyfrowypolsat.cpdata.api.navigation.response.common.FlatFilterListResult

data class GetTvChannelsFlatNavigationResult(
        val collections: List<CollectionResult>?,
        val thumbnails: List<ImageSourceResult>?,
        val filterLists: List<FlatFilterListResult>?
)