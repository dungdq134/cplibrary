package pl.cyfrowypolsat.cpdata.api.common.model


data class ImageResult(val type: String,
                       val sources: List<ImageSourceResult>)

data class ImageSourceResult(val type: String, val size: ImageSizeResult, val src: String)

data class ImageSizeResult(val width: Int, val height: Int)

enum class ImageResultType(val type: String) {
    THUMBNAIL("thumbnail"),
    POSTER("poster"),
    SQUARE("square"),
    BACKGROUND("background"),
    OTHER("other");

    companion object {
        fun getFromString(value: String?): ImageResultType {
            return when (value) {
                THUMBNAIL.type -> THUMBNAIL
                POSTER.type -> POSTER
                SQUARE.type -> SQUARE
                BACKGROUND.type -> BACKGROUND
                else -> OTHER
            }
        }
    }
}
