package pl.cyfrowypolsat.cpstats.application.cpactivityevents

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.logging.HttpLoggingInterceptor
import pl.cyfrowypolsat.cpstats.application.ApplicationEvent
import pl.cyfrowypolsat.cpstats.application.ApplicationEventHandler
import pl.cyfrowypolsat.cpstats.core.json.UTCDateSerializer
import timber.log.Timber
import java.io.IOException
import java.util.*
import java.util.concurrent.TimeUnit

private const val TAG = "CPActivityEvents"

class CPActivityEventsHandler(private val config: CPActivityEventsConfig) : ApplicationEventHandler {

    private val okHttpClient: OkHttpClient = createOkHttpClient()
    private val gson = createGson()
    private val mapper: CPActivityEventsMapper = CPActivityEventsMapper(config)

    private var failedEventList = mutableListOf<ApplicationEvent>()

    fun updateAuthToken(authToken: String) {
        mapper.authToken = authToken
        if (failedEventList.isNullOrEmpty()) {
            return
        }

        retryFailedEvents()
    }

    override fun handleEvent(event: ApplicationEvent) {
        val appEventsHit = mapper.map(event)

        appEventsHit?.let {
            val request = buildRequest(gson.toJson(appEventsHit))
            sendRequest(request, object:CPActivityEventsUnauthorizedCallback{
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

    private fun retryFailedEvents() {
        val list = failedEventList.toList()
        failedEventList.clear()
        for (event in list) {
            val appEventsHit = mapper.map(event)

            appEventsHit?.let {
                val request = buildRequest(gson.toJson(appEventsHit))
                sendRequest(request, null)
            } ?: Timber.tag(TAG).e("Event ${event.eventType.name} is not supported")
        }
    }

    private fun sendRequest(request: Request,
                            unauthorizedCallback: CPActivityEventsUnauthorizedCallback?) {
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