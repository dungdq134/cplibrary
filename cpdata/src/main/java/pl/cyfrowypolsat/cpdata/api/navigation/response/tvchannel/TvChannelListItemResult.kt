package pl.cyfrowypolsat.cpdata.api.navigation.response.tvchannel

import pl.cyfrowypolsat.cpdata.api.common.model.GalleryItemResult
import pl.cyfrowypolsat.cpdata.api.common.model.ImageSourceResult

data class TvChannelListItemResult(val cpid: Int,
                                   val id: String,
                                   val title: String,
                                   val description: String,
                                   val mediaType: String,
                                   val posters: List<ImageSourceResult>?,
                                   val thumbnails: List<ImageSourceResult>?,
                                   val gallery: List<GalleryItemResult>?,
                                   val ageGroup: Int,
                                   val underageClassification: List<String>?,
                                   val accessibilityFeatures: List<String>?,
                                   val genres: List<String>,
                                   val timeShiftingDuration: Int?,
                                   val reporting: TvChannelListItemReporting?)

data class TvChannelListItemReporting(val activityEvents: ActivityEvents?) {
    data class ActivityEvents(val contentItem: ContentItem)

    data class ContentItem(val type: String,
                           val value: String = "")
}
