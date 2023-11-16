package pl.cyfrowypolsat.cpplayercore.mobile.lock

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.FrameLayout
import androidx.core.view.GestureDetectorCompat

class LockTouchView
@JvmOverloads constructor(context: Context,
                          attrs: AttributeSet? = null,
                          defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {


    var isTouchForwardingLocked = false
    var onSingleTap: () -> Unit = {}

    private val gestureDetector = GestureDetectorCompat(context, object : GestureDetector.SimpleOnGestureListener() {
        override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
            return performClick()
        }
    })

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(ev: MotionEvent): Boolean {
        if (isTouchForwardingLocked.not()) return false
        gestureDetector.onTouchEvent(ev)
        return true
    }

    override fun performClick(): Boolean {
        super.performClick()
        onSingleTap()
        return true
    }
}