package pl.cyfrowypolsat.cpcommon.presentation.extensions

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.ViewPropertyAnimator

fun ViewPropertyAnimator.setOnAnimationEndListener(onAnimationEnd: (animation: Animator) -> Unit): ViewPropertyAnimator {
    return setListener(object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator) {
            onAnimationEnd(animation)
        }
    })
}