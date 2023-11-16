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
import pl.cyfrowypolsat.cpplayercore.databinding.CpplCrMobileViewSeeAlsoPosterItemBinding

class PosterSeeAlsoItemViewHolder(parent: ViewGroup,
                                  seeAlsoItemClickListener: SeeAlsoItemClickListener) :
        BaseSeeAlsoItemViewHolder(parent, R.layout.cppl_cr_mobile_view_see_also_poster_item, seeAlsoItemClickListener) {

    private val binding = CpplCrMobileViewSeeAlsoPosterItemBinding.bind(itemView)

    override fun bind(seeAlsoItem: SeeAlsoItem, showAutoPlay: Boolean) {
        super.bind(seeAlsoItem, showAutoPlay)

        val images = seeAlsoItem.posters

        if (images.isNotEmpty()) {
            imageLoader.loadImage(imageSources = images,
                    imageSize = ImageSize.posterSize(EXPECTED_POSTER_IMAGE_WIDTH),
                    imageView = binding.posterSeeAlsoItemImage,
                    placeHolderId = ImageLoader.mobilePlaceHolderId())
        } else {
            imageLoader.clearImage(binding.posterSeeAlsoItemImage)
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

        binding.posterSeeAlsoItemAgeRestriction.setImageDrawable(getAgeGroupDrawable(itemView.context, seeAlsoItem.ageGroup))
        setAgeGroupContentDescription(binding.posterSeeAlsoItemAgeRestriction, seeAlsoItem.ageGroup)

        binding.posterSeeAlsoItemMediaBadges.setMediaBadges(seeAlsoItem.mediaBadges)

        binding.posterSeeAlsoItemContainer.contentDescription = seeAlsoItem.title
    }

    private fun hideElementsOnThumbnail() {
        binding.posterSeeAlsoItemGradient.gone()
        binding.posterSeeAlsoItemTitleOn.gone()
    }

    private fun hideElementsBelowThumbnail() {
        binding.posterSeeAlsoItemTitleBelow.gone()
    }

    private fun showElementsOnThumbnail(seeAlsoItem: SeeAlsoItem) {
        binding.posterSeeAlsoItemGradient.visible()
        binding.posterSeeAlsoItemTitleOn.visible()
        binding.posterSeeAlsoItemTitleOn.text = seeAlsoItem.title
    }

    private fun showElementsBelowThumbnail(seeAlsoItem: SeeAlsoItem) {
        binding.posterSeeAlsoItemTitleBelow.visible()
        binding.posterSeeAlsoItemTitleBelow.text = seeAlsoItem.title
    }

    override fun showAutoPlayProgress() {
        super.setupAutoPlayProgress(binding.posterSeeAlsoAutoPlayProgress, binding.posterSeeAlsoAutoPlayIcon)
    }

    override fun hideAutoPlayProgress() {
        super.hideAutoPlayProgress()
        binding.posterSeeAlsoAutoPlayProgress.gone()
        binding.posterSeeAlsoAutoPlayIcon.gone()
    }
}