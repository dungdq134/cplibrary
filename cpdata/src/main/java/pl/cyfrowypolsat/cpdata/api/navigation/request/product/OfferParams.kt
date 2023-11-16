package pl.cyfrowypolsat.cpdata.api.navigation.request.product

import pl.cyfrowypolsat.cpdata.api.common.jsonrpc.JsonRPCParams
import pl.cyfrowypolsat.cpdata.api.common.model.OfferIdParam


data class OfferParams(val offer: OfferIdParam, val offerPricingPlanId: String? = null) : JsonRPCParams() {
    constructor(id: String, type: String, offerPricingPlanId: String? = null) : this(OfferIdParam(id, type), offerPricingPlanId)
}
