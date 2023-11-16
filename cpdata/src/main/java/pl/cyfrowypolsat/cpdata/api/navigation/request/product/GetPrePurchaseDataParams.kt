package pl.cyfrowypolsat.cpdata.api.navigation.request.product

import pl.cyfrowypolsat.cpdata.api.common.jsonrpc.JsonRPCParams
import pl.cyfrowypolsat.cpdata.api.common.model.OfferIdParam
import pl.cyfrowypolsat.cpdata.api.common.model.ProductIdParam

data class GetPrePurchaseDataParams(val products: List<ProductIdParam>,
                                    val offers: List<OfferIdParam>? = null,
                                    val withoutUserPaymentsData: Boolean? = null,
                                    val withoutMinPriceProducts: Boolean? = null) : JsonRPCParams()
