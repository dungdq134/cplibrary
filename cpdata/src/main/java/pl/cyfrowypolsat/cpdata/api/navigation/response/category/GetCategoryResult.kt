package pl.cyfrowypolsat.cpdata.api.navigation.response.category

import pl.cyfrowypolsat.cpdata.api.common.model.ImageSourceResult
import pl.cyfrowypolsat.cpdata.api.common.model.ImageResult
import pl.cyfrowypolsat.cpdata.api.common.model.cpidobject.CategoryReporting

data class GetCategoryResult(val cpid: Int,
                             val id: String,
                             val name: String,
                             val description: String,
                             val keyCategoryId: Int?,
                             val subCategoriesLabel: String?,
                             val genres: List<String>,
                             val images: List<ImageResult>?,
                             val thumbnails: List<ImageSourceResult>?,
                             val chronological: Boolean?,
                             val reporting: CategoryReporting?)
