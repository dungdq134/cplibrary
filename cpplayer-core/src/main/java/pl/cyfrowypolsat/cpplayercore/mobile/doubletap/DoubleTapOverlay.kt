package pl.cyfrowypolsat.cpplayercore.mobile.doubletap

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.media3.common.Player
import pl.cyfrowypolsat.cpplayercore.R
import pl.cyfrowypolsat.cpplayercore.databinding.CpplCrMobileDoubleTapOverlayBinding
import pl.cyfrowypolsat.cpplayercore.mobile.exo.ExoPlayerControlView


class DoubleTapOverlay
@JvmOverloads constructor(context: Context,
                          attrs: AttributeSet? = null,
                          defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {

    val binding = CpplCrMobileDoubleTapOverlayBinding.inflate(LayoutInflater.from(context), this, true)
    var playerControlView: ExoPlayerControlView? = null

    init {
        binding.rewindView.stringResourceId = R.string.cppl_cr_double_tap_rewind
        binding.fastForwardView.stringResourceId = R.string.cppl_cr_double_tap_forward
    }

    fun onDoubleTap(posX: Float, posY: Float) {
        if (playerControlView?.player?.playbackState != Player.STATE_ENDED) {
            playerControlView?.hide()
            if (posX > width * 0.5 && playerControlView?.isFastForwardEnabled() == true) {
                fastForward()
            } else if (posX < width * 0.5 && playerControlView?.isRewindEnabled() == true) {
                rewind()
            }
        }
    }

    private fun fastForward() {
        playerControlView?.player?.play()
        binding.fastForwardView.onDoubleTap()
        playerControlView?.fastForward()
    }

    private fun rewind() {
        playerControlView?.player?.play()
        binding.rewindView.onDoubleTap()
        playerControlView?.rewind()
    }

}