package pl.cyfrowypolsat.cpchromecast.domain.usecase

import com.google.gson.Gson
import pl.cyfrowypolsat.cpchromecast.core.di.CpChromecastQualifier
import pl.cyfrowypolsat.cpchromecast.data.model.received.*
import pl.cyfrowypolsat.cpchromecast.domain.listeners.OnDataReceivedListener
import pl.cyfrowypolsat.cpchromecast.domain.mapper.ChromecastErrorMapper
import pl.cyfrowypolsat.cpchromecast.domain.mapper.ChromecastMediaInfoMapper
import pl.cyfrowypolsat.cpchromecast.domain.mapper.ReceiverInfoMapper
import pl.cyfrowypolsat.cpchromecast.domain.model.ad.AdStateInfo
import timber.log.Timber
import javax.inject.Inject

class GetReceivedMessageUseCase @Inject constructor(@CpChromecastQualifier private val gson: Gson,
                                                    private val chromecastErrorMapper: ChromecastErrorMapper,
                                                    private val chromecastMediaInfoMapper: ChromecastMediaInfoMapper,
                                                    private val receiverInfoMapper: ReceiverInfoMapper) {

    fun getReceivedMessage(message: String, onDataReceivedListener: OnDataReceivedListener){
        when(val message = gson.fromJson<Message>(message, Message::class.java)){
            is Error -> {
                Timber.d("Error $message")
                val chromecastError = chromecastErrorMapper.map(message)
                chromecastError?.let {
                    Timber.d("ChromecastError $it")
                    onDataReceivedListener.onDataReceived(it)
                }
            }
            is PlaybackReady -> {
                Timber.d("PlaybackReady $message")
                val chromecastMediaInfo = chromecastMediaInfoMapper.map(message)
                chromecastMediaInfo?.let {
                    Timber.d("ChromecastMediaInfo $it")
                    onDataReceivedListener.onDataReceived(it)
                }
            }
            is LinearAdStarted -> {
                Timber.d("LinearAdStarted $message")
                onDataReceivedListener.onDataReceived(AdStateInfo(true))
            }
            is LinearAdPodStarted -> {
                Timber.d("LinearAdPodStarted $message")
                onDataReceivedListener.onDataReceived(AdStateInfo(true))
            }
            is LinearAdPodEnded -> {
                Timber.d("LinearAdPodEnded $message")
                onDataReceivedListener.onDataReceived(AdStateInfo(false))
            }
            is ReceiverEnvironment -> {
                Timber.d("ReceiverEnvironment $message")
                val receiverInfo = receiverInfoMapper.map(message)
                onDataReceivedListener.onDataReceived(receiverInfo)
                Timber.d("ReceiverInfo $receiverInfo")
            }
        }
    }
}