package pl.cyfrowypolsat.cpdata.api.navigation.response.category

import pl.cyfrowypolsat.cpdata.api.navigation.response.common.BasicNavigationResult

data class CategoryWithBasicNavigationResult(val id: Int,
                                             val name: String,
                                             val basicNavigation: BasicNavigationResult)