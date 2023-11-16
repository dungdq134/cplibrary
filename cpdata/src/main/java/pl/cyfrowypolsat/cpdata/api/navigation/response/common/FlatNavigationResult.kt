package pl.cyfrowypolsat.cpdata.api.navigation.response.common

data class FlatNavigationResult(val collections: List<CollectionResult>?,
                                val filterLists: List<FlatFilterListResult>?)

data class FlatFilterListResult(val filters: List<FilterResult>,
                                val name: String)

data class FilterResult(val type: String,
                        val value: String,
                        val name: String)
