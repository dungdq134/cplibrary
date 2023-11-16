package pl.cyfrowypolsat.cpdata.api.payments.request

import pl.cyfrowypolsat.cpdata.api.common.jsonrpc.JsonRPCParams
import pl.cyfrowypolsat.cpdata.api.common.model.ProductIdParam


data class SubmitReceiptParams(val product: ProductIdParam,
                               val offer: OfferRequest,
                               val option: SubmitReceiptOptionRequest,
                               val orderId: String,
                               val receipt: String) : JsonRPCParams()

data class SubmitReceiptOptionRequest(val id: String,
                                      val type: String,
                                      val price: PriceRequest)