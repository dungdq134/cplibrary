package pl.cyfrowypolsat.cpdata.api.drm.request

import pl.cyfrowypolsat.cpdata.api.common.jsonrpc.JsonRPCParams
import pl.cyfrowypolsat.cpdata.api.common.model.ProductIdParam

data class CheckProductExternalActivationParams(val product: ProductIdParam, val externalActivationType: String) : JsonRPCParams()
