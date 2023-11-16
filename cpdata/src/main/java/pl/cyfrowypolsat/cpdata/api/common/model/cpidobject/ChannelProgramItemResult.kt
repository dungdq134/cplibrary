package pl.cyfrowypolsat.cpdata.api.common.model.cpidobject

import pl.cyfrowypolsat.cpdata.api.common.model.ChannelIdResult
import pl.cyfrowypolsat.cpdata.api.common.model.GalleryItemResult
import pl.cyfrowypolsat.cpdata.api.common.model.ImageSourceResult
import pl.cyfrowypolsat.cpdata.api.common.model.ImageResult
import pl.cyfrowypolsat.cpdata.api.navigation.response.preplaydata.MediaId
import java.util.*

data class ChannelProgramItemResult(override val cpid: Int,
                                    val id: String,
                                    val title: String,
                                    val genre: String,
                                    val description: String,
                                    val date: Date,
                                    val startTime: Date,
                                    val live: Boolean?,
                                    val mediaId: MediaId?,
                                    val ageGroup: Int?,
                                    val underageClassification: List<String>?,
                                    val images: List<ImageResult>?,
                                    val posters: List<ImageSourceResult>?,
                                    val thumbnails: List<ImageSourceResult>?,
                                    val gallery: List<GalleryItemResult>?,
                                    val channelIds: List<ChannelIdResult>?,
                                    val reporting: ChannelProgramItemReporting?) : CpidObject

data class ChannelProgramItemReporting(val activityEvents: ActivityEvents?) {
    data class ActivityEvents(val contentItem: ContentItem)

    data class ContentItem(val type: String,
                           val value: String = "")
}
