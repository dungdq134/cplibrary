package pl.cyfrowypolsat.cpchromecast.presentation.imagepicker

import com.google.android.gms.cast.MediaMetadata
import com.google.android.gms.cast.framework.media.ImageHints
import com.google.android.gms.cast.framework.media.ImagePicker
import com.google.android.gms.common.images.WebImage
import pl.cyfrowypolsat.cpchromecast.CpChromecast
import pl.cyfrowypolsat.cpchromecast.domain.usecase.ChooseImageUseCase
import javax.inject.Inject

class ChromecastImagePicker : ImagePicker() {

    companion object {
        fun newInstance(): ChromecastImagePicker {
            return ChromecastImagePicker()
        }
    }

    @Inject lateinit var chooseImageUseCase: ChooseImageUseCase

    init {
        CpChromecast.instance.getCpChromecastComponent().inject(this)
    }

    override fun onPickImage(mediaMetadata: MediaMetadata?, hints: ImageHints): WebImage? {
        return chooseImageUseCase.chooseImage(mediaMetadata, hints)
    }

}