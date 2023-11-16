package pl.cyfrowypolsat.cpdata.api.payments.response

import pl.cyfrowypolsat.cpdata.api.payments.request.PaymentCardRequest

data class GetPaymentCardsResult(val id: String,
                                 val maskedNumber: String,
                                 val brand: PaymentCardBrandResult,
                                 val expirationDate: PaymentCardExpirationDateResult,
                                 val operator: String,
                                 val default: Boolean?) {

    fun toPaymentCardRequest(): PaymentCardRequest {
        return PaymentCardRequest(id, operator)
    }
}


data class PaymentCardBrandResult(val name: String,
                                  val type: String)

data class PaymentCardExpirationDateResult(val month: Int,
                                           val year: Int)