package pl.cyfrowypolsat.cpdata.api.drm.response

import pl.cyfrowypolsat.cpdata.api.common.model.Reason

data class CheckProductAccessResult(val statusDescription: String,
                                    val status: Int,
                                    val statusUserMessage: String?,
                                    val reason: Reason?,
                                    val externalActivationType: String?)
