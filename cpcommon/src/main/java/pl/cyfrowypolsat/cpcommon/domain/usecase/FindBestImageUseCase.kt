package pl.cyfrowypolsat.cpcommon.domain.usecase

import pl.cyfrowypolsat.cpcommon.domain.model.image.*
import timber.log.Timber
import javax.inject.Inject
import kotlin.math.abs

class FindBestImageUseCase
@Inject constructor(private val prepareImageUrlUseCase: PrepareImageUrlUseCase) {

    companion object {
        const val ASPECT_RATIO_TOLERANCE = 0.05
        const val LARGER_IMAGE_TOLERANCE = 1f
    }

    fun findBestThumbnail(imageSources: List<ImageSource>,
                          width: Float,
                          baseSrc: String? = null): ImageSource? {
        return findBestImage(imageSources, ImageSize.thumbnailSize(width), baseSrc)
    }

    fun findBestPoster(imageSources: List<ImageSource>,
                       width: Float,
                       baseSrc: String? = null): ImageSource? {
        return findBestImage(imageSources, ImageSize.posterSize(width), baseSrc)
    }

    fun findBestImage(imageSources: List<ImageSource>, searchedSize: ImageSize, baseSrc: String? = null): ImageSource? {
        val foundImage =  searchBestImage(searchedSize, imageSources)
                ?: prepareImageUrlUseCase.prepareBestImageSrc(baseSrc, searchedSize.width)?.let { ImageSource.create(it, searchedSize) }
        //Timber.d("foundImage: $foundImage")
        return foundImage
    }

    private fun searchBestImage(searchedSize: ImageSize, imageSources: List<ImageSource>): ImageSource? {
        val eligibleImage = imageSources.filter { abs(it.aspect - searchedSize.aspect) <= searchedSize.aspect * ASPECT_RATIO_TOLERANCE }
                .sortByDifferenceToWidthWithPreferenceForLargerImage(searchedSize.width, LARGER_IMAGE_TOLERANCE)
                .firstOrNull()

        //Timber.d("searchedSize: $searchedSize\nimages: ${imageSources.joinToString("\n")}\neligibleImage: $eligibleImage")

        return eligibleImage
                ?: imageSources.sortByDifferenceToWidthWithPreferenceForLargerImage(searchedSize.width, LARGER_IMAGE_TOLERANCE).firstOrNull()
    }
}

fun List<ImageSource>.sortByDifferenceToWidthWithPreferenceForLargerImage(width: Float, largerImageTolerance: Float): List<ImageSource> {
    return sortedWith(Comparator { img1, img2 ->
        val absDiff1 = abs(img1.width - width)
        val absDiff2 = abs(img2.width - width)
        val diff1 = img1.width - width
        val diff2 = img2.width - width
        if (absDiff2 < absDiff1) {
            if (diff2 < 0 && diff1 > 0 && diff1 < width * largerImageTolerance) -1 else 1
        } else if (absDiff1 < absDiff2) {
            if (diff1 < 0 && diff2 > 0 && diff2 < width * largerImageTolerance) 1 else -1
        } else {
            0
        }
    })
}