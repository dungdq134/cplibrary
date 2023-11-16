package pl.cyfrowypolsat.cpstats.application.gemiusprism

import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import pl.cyfrowypolsat.cpstats.application.*
import timber.log.Timber
import java.io.IOException
import java.util.concurrent.TimeUnit
private const val TAG = "GemiusPrism"

class GemiusPrismHandler(private val gemiusPrismConfig: GemiusPrismConfig) : ApplicationEventHandler {

    private val okHttpClient: OkHttpClient = createOkHttpClient()
    private val mapper: GemiusPrismMapper = GemiusPrismMapper(gemiusPrismConfig)

    override fun handleEvent(event: ApplicationEvent) {
        val gemiusPrismHit = mapper.map(event)

        gemiusPrismHit?.let {
            val request = buildRequest(it.toUrl())
            sendRequest(request)
        }
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