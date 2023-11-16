package pl.cyfrowypolsat.cpdata.api.common.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber
import java.util.*


// https://jira.polsatc/browse/AND-1083
class RetryRequestInterceptor : Interceptor {

    companion object {
        private const val RETRY_NUMBER = 2
        private const val MIN_DELAY = 3000
        private const val MAX_DELAY = 5000
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        var response = chain.proceed(request)
        var tryCount = 0

        while (isRetriableError(response) && tryCount < RETRY_NUMBER) {
            Thread.sleep(getRandomDelayForRetry())
            tryCount++
            response = chain.proceed(request)
        }

        return response
    }

    private fun isRetriableError(response: Response): Boolean {
        val code = response.code
        return code == 502 || code == 503 || code == 504
    }

    private fun getRandomDelayForRetry(): Long {
        val delay = Random().nextInt(MAX_DELAY - MIN_DELAY) + MIN_DELAY
        Timber.tag("OkHttp")
        Timber.d("Retry request with $delay ms delay")
        return delay.toLong()
    }

}