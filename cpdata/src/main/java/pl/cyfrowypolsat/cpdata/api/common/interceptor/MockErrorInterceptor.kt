package pl.cyfrowypolsat.cpdata.api.common.interceptor

import okhttp3.*
import okhttp3.Interceptor.Chain
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import okio.Buffer
import pl.cyfrowypolsat.cpdata.BuildConfig
import java.io.IOException

class MockErrorInterceptor(private val errorCode: Long,
                           private val postMethodNameToMock: String = "getMedia",
                           private val getMethodNameToMock: String = "getChannelsCurrentProgram",
                           private val errorFrequency: Int = 2) : Interceptor {

    companion object {
        private var countGet = 0
        private var countPost = 0
    }

    @Throws(IOException::class)
    override fun intercept(chain: Chain): Response {
        val response: Response = chain.proceed(chain.request())
        if (BuildConfig.DEBUG) {
            val requestBody = getRequestString(chain.request())
            val methodName = chain.request().url.queryParameter("method")
            if (requestBody != null && requestBody.contains("\"" + postMethodNameToMock + "\"")) {
                countPost++
                if (countPost % errorFrequency == 0) {
                    response.close()
                    return getErrorResponse(chain)
                }
            }
            if (methodName != null && methodName == getMethodNameToMock) {
                countGet++
                if (countGet % errorFrequency == 0) {
                    response.close()
                    return getErrorResponse(chain)
                }
            }
        }
        return response
    }

    private fun getErrorResponse(chain: Chain): Response {

        return Response.Builder()
                .code(200)
                .message("")
                .request(chain.request())
                .protocol(Protocol.HTTP_1_0)
                .body(error().toResponseBody("application/json".toMediaType()))
                .addHeader("content-type", "application/json")
                .build()
    }

    private fun error(): String {
        return "{\"error\":{\"code\":$errorCode,\"data\":{\"code\":12007,\"message\":\"\",\"messageId\":\"efefd99c-0d92-4350-815d-5a5116096679-037\",\"type\":\"\",\"userMessage\":\"\"},\"message\":\"\"},\"id\":587259379515444090,\"jsonrpc\":\"2.0\"}"
    }

    private fun getRequestString(request: Request): String? {
        return try {
            val copy = request.newBuilder().build()
            val buffer = Buffer()
            copy.body!!.writeTo(buffer)
            buffer.readUtf8()
        } catch (e: Exception) {
            null
        }
    }
}