package pl.cyfrowypolsat.cpdata.api.payments.request

import pl.cyfrowypolsat.cpdata.api.common.jsonrpc.JsonRPCParams

data class PaymentCardParams(val paymentCard: PaymentCardIdParam) : JsonRPCParams()

data class PaymentCardIdParam(val id: String,
                              val operator: String)
