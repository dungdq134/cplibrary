package pl.cyfrowypolsat.cpplayercore.mobile.exo

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import androidx.core.view.GestureDetectorCompat
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerControlView
import androidx.media3.ui.PlayerView
import pl.cyfrowypolsat.cpcommon.core.extensions.dp
import pl.cyfrowypolsat.cpcommon.presentation.extensions.setMarginBottom
import pl.cyfrowypolsat.cpplayercore.databinding.ExoPlayerViewBinding

class ExoPlayerView
@JvmOverloads constructor(context: Context,
                          attrs: AttributeSet? = null,
                          defStyleAttr: Int = 0) : PlayerView(context, attrs, defStyleAttr),
        ScaleGestureDetector.OnScaleGestureListener {

    val binding = ExoPlayerViewBinding.bind(this)

    private var scaleFactor = 0f
    private var scaleGestureDetector = ScaleGestureDetector(context, this)

    private val gestureDetector = GestureDetectorCompat(context, object : GestureDetector.SimpleOnGestureListener() {
        override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
            return performClick()
        }

        override fun onDoubleTap(e: MotionEvent): Boolean {
            binding.doubleTapOverlay.onDoubleTap(e.x, e.y)
            return true
        }
    })

    var additionalVisibilityListener: PlayerControlView.VisibilityListener? = null

    init {
        setControllerVisibilityListener(object : ControllerVisibilityListener {
            override fun onVisibilityChanged(visibility: Int) {
                setSubtitlesMargin()
                additionalVisibilityListener?.onVisibilityChange(visibility)
            }
        })
        binding.exoSubtitles.setMarginBottom(20.dp)
        binding.doubleTapOverlay.playerControlView = binding.exoController
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(ev: MotionEvent): Boolean {
        scaleGestureDetector.onTouchEvent(ev)
        gestureDetector.onTouchEvent(ev)
        return true
    }

    override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
        return true
    }

    override fun onScaleEnd(detector: ScaleGestureDetector) {
        if (scaleFactor > 1) {
            resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
        } else {
            resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
        }
    }

    override fun onScale(detector: ScaleGestureDetector): Boolean {
        scaleFactor = detector.scaleFactor
        return true
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        setSubtitlesMargin()
    }

    private fun setSubtitlesMargin() {
        if (isControllerFullyVisible) {
            val bottomMargin = if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                140.dp
            } else {
                112.dp
            }
            binding.exoSubtitles.setMarginBottom(bottomMargin)
        } else {
            binding.exoSubtitles.setMarginBottom(20.dp)
        }
    }

}