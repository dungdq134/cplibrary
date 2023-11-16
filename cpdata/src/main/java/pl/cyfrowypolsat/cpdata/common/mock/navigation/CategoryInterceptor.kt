package pl.cyfrowypolsat.cpdata.common.mock.navigation

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okio.Buffer
import pl.cyfrowypolsat.cpdata.BuildConfig
import javax.inject.Inject

internal class CategoryInterceptor
@Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var response = chain.proceed(chain.request())
        val requestBody: String = getRequestString(chain.request())
        if (BuildConfig.DEBUG) {
            if (requestBody.contains("\"getCategory\"")
                    && requestBody.contains("\"catid\":5024060")) {
                response = getProductElevenPacketResponse(chain)
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
        return return Response.Builder()
                .code(200)
                .message("")
                .request(chain.request())
                .protocol(Protocol.HTTP_1_0)
                .body(ResponseBody.create("application/json".toMediaTypeOrNull(), category()))
                .addHeader("content-type", "application/json")
                .build()
    }

    private fun category(): String {
        return """
            {
               "id":2904504218825052140,
               "jsonrpc":"2.0",
               "result":{
                  "categoryNamesPath":[
                     "PBG",
                     "Seriale",
                     "Przyjaciółki"
                  ],
                  "categoryPath":[
                     5023974,
                     5024044,
                     5024060
                  ],
                  "chronological":true,
                  "cpid":7,
                  "description":"Bohaterkami \"Przyjaciółek\" są cztery kobiety, znające się jeszcze z czasów szkolnych: bizneswoman, która nie ma szczęścia do mężczyzn, wzięta fryzjerka z nieskorym do ożenku partnerem, rozwódka z dzieckiem i oddana rodzinie gospodyni domowa. Każda z bohaterek jest inna, różni je sytuacja życiowa i problemy, którym muszą stawić czoła. Spotkają się po latach na zjeździe absolwentów, gdzie okaże się, że właśnie teraz potrzebują siebie jak nigdy.",
                  "genres":[
                     
                  ],
                  "id":5024060,
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
                  "keyCategoryId":5024060,
                  "name":"Przyjaciółki",
                  "reporting":{
                     "activityEvents":{
                        "contentItem":{
                           "type":"category",
                           "value":"5024060"
                        }
                     },
                     "gprism":{
                        "categoryPath":"Wideo/Seriale/Przyjaciółki"
                     },
                     "planet":{
                        "content":"Seriale"
                     },
                     "playerEvents":{
                        "contentItem":{
                           "type":"category",
                           "value":"5024060"
                        }
                     }
                  },
                  "subCategoriesLabel":"Sezony",
                  "thumbnails":[
                     {
                        "size":{
                           "height":77,
                           "width":136
                        },
                        "src":"http://ipla.pluscdn.pl/p/redb/3r/3r2h3mp8ma859ttgn1muzagqzpwt4gcb.jpg",
                        "type":"jpg"
                     },
                     {
                        "size":{
                           "height":56,
                           "width":100
                        },
                        "src":"http://ipla.pluscdn.pl/p/redb/ts/tsk22duwijvt3oaosyknhm29xd2ci2vc.jpg",
                        "type":"jpg"
                     },
                     {
                        "size":{
                           "height":166,
                           "width":304
                        },
                        "src":"http://ipla.pluscdn.pl/p/redb/t9/t9uoeg315vztr12r236ggdaq1akvjtgy.jpg",
                        "type":"jpg"
                     },
                     {
                        "size":{
                           "height":252,
                           "width":448
                        },
                        "src":"http://ipla.pluscdn.pl/p/redb/4h/4hn3yznx4348ituv8s7e25arp9vdafmt.jpg",
                        "type":"jpg"
                     },
                     {
                        "size":{
                           "height":432,
                           "width":768
                        },
                        "src":"http://ipla.pluscdn.pl/p/redb/o8/o8qgt8gb266cmnzm1dpwe2k4fusrseiv.jpg",
                        "type":"jpg"
                     },
                     {
                        "size":{
                           "height":768,
                           "width":1366
                        },
                        "src":"http://ipla.pluscdn.pl/p/redb/7e/7exkzysac84ukma384zh3ougw242m3d7.jpg",
                        "type":"jpg"
                     },
                     {
                        "size":{
                           "height":1080,
                           "width":1920
                        },
                        "src":"http://ipla.pluscdn.pl/p/redb/td/tdqvwpy4gez1rw535hc97vnmn6qaq6ax.jpg",
                        "type":"jpg"
                     },
                     {
                        "size":{
                           "height":1125,
                           "width":2000
                        },
                        "src":"http://ipla.pluscdn.pl/p/redb/mw/mwe1y1o15d4642wrkcazxqny6k6151t7.jpg",
                        "type":"jpg"
                     },
                     {
                        "size":{
                           "height":153,
                           "width":272
                        },
                        "src":"http://ipla.pluscdn.pl/p/redb/kt/kth4ec83e1q48cs56oy5faugpsgc8t8w.jpg",
                        "type":"jpg"
                     }
                  ]
               }
            }
            """
    }
}
