package pl.cyfrowypolsat.cpstats.core.model

data class ItemData(val activityEventsContentItem: ActivityEventsContentItem? = null)

data class ActivityEventsContentItem(val type: String,
                                     val value: String = "",
                                     val position: Int? = null)