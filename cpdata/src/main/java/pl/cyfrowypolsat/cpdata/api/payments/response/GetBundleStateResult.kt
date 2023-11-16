package pl.cyfrowypolsat.cpdata.api.payments.response

import pl.cyfrowypolsat.cpdata.api.common.model.ProductIdParam

data class GetBundleStateResult(val products: List<ProductIdParam>,
                                val slots: Int,
                                val changes: Int,
                                val usedChanges: Int)