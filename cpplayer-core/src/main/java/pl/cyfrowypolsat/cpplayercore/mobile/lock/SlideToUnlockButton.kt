package pl.cyfrowypolsat.cpplayercore.mobile.lock

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.ViewTreeObserver
import android.view.animation.DecelerateInterpolator
import android.widget.SeekBar
import androidx.appcompat.widget.AppCompatSeekBar
import androidx.core.animation.doOnEnd
import pl.cyfrowypolsat.cpcommon.core.extensions.percentFirstOfSecond
import kotlin.math.abs


class SlideToUnlockButton
@JvmOverloads constructor(context: Context,
                          attrs: AttributeSet? = null,
                          defStyleAttr: Int = 0) : AppCompatSeekBar(context, attrs, defStyleAttr), ViewTreeObserver.OnPreDrawListener {

    var onUnlock: () -> Unit = { }
    var onSlidingStart: () -> Unit = { }
    var onSlidingCancel: () -> Unit = { }
    var onProgressReset: () -> Unit = { }

    private var userTapAcceptedOffset = 10
    private var startSlidingProgress = 0
    private var isUserSliding = false

    init {
        viewTreeObserver.addOnPreDrawListener(this)
        setupOnSeekBarChangeListener()
    }

    override fun onPreDraw(): Boolean {
        userTapAcceptedOffset = percentFirstOfSecond((height / 1.7f), width.toFloat())
        return true
    }

    override fun setOnSeekBarChangeListener(l: OnSeekBarChangeListener?) {

    }

    private fun setupOnSeekBarChangeListener() {
        super.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                startSlidingProgress = progress
            }

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                blockProgressChangeByUserTap(fromUser)
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                handleUserStopSliding()
            }
        })
    }

    private fun blockProgressChangeByUserTap(fromUser: Boolean) {
        if (fromUser.not()) return
        if (startSlidingProgress == progress) return
        if (isUserSliding) return

        if (abs(startSlidingProgress - progress) > userTapAcceptedOffset) {
            progress = startSlidingProgress
        } else {
            isUserSliding = true
            onSlidingStart()
        }
    }

    private fun handleUserStopSliding() {
        if (isUserSliding.not()) return
        if (progress < 100) {
            resetProgress()
            onSlidingCancel()
        } else {
            onUnlock()
        }
        isUserSliding = false
    }

    private fun resetProgress() {
        val initProgress = progress
        val anim = ValueAnimator.ofInt(minOf(5, initProgress), initProgress)
        anim.duration = (2 * initProgress).toLong()
        anim.interpolator = DecelerateInterpolator()
        anim.addUpdateListener { animation ->
            val animProgress = animation.animatedValue as Int
            progress = initProgress - animProgress
        }
        anim.doOnEnd { onProgressReset() }
        anim.start()
    }

}