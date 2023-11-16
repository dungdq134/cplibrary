package pl.cyfrowypolsat.cpchromecast.domain.mapper

import pl.cyfrowypolsat.cpchromecast.data.model.received.ReceiverEnvironment
import pl.cyfrowypolsat.cpchromecast.domain.model.receiver.ReceiverInfo
import javax.inject.Inject

class ReceiverInfoMapper @Inject constructor() {

    fun map(receiverEnvironment: ReceiverEnvironment): ReceiverInfo {
        return if (receiverEnvironment.data?.isStableEnvironment == null) {
            ReceiverInfo(false)
        } else {
            ReceiverInfo(receiverEnvironment.data.isStableEnvironment)
        }
    }
}