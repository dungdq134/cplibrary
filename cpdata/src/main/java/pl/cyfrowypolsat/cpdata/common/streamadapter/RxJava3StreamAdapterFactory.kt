package pl.cyfrowypolsat.cpdata.common.streamadapter

import com.tinder.scarlet.Stream
import com.tinder.scarlet.StreamAdapter
import com.tinder.scarlet.utils.getRawType
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import java.lang.reflect.Type

/**
 * A [stream adapter factory][StreamAdapter.Factory] that uses RxJava3.
 */
class RxJava3StreamAdapterFactory : StreamAdapter.Factory {

    override fun create(type: Type): StreamAdapter<Any, Any> = when (type.getRawType()) {
        Flowable::class.java -> FlowableStreamAdapter()
        Observable::class.java -> ObservableStreamAdapter()
        else -> throw IllegalArgumentException("$type is not supported by this StreamAdapterFactory")
    }
}

class ObservableStreamAdapter<T : Any> : StreamAdapter<T, Observable<T>> {

    override fun adapt(stream: Stream<T>): Observable<T> = Observable.fromPublisher(stream)
}

class FlowableStreamAdapter<T : Any> : StreamAdapter<T, Flowable<T>> {

    override fun adapt(stream: Stream<T>): Flowable<T> = Flowable.fromPublisher(stream)
}