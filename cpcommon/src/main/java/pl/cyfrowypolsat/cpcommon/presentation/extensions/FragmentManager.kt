package pl.cyfrowypolsat.cpcommon.presentation.extensions

import androidx.fragment.app.FragmentManager

fun FragmentManager.isFragmentAttached(tag: String?) : Boolean {
    return findFragmentByTag(tag) != null
}