package pl.cyfrowypolsat.cpplayercore.mobile.seealso.adapter

import android.view.ViewGroup
import pl.cyfrowypolsat.cpcommon.domain.model.image.ImageSize
import pl.cyfrowypolsat.cpcommon.presentation.extensions.gone
import pl.cyfrowypolsat.cpcommon.presentation.extensions.visible
import pl.cyfrowypolsat.cpcommon.presentation.imageloader.ImageLoader
import pl.cyfrowypolsat.cpcommon.presentation.utils.getAgeGroupDrawable
import pl.cyfrowypolsat.cpcommon.presentation.utils.setAgeGroupContentDescription
import pl.cyfrowypolsat.cpplayercore.R
import pl.cyfrowypolsat.cpplayercore.core.seealso.ListElementTitlePosition
import pl.cyfrowypolsat.cpplayercore.core.seealso.SeeAlsoItem
import pl.cyfrowypolsat.cpplayercore.databinding.CpplCrMobileViewSeeAlsoThumbnailItemBinding

class ThumbnailSeeAlsoItemViewHolder(parent: ViewGroup,
                                     seeAlsoItemClickListener: SeeAlsoItemClickListener) :
        BaseSeeAlsoItemViewHolder(parent, R.layout.cppl_cr_mobile_view_see_also_thumbnail_item, seeAlsoItemClickListener) {

    private val binding = CpplCrMobileViewSeeAlsoThumbnailItemBinding.bind(itemView)

    override fun bind(seeAlsoItem: SeeAlsoItem, showAutoPlay: Boolean) {
        super.bind(seeAlsoItem, showAutoPlay)

        val images = seeAlsoItem.thumbnails

        if (images.isNotEmpty()) {
            imageLoader.loadImage(imageSources = images,
                    imageSize = ImageSize.thumbnailSize(EXPECTED_THUMBNAIL_IMAGE_WIDTH),
                    imageView = binding.thumbnailSeeAlsoItemImage,
                    placeHolderId = ImageLoader.mobilePlaceHolderId())
        } else {
            imageLoader.clearImage(binding.thumbnailSeeAlsoItemImage)
        }

        when (seeAlsoItem.titlePosition) {
            ListElementTitlePosition.HIDDEN -> {
                hideElementsBelowThumbnail()
                hideElementsOnThumbnail()
            }
            ListElementTitlePosition.ON -> {
                hideElementsBelowThumbnail()
                showElementsOnThumbnail(seeAlsoItem)
            }
            else -> {
                showElementsBelowThumbnail(seeAlsoItem)
                hideElementsOnThumbnail()
            }
        }

        binding.thumbnailSeeAlsoItemAgeRestriction.setImageDrawable(getAgeGroupDrawable(itemView.context, seeAlsoItem.ageGroup))
        setAgeGroupContentDescription(binding.thumbnailSeeAlsoItemAgeRestriction, seeAlsoItem.ageGroup)

        binding.thumbnailSeeAlsoItemMediaBadges.setMediaBadges(seeAlsoItem.mediaBadges)

        binding.thumbnailSeeAlsoItemContainer.contentDescription = seeAlsoItem.title
    }

    private fun hideElementsOnThumbnail() {
        binding.thumbnailSeeAlsoItemGradient.gone()
        binding.thumbnailSeeAlsoItemTitleOn.gone()
    }

    private fun hideElementsBelowThumbnail() {
        binding.thumbnailSeeAlsoItemTitleBelow.gone()
    }

    private fun showElementsOnThumbnail(seeAlsoItem: SeeAlsoItem) {
        binding.thumbnailSeeAlsoItemGradient.visible()
        binding.thumbnailSeeAlsoItemTitleOn.visible()
        binding.thumbnailSeeAlsoItemTitleOn.text = seeAlsoItem.title
    }

    private fun showElementsBelowThumbnail(seeAlsoItem: SeeAlsoItem) {
        binding.thumbnailSeeAlsoItemTitleBelow.visible()
        binding.thumbnailSeeAlsoItemTitleBelow.text = seeAlsoItem.title
    }

    override fun showAutoPlayProgress() {
        super.setupAutoPlayProgress(binding.thumbnailSeeAlsoAutoPlayProgress, binding.thumbnailSeeAlsoAutoPlayIcon)
    }

    override fun hideAutoPlayProgress() {
        super.hideAutoPlayProgress()
        binding.thumbnailSeeAlsoAutoPlayProgress.gone()
        binding.thumbnailSeeAlsoAutoPlayIcon.gone()
    }
}