package pl.cyfrowypolsat.cpdata.api.payments.request

import pl.cyfrowypolsat.cpdata.api.common.jsonrpc.JsonRPCParams
import pl.cyfrowypolsat.cpdata.api.common.model.ProductIdParam

data class IsTrialAvailableParams(private val product: ProductIdParam,
                                  private val offer: OfferRequest,
                                  private val option: IsTrialAvailableOptionRequest) : JsonRPCParams()

interface IsTrialAvailableOptionRequest {
    val type: String
}


data class IsTrialAvailableCardOptionRequest(override val type: String,
                                             val paymentCard: PaymentCardRequest) : IsTrialAvailableOptionRequest


data class IsTrialAvailableSimpleOptionRequest(override val type: String) : IsTrialAvailableOptionRequest