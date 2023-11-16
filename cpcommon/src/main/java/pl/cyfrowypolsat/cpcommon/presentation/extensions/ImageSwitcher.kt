package pl.cyfrowypolsat.cpcommon.presentation.extensions

import android.widget.ImageSwitcher
import android.widget.ImageView
import androidx.annotation.DrawableRes

fun ImageSwitcher.setImageResource(@DrawableRes resid: Int, styleImage: (image: ImageView) -> Unit) {
    val image = nextView as ImageView
    image.setImageResource(resid)
    styleImage(image)
    showNext()
}