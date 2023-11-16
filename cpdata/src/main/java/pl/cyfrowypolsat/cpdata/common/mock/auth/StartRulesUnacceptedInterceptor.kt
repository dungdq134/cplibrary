package pl.cyfrowypolsat.cpdata.common.mock.auth

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okio.Buffer
import pl.cyfrowypolsat.cpdata.BuildConfig
import javax.inject.Inject

internal class StartRulesUnacceptedInterceptor
@Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var response = chain.proceed(chain.request())
        val requestBody: String = getRequestString(chain.request())
        if (BuildConfig.DEBUG) {
            if (requestBody.contains("\"getSession\"")) {
                response = authRulesExceptionTypeStartResponse(chain)
            } else if (requestBody.contains("\"getAllRules\"")
                    && requestBody.contains("\"rulesType\":\"start\"")) {
                response = getAllRulesTypeStartResponse(chain)
            } else if (requestBody.contains("\"getRulesStatus\"")) {
                response = getRulesStatusResponse(chain)
            } else if (requestBody.contains("\"getUnacceptedRules\"")) {
                response = getUnacceptedRulesResponse(chain)
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

    private fun authRulesExceptionTypeStartResponse(chain: Interceptor.Chain): Response {
        return Response.Builder()
                .code(200)
                .message("")
                .request(chain.request())
                .protocol(Protocol.HTTP_1_0)
                .body(ResponseBody.create("application/json".toMediaTypeOrNull(), rulesExceptionRulesTypeStart()))
                .addHeader("content-type", "application/json")
                .build()
    }

    private fun rulesExceptionRulesTypeStart(): String {
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
                         "rulesType":"start"
                      }
                   },
                   "id":4018281547819616759,
                   "jsonrpc":"2.0"
                }"""
    }

    private fun getAllRulesTypeStartResponse(chain: Interceptor.Chain): Response {
        return Response.Builder()
                .code(200)
                .message("")
                .request(chain.request())
                .protocol(Protocol.HTTP_1_0)
                .body(ResponseBody.create("application/json".toMediaTypeOrNull(), startRules()))
                .addHeader("content-type", "application/json")
                .build()
    }

    private fun startRules(): String {
        return """{
                "result": [
                    {
                        "id": 7,
                        "type": "start",
                        "name": "Regulamin start",
                        "description": "Regulamin",
                        "agreeDescription": "Przeczytałem i akceptuję postanowienia regulaminu",
                        "version": 1,
                        "required": false,
                        "requiredAnswer": true,
                        "sources": [
                            {
                                "type": "html",
                                "url": "https://redirector.redefine.pl/versions/regulamin_iOS_04_10_2018.htm"
                            }
                        ]
                    }
                ],
                "id": "2656",
                "jsonrpc": "2.0"
            }"""
    }

    private fun getRulesStatusResponse(chain: Interceptor.Chain): Response {
        return Response.Builder()
                .code(200)
                .message("")
                .request(chain.request())
                .protocol(Protocol.HTTP_1_0)
                .body(ResponseBody.create("application/json".toMediaTypeOrNull(), ruleStatusAccepted()))
                .addHeader("content-type", "application/json")
                .build()
    }

    private fun ruleStatusAccepted(): String {
        return """{
                "result": [
                    {
                        "rulesId": 7,
                        "status": "accepted"
                    }
                ],
                "id": "2656",
                "jsonrpc": "2.0"
            }"""
    }

    private fun getUnacceptedRulesResponse(chain: Interceptor.Chain): Response {
        return Response.Builder()
                .code(200)
                .message("")
                .request(chain.request())
                .protocol(Protocol.HTTP_1_0)
                .body(ResponseBody.create("application/json".toMediaTypeOrNull(), unacceptedRules()))
                .addHeader("content-type", "application/json")
                .build()
    }

    private fun unacceptedRules(): String {
        return """{
                   "result":[
                      {
                         "id":8,
                         "type":"registration",
                         "name":"Regulamin IPLA",
                         "description":"Regulamin",
                         "agreeDescription":"Oświadczam, że zapoznałem się i akceptuję postanowienia regulaminu",
                         "version":1,
                         "required":true,
                         "requiredAnswer":true,
                         "sources":[
                            {
                               "type":"html",
                               "url":"http://redirector.redefine.pl/versions/regulaminIpla.html"
                            },
                            {
                               "type":"pdf",
                               "url":"https://redirector.redefine.pl/versions/RegulaminIpla.pdf"
                            }
                         ]
                      },
                      {
                         "id":16,
                         "type":"registration",
                         "name":"Regulamin rejestracja",
                         "description":"Regulamin",
                         "agreeDescription":"Oświadczam, że zapoznałem się i akceptuję postanowienia Regulaminu",
                         "version":1,
                         "required":true,
                         "requiredAnswer":true,
                         "sources":[
                            {
                               "type":"html",
                               "url":"http://redirector.redefine.pl/versions/eleven-sports-regulamin.html"
                            },
                            {
                               "type":"pdf",
                               "url":"http://redirector.redefine.pl/elevensports/regulamin-platnego-dostepu.pdf"
                            }
                         ]
                      },
                      {
                         "id":34,
                         "type":"registration",
                         "name":"Regulamin rejestracji",
                         "description":"Regulamin rejestracji",
                         "agreeDescription":"Oświadczam, że zapoznałem się i akceptuję postanowienia <a href=\"http://redirector.redefine.pl/versions/regulamin-loveisland.html\">Regulaminu</a>",
                         "version":1,
                         "required":true,
                         "requiredAnswer":true,
                         "sources":[
                            {
                               "type":"html",
                               "url":"http://redirector.redefine.pl/versions/regulamin-loveisland.html"
                            }
                         ]
                      },
                      {
                         "id":43,
                         "type":"registration",
                         "name":"Regulamin rejestracji",
                         "description":"Regulamin rejestracji",
                         "agreeDescription":"Oświadczam, że zapoznałem się i akceptuję postanowienia <a href=\"http://redirector.redefine.pl/versions/reg_taniec_z_gwiazdami.html\">Regulaminu</a>",
                         "version":1,
                         "required":true,
                         "requiredAnswer":true,
                         "sources":[
                            {
                               "type":"html",
                               "url":"http://redirector.redefine.pl/versions/reg_taniec_z_gwiazdami.html"
                            }
                         ]
                      },
                      {
                         "id":46,
                         "type":"registration",
                         "name":"Regulamin rejestracji",
                         "description":"Regulamin rejestracji",
                         "agreeDescription":"Oświadczam, że zapoznałem się i akceptuję postanowienia <a href=\"http://redirector.redefine.pl/versions/reg_tylko_jeden.html\">Regulaminu</a>",
                         "version":1,
                         "required":true,
                         "requiredAnswer":true,
                         "sources":[
                            {
                               "type":"html",
                               "url":"http://redirector.redefine.pl/versions/reg_tylko_jeden.html"
                            }
                         ]
                      }
                   ],
                   "id":"2656",
                   "jsonrpc":"2.0"
                }"""
    }
}
