package pl.cyfrowypolsat.cpstats.core.model

data class ListData(val activityEventsList: ActivityEventsList? = null)

data class ActivityEventsList(val type: String,
                              val value: String? = null,
                              val position: Int? = null)