package pl.cyfrowypolsat.cpdata.api.navigation.response.homemenu

import pl.cyfrowypolsat.cpdata.api.common.model.ImageResult

data class HomeMenuItemResult(val name: String,
                          val place: PlaceResult?,
                          val images: List<ImageResult>?)

data class PlaceResult(val type: String,
                       val value: String)