package pl.cyfrowypolsat.cpchromecast.domain.usecase

import com.google.android.gms.cast.framework.CastState
import pl.cyfrowypolsat.cpchromecast.domain.enums.ChromecastState
import javax.inject.Inject

class GetChromecastStateUseCase @Inject constructor() {

    fun mapChromecastState(castState: Int?, viewError: Throwable?): ChromecastState {
        if(viewError != null) return ChromecastState.UNAVAILABLE
        when (castState) {
            CastState.CONNECTED -> return ChromecastState.CONNECTED
            CastState.CONNECTING -> return ChromecastState.CONNECTING
            CastState.NOT_CONNECTED -> return ChromecastState.NOT_CONNECTED
            CastState.NO_DEVICES_AVAILABLE -> return ChromecastState.UNAVAILABLE
        }
        return ChromecastState.UNAVAILABLE
    }
}