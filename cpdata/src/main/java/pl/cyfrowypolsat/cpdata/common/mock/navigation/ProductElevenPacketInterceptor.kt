package pl.cyfrowypolsat.cpdata.common.mock.navigation

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okio.Buffer
import pl.cyfrowypolsat.cpdata.BuildConfig
import javax.inject.Inject

internal class ProductElevenPacketInterceptor
@Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var response = chain.proceed(chain.request())
        val requestBody: String = getRequestString(chain.request())
        if (BuildConfig.DEBUG) {
            if (requestBody.contains("\"getProduct\"")
                    && requestBody.contains("\"product\":{\"id\":\"eleven_eleven\",\"subType\":\"packet\",\"type\":\"multiple\"}")) {
                response = getProductElevenPacketResponse(chain)
            } else if (requestBody.contains("\"getProducts\"")) {
                response = getProductsWithElevenPacketResponse(chain)
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

    private fun getProductElevenPacketResponse(chain: Interceptor.Chain): Response {
        return getResponse(chain, productElevenPacket())
    }

    private fun getProductsWithElevenPacketResponse(chain: Interceptor.Chain): Response {
        return getResponse(chain, productsWithElevenPacket())
    }

    private fun getResponse(chain: Interceptor.Chain, body: String): Response {
        return Response.Builder()
                .code(200)
                .message("")
                .request(chain.request())
                .protocol(Protocol.HTTP_1_0)
                .body(ResponseBody.create("application/json".toMediaTypeOrNull(), body))
                .addHeader("content-type", "application/json")
                .build()
    }

    private fun productsWithElevenPacket(): String {
        return """
            {
               "id":907297118527286066,
               "jsonrpc":"2.0",
               "result":[${elevenPacketResult()}]
            }
            """
    }

    private fun productElevenPacket(): String {
        return """
            {
               "id":5307929117979807730,
               "jsonrpc":"2.0",
               "result":${elevenPacketResult()}
            }
            """
    }

    private fun elevenPacketResult(): String {
        return """
           {
                  "content":{
                     "cpid":9,
                     "id":"eleven_eleven"
                  },
                  "description":"Pełny dostęp do Eleven Sports 1, Eleven Sports 2, Eleven Sports 3, Eleven Sports 4",
                  "id":"eleven_eleven",
                  "name":"Eleven Sports",
                  "offers":[
                     {
                        "accessDuration":2592000,
                        "accessFrom":"2022-10-27T14:03:52Z",
                        "accessText":"30 dni",
                        "accessTo":"2022-11-26T14:03:52Z",
                        "id":"e30d",
                        "longAccessText":"Dostęp jednorazowy do 26.11.2022",
                        "name":"Eleven Sports na 30 dni",
                        "options":[
                           {
                              "id":"411631610",
                              "name":"Kod",
                              "type":"code"
                           }
                        ],
                        "type":"online"
                     },
                     {
                        "accessDuration":63072000,
                        "accessFrom":"2022-10-27T14:03:52Z",
                        "accessText":"na 2 lata",
                        "accessTo":"2024-10-26T14:03:52Z",
                        "id":"ee730",
                        "longAccessText":"Dostęp jednorazowy do 26.10.2024",
                        "name":"Eleven Sports na 730 dni",
                        "options":[
                           {
                              "id":"451673338",
                              "name":"Kod",
                              "type":"code"
                           }
                        ],
                        "type":"online"
                     },
                     {
                        "accessDuration":432000,
                        "accessFrom":"2022-10-27T14:03:52Z",
                        "accessText":"5 dni",
                        "accessTo":"2022-11-01T14:03:52Z",
                        "id":"e5d",
                        "longAccessText":"Dostęp jednorazowy do 01.11.2022",
                        "name":"Eleven Sports na 5 dni",
                        "options":[
                           {
                              "id":"427643145",
                              "name":"Kod",
                              "type":"code"
                           }
                        ],
                        "partnerId":"a57db6b0-7b70-4282-aac4-6b5f32315cc7",
                        "type":"online"
                     }
                  ],
                  "platforms":[
                     "tv",
                     "pc",
                     "mobile",
                     "stb"
                  ],
                  "posters":[
                     {
                        "size":{
                           "height":1080,
                           "width":1920
                        },
                        "src":"https://redirector.redefine.pl/iplatv/elevensports_1920x1080.jpg",
                        "type":"jpg"
                     }
                  ],
                  "reporting":{
                     "activityEvents":{
                        "contentItem":{
                           "type":"product_packet",
                           "value":"eleven_eleven"
                        }
                     },
                     "playerEvents":{
                        "contentItem":{
                           "type":"product_packet",
                           "value":"eleven_eleven"
                        }
                     }
                  },
                  "subType":"packet",
                  "images":[
                     {
                        "type":"background",
                        "sources":[
                           {
                              "size":{
                                 "height":1125,
                                 "width":2000
                              },
                              "src":"https://ipla.pluscdn.pl/p/cms_thumbnails/ho/hordro49ntyxbnwgt95abnqmwb84s547.jpg",
                              "type":"jpg"
                           }
                        ]
                     }
                  ],
                  "thumbnails":[
                     {
                        "size":{
                           "height":58,
                           "width":58
                        },
                        "src":"https://redirector.redefine.pl/iplatv/elevensports_58x58.jpg",
                        "type":"jpg"
                     },
                     {
                        "size":{
                           "height":400,
                           "width":400
                        },
                        "src":"https://redirector.redefine.pl/iplatv/elevensports_400x400.jpg",
                        "type":"jpg"
                     },
                     {
                        "size":{
                           "height":200,
                           "width":200
                        },
                        "src":"https://redirector.redefine.pl/iplatv/elevensports_200x200.jpg",
                        "type":"jpg"
                     }
                  ],
                  "type":"multiple"
               } 
        """
    }
}
