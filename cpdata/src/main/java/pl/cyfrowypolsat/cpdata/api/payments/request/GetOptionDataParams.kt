package pl.cyfrowypolsat.cpdata.api.payments.request

import pl.cyfrowypolsat.cpdata.api.common.jsonrpc.JsonRPCParams
import pl.cyfrowypolsat.cpdata.api.common.model.ProductIdParam

data class GetOptionDataParams(val product: ProductIdParam,
                               val offer: OfferRequest,
                               val option: GetOptionDataRequest,
                               val orderId: String) : JsonRPCParams()

data class GetOptionDataRequest(val id: String,
                                val type: String,
                                val price: PriceRequest)
