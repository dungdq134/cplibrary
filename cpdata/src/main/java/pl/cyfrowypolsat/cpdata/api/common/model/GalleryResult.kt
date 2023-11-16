package pl.cyfrowypolsat.cpdata.api.common.model

data class GalleryItemResult(val aspect: AspectRatioResult,
                             val customParams: CustomParamsResult?,
                             val resolutions: List<GalleryImageResolutionResult>?,
                             val src: String)

data class AspectRatioResult(val width: Int,
                             val height: Int)

data class CustomParamsResult(val display: String?,
                              val rollOverEffect: String?,
                              val backgroundEffect: String?,
                              val displayPlace: String?,
                              val appTheme: String?,
                              val keyFrame: Boolean?,
                              val appGalleryOrder: Int?)

data class GalleryImageResolutionResult(val width: Int,
                                        val height: Int,
                                        val url: String)


