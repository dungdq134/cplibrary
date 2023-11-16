package pl.cyfrowypolsat.cpdata.api.payments.request

import pl.cyfrowypolsat.cpdata.api.common.jsonrpc.JsonRPCParams

data class RegisterPaymentCardParams(val paymentCardRegistrationId: String,
                                     val paymentCardRegistrationDataParams: PaymentCardRegistrationDataParams) : JsonRPCParams()

data class PaymentCardRegistrationDataParams(val operator: String)
