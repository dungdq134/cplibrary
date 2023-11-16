package pl.cyfrowypolsat.cpcommon.presentation.imageloader

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.annotation.RawRes
import androidx.core.content.ContextCompat
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.Target
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import pl.cyfrowypolsat.cpcommon.CpCommon
import pl.cyfrowypolsat.cpcommon.R
import pl.cyfrowypolsat.cpcommon.core.di.CpCommonQualifier
import pl.cyfrowypolsat.cpcommon.domain.model.image.ImageSize
import pl.cyfrowypolsat.cpcommon.domain.model.image.ImageSource
import pl.cyfrowypolsat.cpcommon.domain.usecase.FindBestImageUseCase
import pl.cyfrowypolsat.cpcommon.presentation.extensions.asActivity
import pl.cyfrowypolsat.cpcommon.presentation.extensions.isAfterOnSaveInstanceState
import timber.log.Timber
import javax.inject.Inject

class ImageLoader(private val context: Context) {

    companion object {
        fun mobilePlaceHolderId() = R.drawable.cpcmn_mobile_placeholder_static
    }

    init {
        CpCommon.instance.getCpCommonComponent().inject(this)
    }

    val disposables = CompositeDisposable()

    @CpCommonQualifier
    @Inject lateinit var findBestImageUseCase: FindBestImageUseCase

    fun loadAsBitmap(imageSources: List<ImageSource>,
                     imageSize: ImageSize,
                     requestOptions: RequestOptions? = null,
                     target: CustomTarget<Bitmap>) {
        disposables.add(Observable.fromCallable<ImageSource> { findBestImageUseCase.findBestImage(imageSources, imageSize) }
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ image ->
                    Glide.with(context)
                            .asBitmap()
                            .load(image.src)
                            .apply { requestOptions?.let { apply(requestOptions) } }
                            .into(target)

                }, { throwable -> Timber.d(throwable) }))
    }

    fun loadImage(imageSources: List<ImageSource>,
                  imageSize: ImageSize,
                  imageView: ImageView,
                  @DrawableRes placeHolderId: Int? = null,
                  baseSrc: String? = null) {
        loadImage(findBestImage = { findBestImageUseCase.findBestImage(imageSources, imageSize, baseSrc) },
                imageView = imageView,
                placeHolderId = placeHolderId)
    }

    private fun loadImage(findBestImage: () -> ImageSource?,
                          imageView: ImageView,
                          @DrawableRes placeHolderId: Int? = null) {
        disposables.add(Observable.fromCallable<ImageSource> { findBestImage() }
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ image ->
                    Glide.with(context)
                            .load(image.src)
                            .transition(DrawableTransitionOptions.with(CrossFadeFactory()))
                            .apply { placeHolderId?.let { placeholder(ContextCompat.getDrawable(context, it)) } }
                            .into(imageView)
                }, { throwable -> Timber.d(throwable) }))
    }

    fun loadImage(imageView: ImageView, imageUrl: String?, defaultImageId: Int) {
        Glide.with(imageView.context)
                .load(imageUrl)
                .error(defaultImageId)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView)
    }

    fun loadCircleImage(imageView: ImageView, imageUrl: String?, defaultImageId: Int) {
        Glide.with(imageView.context)
                .load(imageUrl)
                .circleCrop()
                .error(defaultImageId)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView)
    }

    fun loadImage(imageView: ImageView, imageUrl: String?) {
        Glide.with(imageView.context)
                .load(imageUrl)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView)
    }

    fun loadImage(imageView: ImageView, drawable: Drawable?) {
        Glide.with(imageView.context)
                .load(drawable)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView)
    }

    fun loadGifOneCycle(imageView: ImageView,
                        @RawRes gifRawId: Int?,
                        defaultDrawable: Drawable?,
                        onLoadFailed: () -> Unit = {},
                        onAnimationEnd: () -> Unit) {
        loadGifImage(imageView = imageView,
                gifRawId = gifRawId,
                defaultDrawable = defaultDrawable,
                loopCount = 1,
                onLoadFailed = onLoadFailed,
                onAnimationEnd = onAnimationEnd)
    }

    private fun loadGifImage(imageView: ImageView,
                             @RawRes gifRawId: Int?,
                             defaultDrawable: Drawable?,
                             loopCount: Int,
                             onLoadFailed: () -> Unit = {},
                             onAnimationEnd: () -> Unit) {
        Glide.with(imageView.context)
                .asGif()
                .load(gifRawId)
                .listener(object : RequestListener<GifDrawable?> {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<GifDrawable?>?, isFirstResource: Boolean): Boolean {
                        onLoadFailed()
                        return false
                    }

                    override fun onResourceReady(resource: GifDrawable?, model: Any?, target: Target<GifDrawable?>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        resource?.setLoopCount(loopCount)
                        resource?.registerAnimationCallback(object : Animatable2Compat.AnimationCallback() {
                            override fun onAnimationEnd(drawable: Drawable) {
                                onAnimationEnd()
                            }
                        })
                        return false
                    }
                })
                .error(defaultDrawable)
                .into(imageView)
    }

    fun clearImage(imageView: ImageView) {
        if (imageView.context.asActivity()?.isAfterOnSaveInstanceState() ?: false) return
        
        Glide.with(context)
                .clear(imageView)
    }

    fun onDestroy() {
        disposables.clear()
    }

}