package pl.cyfrowypolsat.cpdata.api.navigation.request.product

import pl.cyfrowypolsat.cpdata.api.common.jsonrpc.JsonRPCParams
import pl.cyfrowypolsat.cpdata.api.common.model.ProductIdParam

data class GetPurchaseCodeProductParams(val product: ProductIdParam?,
                                        val purchaseCode: String) : JsonRPCParams()