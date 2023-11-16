package pl.cyfrowypolsat.cpdata.api.payments.response

import pl.cyfrowypolsat.cpdata.api.common.model.Result
import pl.cyfrowypolsat.cpdata.api.navigation.response.product.ProductResult
import java.util.*

data class GetPrePurchaseDataResult(val product: ProductIdResult,
                                    val purchaseAvailability: Result,
                                    val purchaseCodeAvailability: Result,
                                    val minPriceProduct: ProductResult?,
                                    val relatedMinPriceProducts: List<ProductResult>?,
                                    val minPricePurchaseText: String?,
                                    val productAccessText: String?)

interface ProductIdResult {
    val id: String
    val subType: String
    val type: String
}

data class SimpleProductIdResult(
    override val id: String,
    override val subType: String,
    override val type: String
) : ProductIdResult

data class PrePurchaseProductResult(
    override val id: String,
    override val subType: String,
    override val type: String,
    val name: String,
    val offers: List<PrePurchaseOfferResult>?
) : ProductIdResult

data class PrePurchaseOfferResult(
    val id: String,
    val type: String,
    val name: String?,
    val description: String?,
    val accessText: String?,
    val longAccessText: String?,
    val accessTo: Date?
)

