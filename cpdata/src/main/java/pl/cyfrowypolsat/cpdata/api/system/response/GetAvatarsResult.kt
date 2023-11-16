package pl.cyfrowypolsat.cpdata.api.system.response

import pl.cyfrowypolsat.cpdata.api.common.model.ImageResult

data class AvatarResult(val id: String,
                        val images: List<ImageResult>?)