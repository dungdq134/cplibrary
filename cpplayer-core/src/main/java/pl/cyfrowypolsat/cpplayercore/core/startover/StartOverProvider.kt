package pl.cyfrowypolsat.cpplayercore.core.startover

import io.reactivex.rxjava3.core.Observable
import java.util.*

interface StartOverProvider {

    fun provideStartOverDate(): Observable<Date>

}