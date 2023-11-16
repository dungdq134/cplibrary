package pl.cyfrowypolsat.cpdata.api.payments.response

import pl.cyfrowypolsat.cpdata.api.common.model.ProductIdParam
import java.util.*

data class ProductSubscriptionResult(val id: String,
                                     val product: ProductIdParam,
                                     val status: String,
                                     val nextPaymentDate: Date?) {

    companion object {
        const val ACTIVE_SUBSCRIPTION = "active"
        const val BLOCKED_SUBSCRIPTION = "blocked"
        const val CANCELLED_SUBSCRIPTION = "cancelled"
        const val FINISHED_SUBSCRIPTION = "finished"
        const val DELETED_SUBSCRIPTION = "deleted"
    }

}

fun ProductSubscriptionResult?.isActive() = this?.status == ProductSubscriptionResult.ACTIVE_SUBSCRIPTION

fun ProductSubscriptionResult?.isBlocked() = this?.status == ProductSubscriptionResult.BLOCKED_SUBSCRIPTION

fun ProductSubscriptionResult?.isFinished() = this?.status == ProductSubscriptionResult.FINISHED_SUBSCRIPTION

fun ProductSubscriptionResult?.isCanceled() = this?.status == ProductSubscriptionResult.CANCELLED_SUBSCRIPTION

fun ProductSubscriptionResult?.isDeleted() = this?.status == ProductSubscriptionResult.DELETED_SUBSCRIPTION

fun List<ProductSubscriptionResult>.contains(possessionResult: PossessionResult?): Boolean {
    return this.map { it.id }.contains(possessionResult?.productSubscriptionId)
}

fun List<ProductSubscriptionResult>.find(possessionResult: PossessionResult?): ProductSubscriptionResult? {
    return this.firstOrNull { it.id == possessionResult?.productSubscriptionId }
}