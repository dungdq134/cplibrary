package pl.cyfrowypolsat.cpdata.api.navigation.response.tvchannel

data class GetTvChannelsTreeNavigationResult(val filters: List<TreeFilterResult>? = listOf())

data class TreeFilterResult(val type: String,
                            val value: String,
                            val name: String)