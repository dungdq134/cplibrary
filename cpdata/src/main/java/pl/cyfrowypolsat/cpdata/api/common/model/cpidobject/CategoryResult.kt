package pl.cyfrowypolsat.cpdata.api.common.model.cpidobject

import pl.cyfrowypolsat.cpdata.api.common.model.ImageSourceResult

data class CategoryResult(override val cpid: Int,
                          val id: String,
                          val name: String,
                          val description: String,
                          val categoryNamesPath: List<String>?,
                          val keyCategoryId: Int?,
                          val subCategoriesLabel: String?,
                          val genres: List<String>,
                          val thumbnails: List<ImageSourceResult>,
                          val seasonNumber: Int?,
                          val chronological: Boolean?,
                          val reporting: CategoryReporting?) : CpidObject

data class CategoryReporting(val activityEvents: ActivityEvents?) {
    data class ActivityEvents(val contentItem: ContentItem)

    data class ContentItem(val type: String,
                           val value: String = "")
}