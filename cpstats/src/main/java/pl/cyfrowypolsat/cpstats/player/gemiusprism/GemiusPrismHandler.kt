package pl.cyfrowypolsat.cpstats.player.gemiusprism

import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import pl.cyfrowypolsat.cpstats.core.ApplicationDataProvider
import pl.cyfrowypolsat.cpstats.core.model.AdvertBlockType
import pl.cyfrowypolsat.cpstats.player.PlayerAdvertEvent
import pl.cyfrowypolsat.cpstats.player.EventType
import pl.cyfrowypolsat.cpstats.player.PlayerEvent
import pl.cyfrowypolsat.cpstats.player.PlayerEventHandler
import timber.log.Timber
import java.io.IOException
import java.util.*
import java.util.concurrent.TimeUnit

private const val TAG = "GemiusPrism"

class GemiusPrismHandler(private val gemiusPrismConfig: GemiusPrismConfig,
                         private val applicationDataProvider: ApplicationDataProvider) : PlayerEventHandler {

    private val okHttpClient: OkHttpClient = createOkHttpClient()
    private val updatePositionIntervalMs = gemiusPrismConfig.intervalMs
    private val mapper: GemiusPrismMapper = GemiusPrismMapper(gemiusPrismConfig, applicationDataProvider)
    private var lastCycleHitTime: Date = Date()
    private var contactHitWasSent: Boolean = false

    override fun handleEvent(event: PlayerEvent) {
        if (event.eventType == EventType.PLAYBACK_POSITION_UPDATED) {
            if (!canSendCycleHit()) {
                return
            }
            resetLastCycleHitTime()
        }

        if (event.eventType == EventType.PLAYBACK_STARTED || event.eventType == EventType.PLAYBACK_UNPAUSED) {
            resetLastCycleHitTime()
        }

        if (shouldSendContactHit(event)) {
            sendContactHit(event)
            contactHitWasSent = true
        }

        val gemiusPrismHit = mapper.map(event)
        gemiusPrismHit?.let {
            val request = buildRequest(it.toUrl())
            sendRequest(request)
        }
    }

    private fun canSendCycleHit(): Boolean {
        val result = (Date().time - lastCycleHitTime.time >= updatePositionIntervalMs)
        return result
    }

    private fun resetLastCycleHitTime() {
        lastCycleHitTime = Date()
    }

    private fun shouldSendContactHit(event: PlayerEvent): Boolean {
        val isPlaybackBeginHit = event.eventType == EventType.PLAYBACK_STARTED
        val isPrerollBeginHit = event.eventType == EventType.ADVERT_BLOCK_STARTED && event is PlayerAdvertEvent && event.advertBlockData.blockType == AdvertBlockType.PREROLL
        if (!contactHitWasSent && isPlaybackBeginHit || isPrerollBeginHit) {
            return true
        }
        return false
    }

    private fun sendContactHit(event: PlayerEvent) {
        val gemiusPrismHit = mapper.mapToContactHit(event)
        gemiusPrismHit?.let {
            val request = buildRequest(it.toUrl())
            sendRequest(request)
        } ?: Timber.tag(TAG).e("Event ${event.eventType.name} is not valid for contact hit")

    }

    private fun buildRequest(params: String): Request {
        val serviceUrl = if (gemiusPrismConfig.service.endsWith("/")) gemiusPrismConfig.service else "${gemiusPrismConfig.service}/"
        val url = "$serviceUrl$params"
        val builder = Request.Builder()
                .url(url).get()
        builder.addHeader("User-Agent", gemiusPrismConfig.userAgent)
        return builder.build()
    }

    private fun sendRequest(request: Request) {
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call,
                                   e: IOException) {
            }

            override fun onResponse(call: Call,
                                    response: Response) {
            }
        })
    }

    private fun createOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor { message -> Timber.tag(TAG).d(message) }
        logging.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .build()
    }
}