package pl.cyfrowypolsat.cpdata.api.payments.request

import pl.cyfrowypolsat.cpdata.api.common.jsonrpc.JsonRPCParams

data class GetOrderStatusParams(val orderId: String) : JsonRPCParams()