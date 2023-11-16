package pl.cyfrowypolsat.cpdata.api.common.model.cpidobject

import pl.cyfrowypolsat.cpdata.api.common.model.ImageSourceResult
import pl.cyfrowypolsat.cpdata.api.common.model.ProductIdParam
import java.util.*

data class MediaListItemResult(override val cpid: Int,
                               val id: String,
                               val title: String,
                               val description: String,
                               val mediaType: String,
                               val posters: List<ImageSourceResult>,
                               val thumbnails: List<ImageSourceResult>,
                               val product: ProductIdParam?,
                               val shortTitle: String?,
                               val episodeTitle: String?,
                               val ageGroup: Int,
                               val underageClassification: List<String>?,
                               val accessibilityFeatures: List<String>?,
                               val genres: List<String>,
                               val countries: List<String>?,
                               val publicationDate: Date?,
                               val originalTitle: String?,
                               val releaseYear: String?,
                               val duration: Int?,
                               val category: CategoryResult?,
                               val allowDownload: Boolean?,
                               val reporting: MediaListItemReporting?) : CpidObject

data class MediaListItemReporting(val activityEvents: ActivityEvents?) {
    data class ActivityEvents(val contentItem: ContentItem)

    data class ContentItem(val type: String,
                           val value: String = "")
}
