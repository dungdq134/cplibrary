package pl.cyfrowypolsat.cpplayercore.mobile.seealso.adapter

import android.animation.ObjectAnimator
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.core.animation.doOnEnd
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.progressindicator.CircularProgressIndicator
import pl.cyfrowypolsat.cpcommon.presentation.extensions.visible
import pl.cyfrowypolsat.cpcommon.presentation.imageloader.ImageLoader
import pl.cyfrowypolsat.cpplayercore.core.seealso.SeeAlsoItem

abstract class BaseSeeAlsoItemViewHolder(parent: ViewGroup,
                                         @LayoutRes layoutId: Int,
                                         private val seeAlsoItemClickListener: SeeAlsoItemClickListener)
    : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(layoutId, parent, false)) {

    companion object {
        const val EXPECTED_THUMBNAIL_IMAGE_WIDTH = 700F
        const val EXPECTED_POSTER_IMAGE_WIDTH = 650F
        const val PROGRESS_MAX = 1000
        const val PROGRESS_DURATION = 10000L
    }

    protected val imageLoader = ImageLoader(itemView.context)

    private lateinit var seeAlsoItem: SeeAlsoItem

    private var progressObjectAnimator: ObjectAnimator? = null

    init {
        itemView.setOnClickListener {
            seeAlsoItemClickListener.onSeeAlsoItemClick(seeAlsoItem)
        }
    }

    @CallSuper
    open fun bind(seeAlsoItem: SeeAlsoItem, showAutoPlay: Boolean) {
        this.seeAlsoItem = seeAlsoItem
        if (showAutoPlay) {
            showAutoPlayProgress()
        } else {
            hideAutoPlayProgress()
        }
    }

    open fun showAutoPlayProgress() {}

    protected fun setupAutoPlayProgress(progressView: CircularProgressIndicator,
                                        progressIconView: ImageView) {
        progressView.visible()
        progressIconView.visible()
        progressView.max = PROGRESS_MAX
        progressObjectAnimator = ObjectAnimator.ofInt(progressView, "progress", 100, PROGRESS_MAX)
                .setDuration(PROGRESS_DURATION)
        progressObjectAnimator?.addUpdateListener { valueAnimator ->
            val progress = valueAnimator.animatedValue as Int
            progressView.progress = progress
        }
        progressObjectAnimator?.doOnEnd {
            seeAlsoItemClickListener.onSeeAlsoItemAutoPlay(seeAlsoItem)
        }
        progressObjectAnimator?.start()
    }

    open fun hideAutoPlayProgress() {
        progressObjectAnimator?.pause()
    }

}