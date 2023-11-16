package pl.cyfrowypolsat.cpdata.api.navigation.request.product

import pl.cyfrowypolsat.cpdata.api.common.jsonrpc.JsonRPCParams
import pl.cyfrowypolsat.cpdata.api.common.model.ProductIdParam


data class ProductParams(val product: ProductIdParam, val offerPricingPlanId: String? = null) : JsonRPCParams() {
    constructor(id: String, subType: String, type: String, offerPricingPlanId: String? = null) : this(ProductIdParam(id, subType, type), offerPricingPlanId)
}
