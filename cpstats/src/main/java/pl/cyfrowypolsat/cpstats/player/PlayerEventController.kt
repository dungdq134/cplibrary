package pl.cyfrowypolsat.cpstats.player

import android.annotation.SuppressLint
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber

class PlayerEventController(private val eventHandlerList: List<PlayerEventHandler>) {

    private val thread = Schedulers.single()

    @SuppressLint("CheckResult")
    fun handleEvent(playerEvent: PlayerEvent) {
        Observable.fromCallable { eventHandlerList.forEach { it.handleEvent(playerEvent) } }
                .subscribeOn(thread)
                .subscribe({ }, Timber::e)
    }

}