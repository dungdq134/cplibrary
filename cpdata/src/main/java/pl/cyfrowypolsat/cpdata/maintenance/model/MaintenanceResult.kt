package pl.cyfrowypolsat.cpdata.maintenance.model

import android.content.Context
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import pl.cyfrowypolsat.cpdata.R
import java.util.*

@Parcelize
data class MaintenanceResult(@SerializedName("icon") val icon: String? = null,
                             @SerializedName("title") val title: String? = null,
                             @SerializedName("description") val description: String? = null,
                             @SerializedName("auto_refresh_interval") val autoRefreshInterval: Int = 0,
                             @SerializedName("json_refresh_interval") val jsonRefreshInterval: Int = 0,
                             @SerializedName("end_data") val endData: EndData? = null): Parcelable {
    companion object {
        fun defaultValues(context: Context): MaintenanceResult {
            return MaintenanceResult(
                    icon = "info",
                    title = context.resources.getString(R.string.maintenance_default_title),
                    description = context.resources.getString(R.string.maintenance_default_description),
                    autoRefreshInterval = 0,
                    jsonRefreshInterval = 60,
                    endData = EndData(
                            plannedEnd = null,
                            description = null,
                            timeFormat = null))
        }
    }
}

@Parcelize
data class EndData(@SerializedName("planned_end") val plannedEnd: Date?,
                   @SerializedName("description") val description: String?,
                   @SerializedName("time_format") val timeFormat: String?): Parcelable