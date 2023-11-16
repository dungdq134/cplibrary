package pl.cyfrowypolsat.cpdata.api.navigation.response.tvchannel

data class GetTvChannelsResult(
        val count: Int,
        val offset: Int,
        val results: List<TvChannelListItemResult>
)