package pl.cyfrowypolsat.cpdata.api.payments.response

import pl.cyfrowypolsat.cpdata.api.common.model.ProductIdParam
import java.util.*

data class PossessionResult(val product: ProductIdParam,
                            val status: String,
                            val validFrom: Date?,
                            val validTo: Date?,
                            val offer: PossessionOfferResult,
                            val productSubscriptionId: String?) {

    companion object {
        const val ACTIVE_POSSESSION = "active"
        const val BLOCKED_POSSESSION = "blocked"
    }

}

data class PossessionOfferResult(val id: String,
                                 val type: String)

fun PossessionResult?.isActive() = this?.status == PossessionResult.ACTIVE_POSSESSION

fun PossessionResult?.isBlocked() = this?.status == PossessionResult.BLOCKED_POSSESSION

fun PossessionResult?.isOneTimeInfinitelyAccessActive() = this?.validTo == null && this?.productSubscriptionId == null && this?.status == PossessionResult.ACTIVE_POSSESSION