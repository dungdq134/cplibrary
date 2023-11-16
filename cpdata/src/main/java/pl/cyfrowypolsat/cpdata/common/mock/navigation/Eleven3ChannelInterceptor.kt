package pl.cyfrowypolsat.cpdata.common.mock.navigation

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okio.Buffer
import pl.cyfrowypolsat.cpdata.BuildConfig
import javax.inject.Inject

internal class Eleven3ChannelInterceptor
@Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var response = chain.proceed(chain.request())
        val requestBody: String = getRequestString(chain.request())
        if (BuildConfig.DEBUG) {
            if (requestBody.contains("\"getMedia\"")
                    && requestBody.contains("\"mediaId\":\"13105\"")) {
                response = getResponse(chain)
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

    private fun getResponse(chain: Interceptor.Chain): Response {
        return Response.Builder()
                .code(200)
                .message("")
                .request(chain.request())
                .protocol(Protocol.HTTP_1_0)
                .body(ResponseBody.create("application/json".toMediaTypeOrNull(), response()))
                .addHeader("content-type", "application/json")
                .build()
    }

    private fun response(): String{
        return """{
   "result":{
      "serializationType":"normal",
      "cpid":0,
      "id":"13105",
      "mediaType":"tv",
      "title":"Eleven Sports 3",
      "shortTitle":"Eleven Sports 3",
      "description":"",
      "genres":[
         "Inne"
      ],
      "images":[
         {
            "type":"background",
            "sources":[
               {
                  "type":"jpg",
                  "src":"http:\/\/ipla-e2-22.pluscdn.pl/p/redb/1a/1a28zchtz33mpfn5bpj4qq9vp6ut2fgy.jpg",
                  "size":{
                     "width":768,
                     "height":432
                  }
               }
            ]
         }
      ],
      "thumbnails":[
         {
            "type":"jpg",
            "src":"http:\/\/ipla.pluscdn.pl\/p\/redb\/m3\/m3rdgh9555e83u3zq41u3mtpcxem18cw.jpg",
            "size":{
               "width":768,
               "height":432
            }
         },
         {
            "type":"jpg",
            "src":"http:\/\/ipla.pluscdn.pl\/p\/redb\/oa\/oaigxannvxvse15cs8uidtw9wx2qr4tx.jpg",
            "size":{
               "width":2000,
               "height":1125
            }
         }
      ],
      "posters":[
         
      ],
      "ageGroup":0,
      "vote":"0",
      "grantExpression":"loc:pl*sc:sport_ott*dev:all*os:all*app:all*player:all+loc:eu_no_pl*verified:true*sc:sport_ott*dev:all*os:all*app:all*player:all+loc:pl*sc:kat_eleven*dev:all*os:all*app:all*player:all+loc:eu_no_pl*verified:true*sc:kat_eleven*dev:all*os:all*app:all*player:all+loc:pl*sc:eleven*dev:all*os:all*app:all*player:all+loc:eu_no_pl*verified:true*sc:eleven*dev:all*os:all*app:all*player:all+loc:pl*sc:sport*dev:all*os:all*app:all*player:all+loc:eu_no_pl*verified:true*sc:sport*dev:all*os:all*app:all*player:all+loc:eu_no_pl*ug:verified*sc:sport_ott*dev:all*os:all*app:all*player:all+loc:eu_no_pl*ug:verified*sc:kat_eleven*dev:all*os:all*app:all*player:all+loc:eu_no_pl*ug:verified*sc:eleven*dev:all*os:all*app:all*player:all+loc:eu_no_pl*ug:verified*sc:sport*dev:all*os:all*app:all*player:all+vip:true",
      "published":true,
      "allowDownload":false,
      "category":{
         "cpid":7,
         "id":5024077,
         "name":"Sport",
         "description":"",
         "genres":[
            
         ],
         "categoryPath":[
            5023974,
            5024077
         ],
         "categoryNamesPath":[
            "Pomidor",
            "Sport"
         ],
         "images":[
            
         ],
         "thumbnails":[
            
         ],
         "reporting":{
            "gprism":{
               "categoryPath":"Wideo\/Sport"
            },
            "activityEvents":{
               "contentItem":{
                  "type":"category",
                  "value":"5024077"
               }
            },
            "playerEvents":{
               "contentItem":{
                  "type":"category",
                  "value":"5024077"
               }
            }
         }
      },
      "timeShiftingDuration":43200,
      "publicationDate":"2016-12-28T12:37:16Z",
      "reporting":{
         "gprism":{
            "title":"Eleven Extra HD",
            "categoryPath":"Wideo\/SPORT"
         }
      },
      "product":{
         "id":"13105",
         "type":"media",
         "subType":"tv"
      },
      "distributor":"Eleven",
      "longDescription":"",
      "ads":{
         "tags":[
            "PAYPERVIEW",
            "ctype=0",
            "midspots=0",
            "postspots=0",
            "cdur=999999",
            "time=999999",
            "genres=Inne"
         ]
      },
      "platforms":[
         "mobile",
         "pc",
         "tv",
         "stb"
      ]
   },
   "id":308620575479370513,
   "jsonrpc":"2.0"
}"""
    }
}
