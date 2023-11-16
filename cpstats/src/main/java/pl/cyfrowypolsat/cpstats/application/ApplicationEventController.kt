package pl.cyfrowypolsat.cpstats.application

import android.annotation.SuppressLint
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber

class ApplicationEventController(private val eventHandlerList: List<ApplicationEventHandler>) {

    private val thread = Schedulers.single()

    @SuppressLint("CheckResult")
    fun handleEvent(applicationEvent: ApplicationEvent) {
        Observable.fromCallable { eventHandlerList.forEach { it.handleEvent(applicationEvent) } }
                .subscribeOn(thread)
                .subscribe({ }, Timber::e)
    }

}