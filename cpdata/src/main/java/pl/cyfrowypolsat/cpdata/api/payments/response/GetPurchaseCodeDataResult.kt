package pl.cyfrowypolsat.cpdata.api.payments.response

import java.util.*

data class GetPurchaseCodeDataResult(val status: String,
                                     val validFrom: Date? = null,
                                     val validTo: Date? = null,
                                     val purchaseItems: List<PurchaseItem>,
                                     val activationAvailability: ActivationAvailabilityResult)

data class PurchaseItem(val product: Product? = null,
                        val offer: Offer)

data class Product(val id: String,
                   val type: String,
                   val subType: String)

data class Offer(val id: String,
                 val type: String)

data class ActivationAvailabilityResult(val status: Int,
                                        val statusDescription: String,
                                        val statusUserMessage: String?)