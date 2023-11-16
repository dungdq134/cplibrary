package pl.cyfrowypolsat.cpdata.api.navigation.response.live

data class GetLiveChannelsResult(
        val count: Int,
        val offset: Int,
        val results: List<LiveChannelListItemResult>
)