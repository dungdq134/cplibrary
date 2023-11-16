package pl.cyfrowypolsat.cpdata.api.common.model.cpidobject

import pl.cyfrowypolsat.cpdata.api.common.model.ImageSourceResult

data class PacketResult(override val cpid: Int,
                        val id: String,
                        val name: String,
                        val description: String?,
                        val thumbnails: List<ImageSourceResult>?,
                        val reporting: PacketReporting?) : CpidObject

data class PacketReporting(val activityEvents: ActivityEvents?) {
    data class ActivityEvents(val contentItem: ContentItem)

    data class ContentItem(val type: String,
                           val value: String = "")
}
