package pl.cyfrowypolsat.cpdata.common.mock.auth

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okio.Buffer
import pl.cyfrowypolsat.cpdata.BuildConfig
import javax.inject.Inject

internal class Rodo2RuleUnacceptedExceptionInterceptor
@Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var response = chain.proceed(chain.request())
        val requestBody: String = getRequestString(chain.request())
        if (BuildConfig.DEBUG) {
            if (requestBody.contains("\"method\":\"login\"")) {
                response = authRulesExceptionTypeRodo2Response(chain)
            }
        }
        return response
    }

    private fun getRequestString(request: Request): String {
        return try {
            val copy = request.newBuilder().build()
            val buffer = Buffer()
            copy.body!!.writeTo(buffer)
            buffer.readUtf8()
        } catch (e: Exception) {
            ""
        }
    }

    private fun authRulesExceptionTypeRodo2Response(chain: Interceptor.Chain): Response {
        return Response.Builder()
                .code(200)
                .message("")
                .request(chain.request())
                .protocol(Protocol.HTTP_1_0)
                .body(ResponseBody.create("application/json".toMediaTypeOrNull(), rulesExceptionRulesTypeRodo2()))
                .addHeader("content-type", "application/json")
                .build()
    }

    private fun rulesExceptionRulesTypeRodo2(): String {
        return """{
                   "error":{
                      "code":13431,
                      "message":"Rules not anwsered or not accepted",
                      "data":{
                         "type":"RulesException",
                         "code":12019,
                         "message":"U\u017cytkownik musi przeczyta\u0107 regulamin.",
                         "messageId":"6c7a0fd5-8118-4110-8a0f-837e32fcf82c-029",
                         "userMessage":"Prosz\u0119 zapozna\u0107 si\u0119 z regulaminami",
                         "rulesType":"rodo2"
                      }
                   },
                   "id":4018281547819616759,
                   "jsonrpc":"2.0"
                }"""
    }
}
