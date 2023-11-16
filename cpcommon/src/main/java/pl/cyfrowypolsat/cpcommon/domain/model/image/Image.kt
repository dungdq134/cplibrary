package pl.cyfrowypolsat.cpcommon.domain.model.image

import pl.cyfrowypolsat.cpcommon.domain.model.enums.AppThemeType
import pl.cyfrowypolsat.cpcommon.domain.model.enums.AspectType
import pl.cyfrowypolsat.cpcommon.domain.model.enums.BackgroundEffectType
import pl.cyfrowypolsat.cpcommon.domain.model.enums.DisplayPlaceType
import pl.cyfrowypolsat.cpcommon.domain.model.enums.DisplayType
import pl.cyfrowypolsat.cpcommon.domain.model.enums.RollOverEffectType

fun Image?.sources() = this?.sources ?: listOf()
fun List<Image>.search(filter: Image? = null) = filter {
    filter?.run {
        aspect?.run { it.aspect == this } ?: true
                && it.display == display
                && it.rollOverEffect == rollOverEffect
                && it.backgroundEffect == backgroundEffect
                && it.displayPlace == displayPlace
                && it.appTheme == appTheme
                && it.keyFrame == keyFrame
    } ?: true
}.sortedBy { it.appGalleryOrder }.firstOrNull()

fun List<Image>.thumbnails(filter: Image? = null) = search(filter?.copy(aspect = AspectType.ASPECT_16x9))
fun List<Image>.posters(filter: Image? = null) = search(filter?.copy(aspect = AspectType.ASPECT_7x10))
fun List<Image>.squares(filter: Image? = null) = search(filter?.copy(aspect = AspectType.ASPECT_1x1))

fun List<Image>.thumbnailsForBackground(filter: Image? = null) = thumbnails(filter?.copy(displayPlace = DisplayPlaceType.BACKGROUND))
        ?: thumbnails(filter)

data class Image(val aspect: AspectType? = null,
                 val sources: List<ImageSource> = listOf(),
                 val display: DisplayType = DisplayType.DEFAULT,
                 val rollOverEffect: RollOverEffectType = RollOverEffectType.DEFAULT,
                 val backgroundEffect: BackgroundEffectType = BackgroundEffectType.DEFAULT,
                 val displayPlace: DisplayPlaceType = DisplayPlaceType.DEFAULT,
                 val appTheme: AppThemeType = AppThemeType.DEFAULT,
                 val keyFrame: Boolean = false,
                 val appGalleryOrder: Int = 0,
                 val baseSrc: String? = null)

data class ImageSource(val width: Int,
                       val height: Int,
                       val src: String) {

    val aspect: Float = width.toFloat() / height.toFloat()

    companion object {
        fun create(src: String, imageSize: ImageSize): ImageSource {
            return ImageSource(imageSize.width.toInt(), imageSize.height.toInt(), src)
        }
    }
}

data class ImageSize(val aspect: Float,
                     val width: Float) {

    val height = width / aspect

    companion object {
        fun thumbnailSize(width: Float) = ImageSize(AspectType.ASPECT_16x9.ratio, width)
        fun posterSize(width: Float) = ImageSize(AspectType.ASPECT_7x10.ratio, width)
        fun squareSize(width: Float) = ImageSize(AspectType.ASPECT_1x1.ratio, width)
    }
}
