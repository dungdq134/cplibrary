package pl.cyfrowypolsat.cpdata.api.common.interceptor

import android.annotation.SuppressLint
import com.google.gson.Gson
import com.google.gson.JsonParser
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import pl.cyfrowypolsat.cpdata.api.common.extensions.bodyToString
import pl.cyfrowypolsat.cpdata.api.common.jsonrpc.JsonRPCError
import pl.cyfrowypolsat.cpdata.api.common.jsonrpc.JsonRPCException
import pl.cyfrowypolsat.cpdata.api.common.jsonrpc.JsonRPCResponse
import pl.cyfrowypolsat.cpdata.api.common.session.AutoLoginFailedListener
import pl.cyfrowypolsat.cpdata.api.common.session.SessionManager
import pl.cyfrowypolsat.cpdata.di.CpDataQualifier
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

internal class SessionInterceptor
@Inject constructor(private val sessionManager: SessionManager,
                    @CpDataQualifier private val gson: Gson,
                    private val autoLoginFailedListener: AutoLoginFailedListener) : Interceptor {

    @SuppressLint("CheckResult")
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        try {
            val responseBody = response.peekBody(Long.MAX_VALUE).string() // https://github.com/square/okhttp/issues/1240
            val containsError = responseBody.contains("\"error\"")

            if (containsError && isUnauthorizedResponse(responseBody)) {
                synchronized(this) {
                    if (isSessionRefreshed(request)) {
                        return chain.proceed(getRequestWithNewSessionToken(request))
                    } else {
                        sessionManager.autoLogin()
                                .doOnError { throwable ->
                                    Timber.d(throwable)
                                    if (throwable is JsonRPCException
                                            || throwable is SessionManager.NoLoginAuthDataException) {
                                        autoLoginFailedListener.onAutoLoginFailed(throwable)
                                    }
                                }
                                .blockingFirst()

                        return chain.proceed(getRequestWithNewSessionToken(request))
                    }
                }
            } else {
                return response
            }
        } catch (exception: Exception) {
            Timber.d(exception)
            return response
        }
    }

    private fun isUnauthorizedResponse(responseBody: String): Boolean {
        val rpcResponse: JsonRPCResponse<*>? = gson.fromJson(responseBody, JsonRPCResponse::class.java)
        return rpcResponse?.error?.code == JsonRPCError.RPC_ERROR_UNAUTHORIZED_ACCESS
    }

    @Throws(Exception::class)
    private fun getRequestWithNewSessionToken(request: Request): Request {
        val body = request.bodyToString()

        val jsonObject = JsonParser().parse(body).asJsonObject
        val sessionToken = jsonObject.getAsJsonObject("params").getAsJsonObject("authData").getAsJsonPrimitive("sessionToken").asString
        val sessionTokenItems = sessionToken.split("|")
        val methodNamespace = sessionTokenItems[2]
        val method = sessionTokenItems[3]

        val newSessionToken = sessionManager.createSessionToken(method, methodNamespace)

        jsonObject.getAsJsonObject("params").getAsJsonObject("authData").remove("sessionToken")
        jsonObject.getAsJsonObject("params").getAsJsonObject("authData").addProperty("sessionToken", newSessionToken)

        return request.newBuilder()
                .method(request.method, RequestBody.create("application/json".toMediaTypeOrNull(), gson.toJson(jsonObject)))
                .build()
    }

    private fun isSessionRefreshed(request: Request): Boolean {
        return getSessionIdFromRequest(request) != sessionManager.getSessionId()
    }

    private fun getSessionIdFromRequest(request: Request): String {
        val body = request.bodyToString()
        val jsonObject = JsonParser().parse(body).asJsonObject
        val sessionToken = jsonObject.getAsJsonObject("params").getAsJsonObject("authData").getAsJsonPrimitive("sessionToken").asString
        val sessionTokenItems = sessionToken.split("|")
        return sessionTokenItems[0]
    }

}
