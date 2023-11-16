package pl.cyfrowypolsat.cpplayercore.tv.seealso.adapter

import android.animation.ObjectAnimator
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import androidx.core.animation.doOnEnd
import androidx.leanback.widget.BaseCardView
import pl.cyfrowypolsat.cpcommon.presentation.extensions.gone
import pl.cyfrowypolsat.cpcommon.presentation.extensions.visible
import pl.cyfrowypolsat.cpplayercore.core.seealso.SeeAlsoItem
import pl.cyfrowypolsat.cpplayercore.databinding.CpplCrTvViewSeeAlsoCardBinding

class TvSeeAlsoCardView(context: Context) : BaseCardView(context) {

    companion object {
        const val PROGRESS_MAX = 1000
        const val PROGRESS_DURATION = 10000L
    }

    init {
        isFocusable = true
    }

    private val binding = CpplCrTvViewSeeAlsoCardBinding.inflate(LayoutInflater.from(context), this, true)
    private var progressObjectAnimator: ObjectAnimator? = null

    fun setAutoPlayProgress(seeAlsoItem: SeeAlsoItem,
                            showAutoPlay: Boolean,
                            onAutoPlayProgressEndListener: OnAutoPlayProgressEndListener) {
        if (showAutoPlay) {
            binding.tvSeeAlsoAutoPlayProgress.visible()
            binding.tvSeeAlsoAutoPlayIcon.visible()
            binding.tvSeeAlsoAutoPlayProgress.max = PROGRESS_MAX
            progressObjectAnimator = ObjectAnimator.ofInt(binding.tvSeeAlsoAutoPlayProgress, "progress", 100, PROGRESS_MAX)
                    .setDuration(PROGRESS_DURATION)
            progressObjectAnimator?.addUpdateListener { valueAnimator ->
                val progress = valueAnimator.animatedValue as Int
                binding.tvSeeAlsoAutoPlayProgress.progress = progress
            }
            progressObjectAnimator?.doOnEnd {
                onAutoPlayProgressEndListener.onAutoPlayProgressEnd(seeAlsoItem)
            }
            progressObjectAnimator?.start()
        } else {
            binding.tvSeeAlsoAutoPlayProgress.gone()
            binding.tvSeeAlsoAutoPlayIcon.gone()
        }
    }

    fun stopAutoPlayProgress() {
        progressObjectAnimator?.pause()
        Handler(Looper.getMainLooper()).post {
            binding.tvSeeAlsoAutoPlayProgress.gone()
            binding.tvSeeAlsoAutoPlayIcon.gone()
        }
    }

}