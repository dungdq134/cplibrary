package pl.cyfrowypolsat.cpstats.player.cpplayerevents

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.logging.HttpLoggingInterceptor
import pl.cyfrowypolsat.cpstats.core.ApplicationDataProvider
import pl.cyfrowypolsat.cpstats.core.json.UTCDateSerializer
import pl.cyfrowypolsat.cpstats.player.EventType
import pl.cyfrowypolsat.cpstats.player.PlayerEvent
import pl.cyfrowypolsat.cpstats.player.PlayerEventHandler
import timber.log.Timber
import java.io.IOException
import java.util.*
import java.util.concurrent.TimeUnit

private const val TAG = "CPPlayerEvents"

class PlayerEventsHandler(private val config: CPPlayerEventsConfig,
                          private val applicationDataProvider: ApplicationDataProvider) : PlayerEventHandler {

    private val okHttpClient: OkHttpClient = createOkHttpClient()
    private val gson = createGson()
    private val mapper: CPPlayerEventsMapper = CPPlayerEventsMapper(config, applicationDataProvider)
    private var lastCycleHitTime: Date = Date()
    private val updatePositionIntervalMs = config.intervalMs

    private var failedEventList = mutableListOf<PlayerEvent>()

    fun updateAuthToken(authToken: String) {
        mapper.authToken = authToken
        if (failedEventList.isNullOrEmpty()) {
            return
        }

        retryFailedEvents()
    }

    override fun handleEvent(event: PlayerEvent) {
        if (event.eventType == EventType.PLAYBACK_POSITION_UPDATED) {
            if (!canSendCycleHit() || event.playerData.isPlayingAdvert) {
                return
            }
            resetLastCycleHitTime()
        }

        if (event.eventType == EventType.PLAYBACK_STARTED || event.eventType == EventType.PLAYBACK_UNPAUSED) {
            resetLastCycleHitTime()
        }

        val playerEventsHit = mapper.map(event)

        playerEventsHit?.let {
            val request = buildRequest(gson.toJson(playerEventsHit))
            sendRequest(request, object : CPPlayerEventsUnauthorizedCallback {
                override fun onUnauthorizedException() {
                    try {
                        failedEventList.add(event)
                        config.unauthorizedCallback.onUnauthorizedException()
                    } catch (t: Throwable) {
                        t.printStackTrace()
                    }
                }
            })
        }
    }

    private fun buildRequest(jsonData: String): Request {
        val body = RequestBody.create("application/json; charset=utf-8".toMediaTypeOrNull(), jsonData)
        val builder = Request.Builder()
                .url(config.service)
                .post(body)
        builder.addHeader("User-Agent", config.userAgent)
        return builder.build()
    }

    private fun canSendCycleHit(): Boolean {
        val result = (Date().time - lastCycleHitTime.time >= updatePositionIntervalMs)
        return result
    }

    private fun resetLastCycleHitTime() {
        lastCycleHitTime = Date()
    }

    private fun retryFailedEvents() {
        val list = failedEventList.toList()
        failedEventList.clear()
        for (event in list) {
            val playerEventsHit = mapper.map(event)

            playerEventsHit?.let {
                val request = buildRequest(gson.toJson(playerEventsHit))
                sendRequest(request, null)
            } ?: Timber.tag(TAG).e("Event ${event.eventType.name} is not supported")
        }
    }

    private fun sendRequest(request: Request,
                            unauthorizedCallback: CPPlayerEventsUnauthorizedCallback?) {
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call,
                                   e: IOException) {
            }

            override fun onResponse(call: Call,
                                    response: Response) {
                if (response.code == 401) {
                    unauthorizedCallback?.onUnauthorizedException()
                }
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

    private fun createGson(): Gson {
        return GsonBuilder().registerTypeAdapter(Date::class.java, UTCDateSerializer()).create()
    }
}