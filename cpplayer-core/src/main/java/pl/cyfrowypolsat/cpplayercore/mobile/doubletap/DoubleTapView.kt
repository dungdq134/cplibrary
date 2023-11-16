package pl.cyfrowypolsat.cpplayercore.mobile.doubletap

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.annotation.StringRes
import pl.cyfrowypolsat.cpcommon.presentation.extensions.invisible
import pl.cyfrowypolsat.cpcommon.presentation.extensions.visible
import pl.cyfrowypolsat.cpplayercore.R
import pl.cyfrowypolsat.cpplayercore.databinding.CpplCrMobileDoubleTapViewBinding


class DoubleTapView
@JvmOverloads constructor(context: Context,
                          attrs: AttributeSet? = null,
                          defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {

    companion object {
        private const val INCREMENT_SECONDS = 10
        private const val DOUBLE_TAP_DELAY = 650L
    }

    val binding = CpplCrMobileDoubleTapViewBinding.inflate(LayoutInflater.from(context), this)

    private var isDoubleTapping = false
    private var seconds = INCREMENT_SECONDS

    @StringRes
    var stringResourceId: Int = R.string.cppl_cr_double_tap_forward

    private val runnable = Runnable {
        isDoubleTapping = false
        binding.doubleTapRipple.isPressed = false
        binding.doubleTapText.invisible()
    }

    fun onDoubleTap() {
        if (isDoubleTapping) {
            seconds += INCREMENT_SECONDS
            removeCallbacks(runnable)
            binding.doubleTapRipple.isPressed = false
            binding.doubleTapRipple.isPressed = true
            postOnAnimationDelayed(runnable, DOUBLE_TAP_DELAY)
        } else {
            seconds = INCREMENT_SECONDS
            isDoubleTapping = true
            binding.doubleTapText.visible()
            binding.doubleTapRipple.isPressed = true
            postOnAnimationDelayed(runnable, DOUBLE_TAP_DELAY)
        }

        binding.doubleTapText.text = resources.getString(stringResourceId).format(seconds)
    }

}