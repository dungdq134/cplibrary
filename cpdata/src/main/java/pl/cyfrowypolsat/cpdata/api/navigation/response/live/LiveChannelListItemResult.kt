package pl.cyfrowypolsat.cpdata.api.navigation.response.live

import pl.cyfrowypolsat.cpdata.api.common.model.ImageSourceResult
import java.util.*

data class LiveChannelListItemResult(val cpid: Int,
                                     val id: String,
                                     val title: String,
                                     val description: String,
                                     val posters: List<ImageSourceResult>,
                                     val thumbnails: List<ImageSourceResult>,
                                     val ageGroup: Int,
                                     val underageClassification: List<String>?,
                                     val accessibilityFeatures: List<String>?,
                                     val genres: List<String>,
                                     val publicationDate: Date?,
                                     val reporting: LiveChannelListItemReporting?)

data class LiveChannelListItemReporting(val activityEvents: ActivityEvents?) {
    data class ActivityEvents(val contentItem: ContentItem)

    data class ContentItem(val type: String,
                           val value: String = "")
}