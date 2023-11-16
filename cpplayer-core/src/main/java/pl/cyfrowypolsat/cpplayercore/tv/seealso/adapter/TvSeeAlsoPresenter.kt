package pl.cyfrowypolsat.cpplayercore.tv.seealso.adapter

import android.content.Context
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.leanback.widget.BaseCardView
import androidx.leanback.widget.Presenter
import pl.cyfrowypolsat.cpcommon.presentation.imageloader.ImageLoader
import pl.cyfrowypolsat.cpcommon.domain.model.image.ImageSize
import pl.cyfrowypolsat.cpcommon.domain.model.image.ImageSource
import pl.cyfrowypolsat.cpplayercore.R

abstract class TvSeeAlsoPresenter<T : BaseCardView>(val context: Context) : Presenter() {

    companion object {
        const val EXPECTED_POSTER_IMAGE_WIDTH = 400F
        const val EXPECTED_THUMBNAIL_IMAGE_WIDTH = 700F
    }

    private val imageLoader = ImageLoader(context)
    protected val defaultBackgroundColor = ContextCompat.getColor(context, R.color.cppl_cr_tv_default_card_view_background)
    protected val selectedBackgroundColor = ContextCompat.getColor(context, R.color.cppl_cr_color_primary)

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        val cardView = onCreateView()
        return ViewHolder(cardView)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, item: Any) {
        onBindViewHolder(item, viewHolder.view as T)
    }

    override fun onUnbindViewHolder(viewHolder: Presenter.ViewHolder) {
        onUnbindViewHolder(viewHolder.view as T)
    }

    private fun onUnbindViewHolder(cardView: T) {

    }

    protected abstract fun onCreateView(): T

    protected abstract fun onBindViewHolder(item: Any, cardView: T)


    protected fun loadImage(imageSources: List<ImageSource>, imageSize: ImageSize, imageView: ImageView) {
        imageLoader.loadImage(
                imageSources = imageSources,
                imageSize = imageSize,
                imageView = imageView
        )
    }

}
