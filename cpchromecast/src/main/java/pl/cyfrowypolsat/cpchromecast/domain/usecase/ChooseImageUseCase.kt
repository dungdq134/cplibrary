package pl.cyfrowypolsat.cpchromecast.domain.usecase

import android.net.Uri
import com.google.android.gms.cast.MediaMetadata
import com.google.android.gms.cast.framework.media.ImageHints
import com.google.android.gms.common.images.WebImage
import pl.cyfrowypolsat.cpchromecast.core.di.CpChromecastQualifier
import pl.cyfrowypolsat.cpcommon.domain.model.enums.AspectType
import pl.cyfrowypolsat.cpcommon.domain.model.image.ImageSource
import pl.cyfrowypolsat.cpcommon.domain.usecase.FindBestImageUseCase
import javax.inject.Inject

class ChooseImageUseCase
@Inject constructor(@CpChromecastQualifier private val findBestImageUseCase: FindBestImageUseCase) {

    fun chooseImage(mediaMetadata: MediaMetadata?, hints: ImageHints? = null): WebImage? {
        if (mediaMetadata == null || !mediaMetadata.hasImages()) return null

        val width = hints?.widthInPixels?.toFloat()?.let { it * AspectType.ASPECT_16x9.ratio } ?: 720f
        findBestImageUseCase.findBestThumbnail(mapToImages(mediaMetadata), width)?.let {
            return WebImage(Uri.parse(it.src))
        }
        return null
    }

    private fun mapToImages(mediaMetadata: MediaMetadata): List<ImageSource> {
        return mediaMetadata.images.map { ImageSource(it.width, it.height, it.url.toString()) }
    }
}