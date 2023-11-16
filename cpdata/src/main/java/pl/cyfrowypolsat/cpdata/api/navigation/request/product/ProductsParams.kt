package pl.cyfrowypolsat.cpdata.api.navigation.request.product

import pl.cyfrowypolsat.cpdata.api.common.jsonrpc.JsonRPCParams
import pl.cyfrowypolsat.cpdata.api.common.model.ProductIdParam

data class ProductsParams(val products: List<ProductIdParam>) : JsonRPCParams() {
    constructor(productId: ProductIdParam) : this(listOf(productId))
}
