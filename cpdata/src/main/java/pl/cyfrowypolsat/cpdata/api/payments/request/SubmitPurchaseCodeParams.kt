package pl.cyfrowypolsat.cpdata.api.payments.request

import pl.cyfrowypolsat.cpdata.api.common.jsonrpc.JsonRPCParams
import pl.cyfrowypolsat.cpdata.api.common.model.ProductIdParam

data class SubmitPurchaseCodeParams(val product: ProductIdParam,
                                    val offer: OfferRequest,
                                    val option: PaymentOptionRequest,
                                    val orderId: String,
                                    val purchaseCode: String) : JsonRPCParams()