package pl.cyfrowypolsat.cpdata.api.drm.request

import pl.cyfrowypolsat.cpdata.api.common.jsonrpc.JsonRPCParams
import pl.cyfrowypolsat.cpdata.api.common.model.ProductIdParam

data class CheckProductsAccessParams(val products: List<ProductIdParam>, val offline: Boolean = false) : JsonRPCParams() {
    constructor(checkProductAccessParams: CheckProductAccessParams) : this(listOf(checkProductAccessParams.product), checkProductAccessParams.offline)
}