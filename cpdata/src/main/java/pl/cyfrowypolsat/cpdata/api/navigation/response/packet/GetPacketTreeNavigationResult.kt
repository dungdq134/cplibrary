package pl.cyfrowypolsat.cpdata.api.navigation.response.packet

data class GetPacketTreeNavigationResult(val filters: List<TreeFilterResult>? = listOf())

data class TreeFilterResult(val type: String,
                            val value: String,
                            val name: String)