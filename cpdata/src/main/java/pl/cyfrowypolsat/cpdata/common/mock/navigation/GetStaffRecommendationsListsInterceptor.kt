package pl.cyfrowypolsat.cpdata.common.mock.navigation

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okio.Buffer
import pl.cyfrowypolsat.cpdata.BuildConfig
import javax.inject.Inject

internal class GetStaffRecommendationsListsInterceptor
@Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var response = chain.proceed(chain.request())
        val requestBody: String = getRequestString(chain.request())
        if (BuildConfig.DEBUG) {
            if (requestBody.contains("\"getStaffRecommendationsLists\""))
                response = getStaffRecommendationsListsResponse(chain)
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

    private fun getStaffRecommendationsListsResponse(chain: Interceptor.Chain): Response {
        return Response.Builder()
                .code(200)
                .message("")
                .request(chain.request())
                .protocol(Protocol.HTTP_1_0)
                .body(ResponseBody.create("application/json".toMediaTypeOrNull(), response()))
                .addHeader("content-type", "application/json")
                .build()
    }

    private fun response(): String {
        return """
            {
                "result": [
                    {
                        "id": "666",
                        "type": "static",
                        "name": "Pakiet Polsat Box Go Sport",
                        "description": "lista zwykła - sport",
                        "layout": {
                            "type": "custom",
                            "value": "simple"
                        }
                    }
                ],
                "id": 3478233290455592400,
                "jsonrpc": "2.0"
            }
        """.trimIndent()
    }
}
