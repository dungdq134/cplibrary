package pl.cyfrowypolsat.cpdata.api.navigation.response.product

import pl.cyfrowypolsat.cpdata.api.common.model.ProductIdParam

data class BundleResult(val id: String,
                        val name: String,
                        val description: String?,
                        val items: List<BundleItemResult>?)

data class BundleItemResult(val product: ProductIdParam,
                            val default: Boolean?)