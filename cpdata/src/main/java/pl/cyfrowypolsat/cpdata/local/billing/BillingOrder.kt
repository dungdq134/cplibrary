package pl.cyfrowypolsat.cpdata.local.billing

import java.util.*

data class BillingOrder(val id: String,
                        val subtype: String,
                        val type: String,
                        val offerType: String,
                        val offerId: String,
                        val paymentOptionType: String,
                        val paymentOptionId: String,
                        val regularPrice: Int,
                        val currency: String,
                        val offerPricingPlanId: String?,
                        val accessText: String,
                        val accessTo: Date?,
                        val cyclic: Boolean,
                        val availableTrialDuration: Int?,
                        val googlePlayProductId: String?)