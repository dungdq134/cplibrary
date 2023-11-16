package pl.cyfrowypolsat.cpcommon.presentation.extensions

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

fun <T : Any?, L : LiveData<T>> LifecycleOwner.observe(liveData: L, body: (T?) -> Unit) =
        liveData.observe(this, Observer(body))

fun <T : Any, L : LiveData<T>> LifecycleOwner.nonNullObserve(liveData: L, body: (T) -> Unit) =
        liveData.observe(this, Observer { it?.let(body) })
