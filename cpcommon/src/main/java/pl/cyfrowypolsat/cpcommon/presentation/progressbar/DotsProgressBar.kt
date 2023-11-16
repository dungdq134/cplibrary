package pl.cyfrowypolsat.cpcommon.presentation.progressbar

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.forEach
import androidx.core.view.isVisible
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import pl.cyfrowypolsat.cpcommon.R
import pl.cyfrowypolsat.cpcommon.core.rx.RxTransformers
import pl.cyfrowypolsat.cpcommon.databinding.CpcmnCommonViewDotsProgressBarBinding
import pl.cyfrowypolsat.cpcommon.presentation.extensions.*
import java.util.concurrent.TimeUnit


class DotsProgressBar @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    companion object {
        private const val DOTS_NUMBER = 5
        private const val INITIAL_DELAY_TIME = 0L
        private const val TICK_TIME = 350L
        private const val MAX_SCALE = 1.0f
        private const val MIN_SCALE = 0.33f
    }

    private var timerDisposable: Disposable? = null
    private var animatedDot = 0
    private val progressBarManager: ProgressBarManager

    private val binding = CpcmnCommonViewDotsProgressBarBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        val isDelayed = context.retrieveBoolean(attrs, R.styleable.CPCommon_DotsProgressBar, R.styleable.CPCommon_DotsProgressBar_isDelayed, true)
        val descriptionText = context.retrieveString(attrs, R.styleable.CPCommon_DotsProgressBar, R.styleable.CPCommon_DotsProgressBar_description)
        val descriptionTextColor = context.retrieveColor(attrs, R.styleable.CPCommon_DotsProgressBar, R.styleable.CPCommon_DotsProgressBar_descriptionColor, R.color.cpcmn_white_solid)
        val descriptionTypeface = context.retrieveFont(attrs, R.styleable.CPCommon_DotsProgressBar, R.styleable.CPCommon_DotsProgressBar_android_fontFamily)

        binding.viewDotsProgressBarDescription.setTextAndAdjustVisibility(descriptionText)
        binding.viewDotsProgressBarDescription.setTextColor(descriptionTextColor)
        binding.viewDotsProgressBarDescription.typeface = descriptionTypeface
        resetDotsContainer()
        progressBarManager = ProgressBarManager(this, isDelayed, object : ProgressBarManager.Listener {
            override fun show() {
                this@DotsProgressBar.showAndStartAnimation()
            }

            override fun hide() {
                this@DotsProgressBar.hideAndClearAnimation()
            }
        })
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        progressBarManager.onAttachedToWindow()
        if (isVisible) {
            startAnimation()
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        progressBarManager.onDetachedFromWindow()
    }

    override fun setVisibility(visibility: Int) {
        if (visibility == View.VISIBLE) {
            progressBarManager.maybeShow()
        } else {
            progressBarManager.maybeHide()
        }
    }

    private fun showAndStartAnimation() {
        super.setVisibility(View.VISIBLE)
        startAnimation()
    }

    private fun hideAndClearAnimation() {
        super.setVisibility(View.GONE)
        timerDisposable?.dispose()
        clearAllAnimations()
        resetDotsContainer()
        animatedDot = 0
        timerDisposable = null
    }

    fun setDescription(description: String) {
        binding.viewDotsProgressBarDescription.setTextAndAdjustVisibility(description)
    }

    private fun resetDotsContainer() {
        binding.viewDotsProgressBarDotsContainer.removeAllViews()
        repeat(DOTS_NUMBER) {
            val dotView = DotsProgressBarDot(context)
            setMinScale(dotView)
            binding.viewDotsProgressBarDotsContainer.addView(dotView)
        }
    }

    private fun startAnimation() {
        timerDisposable?.let { return }
        this.timerDisposable = Observable.interval(INITIAL_DELAY_TIME, TICK_TIME, TimeUnit.MILLISECONDS)
                .compose(RxTransformers.androidIO())
                .subscribe { scaleDots() }
    }

    private fun scaleDots() {
        val mainDotIndex = animatedDot
        val previousMainDotIndex = previousDotIndex(mainDotIndex)
        val previousPreviousMainDotIndex = previousDotIndex(previousMainDotIndex)

        scaleWithAnim(getDot(mainDotIndex), MAX_SCALE)
        scaleWithAnim(getDot(previousPreviousMainDotIndex), MIN_SCALE)

        animatedDot = nextDotIndex(mainDotIndex)
    }

    private fun nextDotIndex(index: Int): Int {
        return if (index == DOTS_NUMBER - 1) {
            0
        } else {
            index + 1
        }
    }

    private fun previousDotIndex(index: Int): Int {
        return if (index == 0) {
            DOTS_NUMBER - 1
        } else {
            index - 1
        }
    }

    private fun getDot(position: Int): View {
        return binding.viewDotsProgressBarDotsContainer.getChildAt(position)
    }

    private fun scaleWithAnim(view: View, scale: Float) {
        view.animate()
                .scaleX(scale)
                .scaleY(scale)
                .setDuration(2 * TICK_TIME)
                .setListener(null)
    }

    private fun setMinScale(view: View) {
        view.scaleY = MIN_SCALE
        view.scaleX = MIN_SCALE
    }

    private fun clearAllAnimations() {
        binding.viewDotsProgressBarDotsContainer.forEach {
            it.animation?.cancel()
            it.clearAnimation()
        }
    }

}