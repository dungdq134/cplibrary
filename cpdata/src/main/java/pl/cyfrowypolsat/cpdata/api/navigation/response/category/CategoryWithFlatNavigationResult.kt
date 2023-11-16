package pl.cyfrowypolsat.cpdata.api.navigation.response.category

import pl.cyfrowypolsat.cpdata.api.common.model.ImageSourceResult
import pl.cyfrowypolsat.cpdata.api.navigation.response.common.FlatNavigationResult

data class CategoryWithFlatNavigationResult(
        val id: Int,
        val name: String,
        val description: String?,
        val thumbnails: List<ImageSourceResult>?,
        val flatNavigation: FlatNavigationResult
)