package pl.cyfrowypolsat.cpdata.api.drm.response

import pl.cyfrowypolsat.cpdata.api.common.model.ProductIdParam

data class CheckProductsAccessResult(val access: CheckProductAccessResult,
                                     val product: ProductIdParam)
