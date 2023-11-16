package pl.cyfrowypolsat.cpdata.api.navigation.response.product

import pl.cyfrowypolsat.cpdata.api.common.model.*
import pl.cyfrowypolsat.cpdata.api.payments.response.PossessionOfferResult
import java.util.*


data class ProductResult(val id: String,
                         val subType: String,
                         val type: String,
                         val name: String,
                         val offers: List<OfferResult>?,
                         val description: String?,
                         val purchaseDescription: String?,
                         val images: List<ImageResult>?,
                         val thumbnails: List<ImageSourceResult>?,
                         val platforms: List<String>?,
                         val posters: List<ImageSourceResult>?,
                         val ageGroup: Int?,
                         val relatedProducts: List<ProductIdParam>?,
                         val minPrice: PriceResult?,
                         val minPriceOffers: List<OfferId>?,
                         val minPriceIncludingPricingPlans: MinPriceIncludingPricingPlans?,
                         val minPriceOffersIncludingPricingPlans: List<OfferId>?,
                         val accessText: String?,
                         val licenseEndDate: Date?,
                         val licenseLocationText: String?,
                         val reporting: ProductReporting?) {

    fun productId(): ProductIdParam {
        return ProductIdParam(id, subType, type)
    }
}

data class OfferResult(val id: String,
                       val type: String,
                       val accessText: String?,
                       val longAccessText: String?,
                       val accessDuration: Int?,
                       val validTo: Date?,
                       val name: String?,
                       val recommended: Boolean?,
                       val description: String?,
                       var options: List<PaymentOptionResult>?,
                       val offerPricingPlans: List<OfferPricingPlanResult>?,
                       val cyclic: CyclicResult?,
                       val trial: TrialResult?,
                       val resignInfo: ResignInfo?,
                       val info: Info?,
                       val images: List<ImageResult>?,
                       val minPrice: PriceResult?,
                       val minPriceOptions: List<PaymentOptionId>?,
                       val minPriceIncludingPricingPlans: MinPriceIncludingPricingPlans?,
                       val minPriceOptionsIncludingPricingPlans: List<PaymentOptionId>?,
                       val offerPricingPlanRequired: Boolean?,
                       val rules: List<RuleResult>?) {

    fun possessionOfferResult() = PossessionOfferResult(id, type)
    fun offerId() = OfferIdParam(id, type)
}

data class PaymentOptionResult(val id: String,
                               val type: String,
                               val name: String? = null,
                               val description: String? = null,
                               val prices: List<PriceResult>? = null,
                               val rules: List<RuleResult>? = null,
                               val minPrice: PriceResult?,
                               val minPriceIncludingPricingPlans: MinPriceIncludingPricingPlans?,
                               val googleplayProductId: String?)

data class CyclicResult(val maxCycles: Int,
                        val maxTries: Int,
                        val retryInterval: Int)

data class TrialResult(val duration: Int)

data class PriceResult(val currency: String,
                       val value: Int,
                       val valueText: String,
                       val higherPricesExist: Boolean?)

data class ResignInfo(val description: String)

data class Info(val sources: List<Source>)

data class Source(val type: String,
                  val url: String)

data class OfferId(val id: String,
                   val type: String)

data class PaymentOptionId(val id: String,
                           val type: String)

data class OfferPricingPlanResult(val id: String,
                                  val validFrom: Date?,
                                  val validTo: Date?,
                                  val limit: OfferPricingPlanLimitResult?,
                                  val pricingPlan: PricingPlanResult,
                                  val minPriceIncludingPricingPlans: MinPriceIncludingPricingPlans?)

data class OfferPricingPlanLimitResult(val total: Int?,
                                       val perUser: Int?)

data class PricingPlanResult(val id: String,
                             val name: String,
                             val description: String?,
                             val validFrom: Date?,
                             val validTo: Date?,
                             val info: Info?,
                             val rules: List<RuleResult>?,
                             val items: List<PricingPlanItem>?)

data class MinPriceIncludingPricingPlans(val currency: String,
                                         val value: Int,
                                         val valueText: String,
                                         val processorFee: Int?,
                                         val processorFeeText: String?,
                                         val higherPricesExist: Boolean?,
                                         val valueIncludingPricingPlans: Int?,
                                         val valueTextIncludingPricingPlans: String?)

data class PricingPlanItem(val optionType: String?,
                           val validFrom: Date?,
                           val validTo: Date?,
                           val cycleNoFrom: Int?,
                           val cycleNoTo: Int?,
                           val currency: String?,
                           val valueModifier: PriceValueModifier)

data class PriceValueModifier(val type: String,
                              val value: Int)

data class ProductReporting(val activityEvents: ActivityEvents?) {
    data class ActivityEvents(val contentItem: ContentItem)

    data class ContentItem(val type: String,
                           val value: String = "")
}