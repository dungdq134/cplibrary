package pl.cyfrowypolsat.cpdata.api.navigation.request.channelprogram

import pl.cyfrowypolsat.cpdata.api.common.jsonrpc.JsonRPCParams
import pl.cyfrowypolsat.cpdata.common.utils.getDate
import java.util.*

data class GetChannelsProgramParams(val channelIds: List<String>,
                                    val date: String) : JsonRPCParams() {

    companion object {
        fun create(channelId: String,
                   date: Date): GetChannelsProgramParams {
            return GetChannelsProgramParams(channelIds = listOf(channelId),
                    date = getFormattedDate(date))
        }

        fun create(channelId: String,
                   formattedDate: String): GetChannelsProgramParams {
            return GetChannelsProgramParams(channelIds = listOf(channelId),
                    date = formattedDate)
        }

        fun getFormattedDate(date: Date): String {
            return getDate("yyyy-MM-dd", date)
        }
    }
}