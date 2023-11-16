package pl.cyfrowypolsat.cpdata.api.payments.request

import pl.cyfrowypolsat.cpdata.api.common.jsonrpc.JsonRPCParams

data class GetPurchaseCodeDataParams(val purchaseCode: String) : JsonRPCParams()