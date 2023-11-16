package pl.cyfrowypolsat.cpplayercore.tv.seealso.adapter

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import pl.cyfrowypolsat.cpcommon.presentation.extensions.gone
import pl.cyfrowypolsat.cpcommon.presentation.extensions.visible
import pl.cyfrowypolsat.cpcommon.domain.model.image.ImageSize
import pl.cyfrowypolsat.cpplayercore.R
import pl.cyfrowypolsat.cpplayercore.core.seealso.ListElementTitlePosition
import pl.cyfrowypolsat.cpplayercore.core.seealso.SeeAlsoItem
import pl.cyfrowypolsat.cpplayercore.core.seealso.SeeAlsoType

interface OnAutoPlayProgressEndListener {
    fun onAutoPlayProgressEnd(seeAlsoItem: SeeAlsoItem)
}

class TvSeeAlsoCardViewPresenter(context: Context,
                                 private val onAutoPlayProgressEndListener: OnAutoPlayProgressEndListener,
                                 private val itemWithAutoPlay: SeeAlsoItem?) : TvSeeAlsoPresenter<TvSeeAlsoCardView>(context) {

    override fun onCreateView(): TvSeeAlsoCardView {
        return TvSeeAlsoCardView(context)
    }

    override fun onBindViewHolder(item: Any, cardView: TvSeeAlsoCardView) {
        val seeAlsoItem = item as SeeAlsoItem
        setDisplayMode(seeAlsoItem, cardView)
        setListElementTitlePosition(seeAlsoItem, cardView)
        cardView.setAutoPlayProgress(seeAlsoItem = seeAlsoItem,
                showAutoPlay = itemWithAutoPlay == seeAlsoItem,
                onAutoPlayProgressEndListener = onAutoPlayProgressEndListener)
    }

    private fun setDisplayMode(seeAlsoItem: SeeAlsoItem,
                               cardView: TvSeeAlsoCardView) {
        val imageViewContainer = cardView.findViewById<RelativeLayout>(R.id.tv_see_also_image_view_container)
        val imageView = imageViewContainer.findViewById<ImageView>(R.id.tv_see_also_image)
        if (seeAlsoItem.type == SeeAlsoType.MOVIE) {
            loadImage(imageSources = seeAlsoItem.posters,
                    imageSize = ImageSize.posterSize(EXPECTED_POSTER_IMAGE_WIDTH),
                    imageView = imageView)
            setPosterDimensions(imageViewContainer)
        } else {
            loadImage(imageSources = seeAlsoItem.thumbnails,
                    imageSize = ImageSize.thumbnailSize(EXPECTED_THUMBNAIL_IMAGE_WIDTH),
                    imageView = imageView)
            setThumbnailDimensions(imageViewContainer)
        }
    }

    private fun setThumbnailDimensions(imageViewContainer: RelativeLayout) {
        imageViewContainer.layoutParams.width = context.resources.getDimensionPixelSize(R.dimen.cppl_cr_tv_see_also_thumbnail_width)
        imageViewContainer.layoutParams.height = context.resources.getDimensionPixelSize(R.dimen.cppl_cr_tv_see_also_thumbnail_height)
    }

    private fun setPosterDimensions(cardViewContainer: RelativeLayout) {
        cardViewContainer.layoutParams.width = context.resources.getDimensionPixelSize(R.dimen.cppl_cr_tv_see_also_poster_width)
        cardViewContainer.layoutParams.height = context.resources.getDimensionPixelSize(R.dimen.cppl_cr_tv_see_also_poster_height)
    }

    private fun setListElementTitlePosition(seeAlsoItem: SeeAlsoItem,
                                            cardView: TvSeeAlsoCardView) {
        val gradient = cardView.findViewById<View>(R.id.tv_see_also_gradient)
        val titleOn = cardView.findViewById<TextView>(R.id.tv_see_also_title_on)
        val titleBelow = cardView.findViewById<TextView>(R.id.tv_see_also_title_below)

        gradient.gone()
        titleOn.gone()
        titleBelow.gone()

        if (seeAlsoItem.type == SeeAlsoType.MOVIE) {
            gradient.background = ContextCompat.getDrawable(context, R.drawable.tv_gradient_poster)
        } else {
            gradient.background = ContextCompat.getDrawable(context, R.drawable.tv_gradient_thumbnail)
        }

        when (seeAlsoItem.titlePosition) {
            ListElementTitlePosition.ON -> {
                titleOn.text = seeAlsoItem.title
                gradient.visible()
                titleOn.visible()
            }
            ListElementTitlePosition.BELOW -> {
                titleBelow.text = seeAlsoItem.title
                titleBelow.visible()
            }
            ListElementTitlePosition.HIDDEN -> {
            }
        }
    }

}