package pl.cyfrowypolsat.cpcommon.presentation.extensions

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.ViewPropertyAnimator
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import androidx.annotation.DimenRes
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.marginBottom
import androidx.core.view.updateLayoutParams

fun View.requestFocusIfShown() {
    if (isShown) {
        requestFocus()
    }
}

fun View.adjustVisibleOrGone(predicate: () -> Boolean) {
    adjustGoneOrVisible { !predicate() }
}


fun View.adjustVisibleOrGone(state: Boolean) {
    adjustGoneOrVisible { !state }
}

fun View.adjustGoneOrVisible(predicate: () -> Boolean) {
    if (predicate()) {
        gone()
    } else {
        visible()
    }
}

fun View.adjustVisibleOrInvisible(predicate: () -> Boolean) {
    adjustInvisibleOrVisible { !predicate() }
}

fun View.adjustInvisibleOrVisible(predicate: () -> Boolean) {
    if (predicate()) {
        invisible()
    } else {
        visible()
    }
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.focusable() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        this.focusable = View.FOCUSABLE
    } else {
        this.isFocusable = true
    }
}

fun View.notFocusable() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        this.focusable = View.NOT_FOCUSABLE
    } else {
        this.isFocusable = false
    }
}

fun View.deactivate() {
    isActivated = false
}

fun View.enable() {
    isEnabled = true
}

fun View.disable() {
    isEnabled = false
}

fun View.blockNextFocusUp() {
    nextFocusUpId = id
}

fun View.blockNextFocusDown() {
    nextFocusDownId = id
}

fun View.blockNextFocusLeft() {
    nextFocusLeftId = id
}

fun View.blockNextFocusRight() {
    nextFocusRightId = id
}

inline fun View.afterMeasured(crossinline f: View.() -> Unit) {
    viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            if (measuredHeight > 0 && measuredWidth > 0) {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                f()
            }
        }
    })
}

fun View.setMarginTopWithInset(insets: WindowInsetsCompat, @DimenRes res: Int) = updateLayoutParams<ViewGroup.MarginLayoutParams> {
    topMargin = insets.systemWindowInsetTop + resources.getDimension(res).toInt()
}

fun View.setMarginTop(value: Int) = updateLayoutParams<ViewGroup.MarginLayoutParams> {
    topMargin = value
}

fun View.setMarginBottom(value: Int) = updateLayoutParams<ViewGroup.MarginLayoutParams> {
    bottomMargin = value
}

fun View.setMarginEnd(value: Int) = updateLayoutParams<ViewGroup.MarginLayoutParams> {
    marginEnd = value
}

fun View.setPaddingTop(value: Int) = setPadding(paddingLeft, value, paddingRight, paddingBottom)

fun View.setPaddingBottom(value: Int) = setPadding(paddingLeft, paddingTop, paddingRight, value)

fun View.setPaddingLeft(value: Int) = setPadding(value, paddingTop, paddingRight, paddingBottom)

fun View.setPaddingRight(value: Int) = setPadding(paddingLeft, paddingTop, value, paddingBottom)


fun View.changeHeight(height: Int) {
    changeSize(layoutParams.width, height)
}

fun View.changeWidth(width: Int) {
    changeSize(width, layoutParams.height)
}

fun View.changeSize(width: Int, height: Int) {
    val params: ViewGroup.LayoutParams = this.layoutParams
    params.width = width
    params.height = height
    this.layoutParams = params
}

fun View.fadeOut(duration: Long,
                 onAnimationEnd: (animation: Animator) -> Unit): ViewPropertyAnimator {
    return fadeOut(duration)
            .setOnAnimationEndListener(onAnimationEnd)
}

fun View.fadeOut(duration: Long): ViewPropertyAnimator {
    return animate()
            .alpha(0f)
            .setDuration(duration)
}

fun View.visibleAndFadeIn(duration: Long): ViewPropertyAnimator {
    visible()
    return fadeIn(duration)
}

fun View.visibleAndFadeIn(duration: Long,
                          onAnimationEnd: (animation: Animator) -> Unit): ViewPropertyAnimator {
    visible()
    return fadeIn(duration, onAnimationEnd)
}

fun View.fadeIn(duration: Long,
                onAnimationEnd: (animation: Animator) -> Unit): ViewPropertyAnimator {
    return fadeIn(duration)
            .setOnAnimationEndListener(onAnimationEnd)
}

fun View.fadeIn(duration: Long): ViewPropertyAnimator {
    return animate()
            .alpha(1f)
            .setDuration(duration)
}

fun View.fadeOutAndGone(duration: Long): ViewPropertyAnimator {
    return fadeOut(duration).withEndAction { gone() }
}

fun View.setMarginBottomWithAnim(value: Int, duration: Long) {
    animateLayoutParamsChange<ViewGroup.MarginLayoutParams>(startValue = marginBottom, endValue = value, duration = duration) {
        bottomMargin = it
    }
}

inline fun <reified T : ViewGroup.LayoutParams> View.animateLayoutParamsChange(startValue: Int,
                                                                               endValue: Int,
                                                                               duration: Long,
                                                                               crossinline block: T.(animatedValue: Int) -> Unit) {
    val params = layoutParams as T
    val animator = ValueAnimator.ofInt(startValue, endValue)
    animator.addUpdateListener { valueAnimator ->
        block(params, valueAnimator.animatedValue as Int)
        requestLayout()
    }
    animator.duration = duration
    animator.start()
}

fun View.hideSoftKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager ?: return
    if (!imm.isActive) return
    imm.hideSoftInputFromWindow(windowToken, 0)
}
