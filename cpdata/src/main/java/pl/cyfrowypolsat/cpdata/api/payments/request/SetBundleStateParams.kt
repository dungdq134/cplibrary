package pl.cyfrowypolsat.cpdata.api.payments.request

import pl.cyfrowypolsat.cpdata.api.common.jsonrpc.JsonRPCParams
import pl.cyfrowypolsat.cpdata.api.common.model.ProductIdParam

data class SetBundleStateParams(val bundleId: String,
                                val products: List<ProductIdParam>) : JsonRPCParams()