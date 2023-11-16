package pl.cyfrowypolsat.cpdata.api.navigation.response.recommendation

import com.google.gson.annotations.SerializedName
import pl.cyfrowypolsat.cpdata.api.common.model.ImageSourceResult
import pl.cyfrowypolsat.cpdata.api.common.model.cpidobject.CpidObject

data class GetStaffRecommendationsListItemsResult(val count: Int,
                                                  val results: List<GetStaffRecommendationsItem>)

data class GetStaffRecommendationsItem(val name: String?,
                                       val description: String?,
                                       val type: String,
                                       val posters: List<ImageSourceResult>?,
                                       val thumbnails: List<ImageSourceResult>?,
                                       val url: String?,
                                       @SerializedName("object") val recoObject: CpidObject?,
                                       val reporting: GetStaffRecommendationsItemReporting?)

data class GetStaffRecommendationsItemReporting(val activityEvents: ActivityEvents?) {
    data class ActivityEvents(val contentItem: ContentItem)

    data class ContentItem(val type: String,
                           val value: String = "")
}