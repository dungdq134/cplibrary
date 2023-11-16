package pl.cyfrowypolsat.cpcommon.domain.model.reporting

data class ReportingData(val activityEventsData: ActivityEventsData? = null,
                         val listId: String? = null)

data class ActivityEventsData(val contentItem: ContentItem) {

    data class ContentItem(val type: String,
                           val value: String = "")
}