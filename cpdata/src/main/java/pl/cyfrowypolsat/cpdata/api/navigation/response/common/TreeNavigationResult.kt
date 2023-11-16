package pl.cyfrowypolsat.cpdata.api.navigation.response.common

data class TreeNavigationResult(val collections: List<CollectionResult>?,
                                val filters: List<FilterWithTreeNavigationResult>?)

data class FilterWithTreeNavigationResult(val type: String,
                                          val value: String,
                                          val name: String,
                                          val treeNavigation: TreeNavigationResult)