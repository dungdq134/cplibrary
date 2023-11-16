package pl.cyfrowypolsat.cpdata.api.common.interceptor

import android.annotation.SuppressLint
import com.google.gson.Gson
import okhttp3.Interceptor
import okhttp3.Response
import pl.cyfrowypolsat.cpdata.api.common.clientcontext.ClientContextRepository
import pl.cyfrowypolsat.cpdata.api.common.clientcontext.ClientContextRepository.Companion.SESSION_UPDATE_DATE_HEADER
import pl.cyfrowypolsat.cpdata.di.CpDataQualifier
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

internal class ClientContextInterceptor
@Inject constructor(@CpDataQualifier private val gson: Gson,
                    private val clientContextRepository: ClientContextRepository) : Interceptor {

    @SuppressLint("CheckResult")
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)
        val sessionUpdateTime = response.headers[SESSION_UPDATE_DATE_HEADER]?.toLong()
                ?: return response

        return try {
            if (clientContextRepository.isClientContextExpired(sessionUpdateTime)) {
                clientContextRepository.getClientContextTokenAndSave().blockingFirst()
            }
            response

        } catch (exception: Exception) {
            Timber.d(exception)
            response
        }
    }

}
