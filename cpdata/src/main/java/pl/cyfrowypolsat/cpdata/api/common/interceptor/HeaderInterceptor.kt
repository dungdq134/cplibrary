package pl.cyfrowypolsat.cpdata.api.common.interceptor

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import pl.cyfrowypolsat.cpdata.local.SharedPrefs
import java.io.IOException
import javax.inject.Inject

internal class HeaderInterceptor
@Inject constructor(private val sharedPrefs: SharedPrefs) : Interceptor {

    companion object {
        private const val USER_AGENT_HEADER = "User-Agent"
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()
        addUserAgentHeader(requestBuilder)
        return chain.proceed(requestBuilder.build())
    }

    private fun addUserAgentHeader(requestBuilder: Request.Builder) {
        val userAgent = sharedPrefs.userAgent
        if (userAgent != null) {
            requestBuilder.addHeader(USER_AGENT_HEADER, userAgent)
        }
    }

}
