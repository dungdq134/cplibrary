package pl.cyfrowypolsat.cpcommon.presentation.imageloader

import android.graphics.drawable.Drawable
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

open class GlideTarget<T : Any>: CustomTarget<T>() {

    override fun onLoadFailed(errorDrawable: Drawable?) {
    }

    override fun onLoadCleared(placeholder: Drawable?) {
    }

    override fun onResourceReady(resource: T,
                                 transition: Transition<in T>?) {
    }
}