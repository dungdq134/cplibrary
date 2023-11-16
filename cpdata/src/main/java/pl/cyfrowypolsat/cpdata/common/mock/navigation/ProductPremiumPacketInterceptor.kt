package pl.cyfrowypolsat.cpdata.common.mock.navigation

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okio.Buffer
import pl.cyfrowypolsat.cpdata.BuildConfig
import javax.inject.Inject

internal class ProductPremiumPacketInterceptor
@Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var response = chain.proceed(chain.request())
        val requestBody: String = getRequestString(chain.request())
        if (BuildConfig.DEBUG) {
            if (requestBody.contains("\"getProduct\"")
                    && requestBody.contains("\"product\":{\"id\":\"premium\",\"subType\":\"packet\",\"type\":\"multiple\"}")) {
                response = getProductPremiumPacketResponse(chain)
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

    private fun getProductPremiumPacketResponse(chain: Interceptor.Chain): Response {
        return Response.Builder()
                .code(200)
                .message("")
                .request(chain.request())
                .protocol(Protocol.HTTP_1_0)
                .body(ResponseBody.create("application/json".toMediaTypeOrNull(), productPremiumPacket()))
                .addHeader("content-type", "application/json")
                .build()
    }

    private fun productPremiumPacket(): String{
        return """{
   "result":{
      "id":"premium",
      "type":"multiple",
      "subType":"packet",
      "name":"IPLA PREMIUM",
      "description":"<b>TERAZ TANIEJ. KUP PAKIET ZA 15 Z\u0141OTYCH. CENA REGULARNA 20 Z\u0141OTYCH<\/b>.<br>\nUlubione seriale wcze\u015bniej ni\u017c w TV. Tysi\u0105ce polskich i zagranicznych film\u00f3w oraz odcink\u00f3w seriali: kinowe hity, klasyka filmowa. Wszystko bez reklam.<br>\nW pakiecie IPLA Premium wcze\u015bniej ni\u017c inni obejrzysz wybrane przedpremierowe odcinki najlepszych i najpopularniejszych seriali Telewizji Polsat i TV4.<br>\nDodatkowo otrzymujesz dost\u0119p do tysi\u0119cy film\u00f3w i odcink\u00f3w seriali: kinowych hit\u00f3w, klasyki oraz film\u00f3w, do kt\u00f3rych lubimy wraca\u0107.<br>\nPakiet w\u0142\u0105cza r\u00f3wnie\u017c opcj\u0119 \"pobierz\" pozwalaj\u0105c\u0105 na zapisywanie plik\u00f3w w aplikacjach systemu Android i ogl\u0105danie ich offline.<br> <\/br>Szczeg\u00f3\u0142y w \u201eRegulaminie P\u0142atnego dost\u0119pu do IPLA w Cyfrowym Polsacie\" pod adresem  http:\/\/redirector.redefine.pl\/iplatv\/regulaminIpla.pdf",
      "thumbnails":[
         {
            "type":"jpg",
            "src":"https:\/\/redirector.redefine.pl\/iplatv\/pakiet_premium_400x400.jpg",
            "size":{
               "width":400,
               "height":400
            }
         },
         {
            "type":"jpg",
            "src":"https:\/\/redirector.redefine.pl\/iplatv\/pakiet_premium_200x200.jpg",
            "size":{
               "width":200,
               "height":200
            }
         }
      ],
      "platforms":[
         "tv",
         "pc",
         "mobile",
         "stb"
      ],
      "offers":[
         {
            "id":"ej",
            "type":"online",
            "name":"Dost\u0119p na 30 dni odnawiany",
            "description":" - IPLA PREMIUM na 30 dni odnawiany dla OTT",
            "accessDuration":2592000,
            "accessText":"30 dni",
            "cyclic":{
               "maxCycles":-1,
               "maxTries":5,
               "retryInterval":3600,
               "retryIntervalText":"1 godz"
            },
            "options":[
               {
                  "id":"444218303",
                  "type":"dotpay-card",
                  "name":"Karta",
                  "prices":[
                     {
                        "currency":"PLN",
                        "value":1500,
                        "valueText":"15,00 z\u0142"
                     }
                  ],
                  "minPrice":{
                     "currency":"PLN",
                     "value":1500,
                     "valueText":"15,00 z\u0142"
                  },
                  "minPriceIncludingPricingPlans":{
                     "currency":"PLN",
                     "value":1500,
                     "valueText":"15,00 z\u0142",
                     "valueIncludingPricingPlans":450,
                     "valueTextIncludingPricingPlans":"4,50 z\u0142"
                  },
                  "pricesIncludingPricingPlans":[
                     {
                        "currency":"PLN",
                        "value":1500,
                        "valueText":"15,00 z\u0142",
                        "valueIncludingPricingPlans":450,
                        "valueTextIncludingPricingPlans":"4,50 z\u0142"
                     }
                  ],
                  "rules":[
                     {
                        "id":8,
                        "type":"registration",
                        "name":"Regulamin IPLA",
                        "description":"Regulamin",
                        "agreeDescription":"O\u015bwiadczam, \u017ce zapozna\u0142em si\u0119 i akceptuj\u0119 postanowienia regulaminu",
                        "version":1,
                        "required":true,
                        "requiredAnswer":true,
                        "sources":[
                           {
                              "type":"html",
                              "url":"http:\/\/redirector.redefine.pl\/versions\/regulaminIpla.html"
                           },
                           {
                              "type":"pdf",
                              "url":"https:\/\/redirector.redefine.pl\/versions\/RegulaminIpla.pdf"
                           }
                        ]
                     },
                     {
                        "id":37,
                        "type":"payment",
                        "name":"Regulamin \u015bwiadczenia us\u0142ugi",
                        "description":"Regulamin \u015bwiadczenia us\u0142ugi",
                        "agreeDescription":"Wyra\u017cam zgod\u0119 na rozpocz\u0119cie wykonywania \u015bwiadczenia przed up\u0142ywem terminu do odst\u0105pienia od umowy (przed up\u0142ywem 14 dni od dnia zawarcia umowy). Przyjmuj\u0119 do wiadomo\u015bci, \u017ce rozpocz\u0119cie \u015bwiadczenia po wyra\u017ceniu zgody powoduje utrat\u0119 prawa do odst\u0105pienia od umowy",
                        "version":1,
                        "required":true,
                        "requiredAnswer":true,
                        "sources":[
                           
                        ]
                     },
                     {
                        "id":40,
                        "type":"payment",
                        "name":"Regulamin korzystania z odnawianych Pakiet\u00f3w IPLA",
                        "description":"Regulamin korzystania z odnawianych Pakiet\u00f3w IPLA",
                        "agreeDescription":"Zapozna\u0142em si\u0119 i akceptuj\u0119 postanowienia Regulaminu korzystania z odnawianych Pakiet\u00f3w IPLA",
                        "version":1,
                        "required":true,
                        "requiredAnswer":true,
                        "sources":[
                           {
                              "type":"pdf",
                              "url":"https:\/\/redirector.redefine.pl\/iplatv\/RegulaminPakietowOdnawianychiPromocji.pdf"
                           }
                        ]
                     },
                     {
                        "id":41,
                        "type":"payment",
                        "name":"Regulamin automatycznego pobierania op\u0142aty z karty p\u0142atniczej",
                        "description":"Regulamin automatycznego pobierania op\u0142aty z karty p\u0142atniczej",
                        "agreeDescription":"Aktywuj\u0105c us\u0142ug\u0119 wyra\u017cam zgod\u0119 na automatyczne pobieranie co 30 dni z mojej karty p\u0142atniczej op\u0142aty za wybrany pakiet",
                        "version":1,
                        "required":true,
                        "requiredAnswer":true,
                        "sources":[
                           
                        ]
                     }
                  ]
               },
			   {
                  "id":"444218303",
                  "type":"dotpay-card",
                  "name":"Karta",
				  "prices":[
                     {
                        "currency":"PLN",
                        "value":1500,
                        "valueText":"15,00 z\u0142"
                     }
                  ],
                  "minPrice":{
                     "currency":"PLN",
                     "value":1500,
                     "valueText":"15,00 z\u0142"
                  }
               },
			   {
                  "id":"444218303",
                  "type":"dotpay-card",
                  "name":"Karta",
				  "prices":[
                     {
                        "currency":"PLN",
                        "value":1500,
                        "valueText":"15,00 z\u0142"
                     }
                  ],
                  "minPrice":{
                     "currency":"PLN",
                     "value":1500,
                     "valueText":"15,00 z\u0142"
                  }
               },
			   {
                  "id":"444218303",
                  "type":"dotpay-card",
                  "name":"Karta",
				  "prices":[
                     {
                        "currency":"PLN",
                        "value":1500,
                        "valueText":"15,00 z\u0142"
                     }
                  ],
                  "minPrice":{
                     "currency":"PLN",
                     "value":1500,
                     "valueText":"15,00 z\u0142"
                  }
               },
			   {
                  "id":"444218303",
                  "type":"dotpay-card",
                  "name":"Karta",
				  "prices":[
                     {
                        "currency":"PLN",
                        "value":1500,
                        "valueText":"15,00 z\u0142"
                     }
                  ],
                  "minPrice":{
                     "currency":"PLN",
                     "value":1500,
                     "valueText":"15,00 z\u0142"
                  }
               },
			   {
                  "id":"444218303",
                  "type":"dotpay-card",
                  "name":"Karta",
				  "prices":[
                     {
                        "currency":"PLN",
                        "value":1500,
                        "valueText":"15,00 z\u0142"
                     }
                  ],
                  "minPrice":{
                     "currency":"PLN",
                     "value":1500,
                     "valueText":"15,00 z\u0142"
                  }
               },
			   {
                  "id":"444218303",
                  "type":"dotpay-card",
                  "name":"Karta",
				  "prices":[
                     {
                        "currency":"PLN",
                        "value":1500,
                        "valueText":"15,00 z\u0142"
                     }
                  ],
                  "minPrice":{
                     "currency":"PLN",
                     "value":1500,
                     "valueText":"15,00 z\u0142"
                  }
               },
			   {
                  "id":"444218303",
                  "type":"dotpay-card",
                  "name":"Karta",
				  "prices":[
                     {
                        "currency":"PLN",
                        "value":1500,
                        "valueText":"15,00 z\u0142"
                     }
                  ],
                  "minPrice":{
                     "currency":"PLN",
                     "value":1500,
                     "valueText":"15,00 z\u0142"
                  }
               },
			   {
                  "id":"444218303",
                  "type":"dotpay-card",
                  "name":"Karta",
				  "prices":[
                     {
                        "currency":"PLN",
                        "value":1500,
                        "valueText":"15,00 z\u0142"
                     }
                  ],
                  "minPrice":{
                     "currency":"PLN",
                     "value":1500,
                     "valueText":"15,00 z\u0142"
                  }
               },
			   {
                  "id":"444218303",
                  "type":"dotpay-card",
                  "name":"Karta",
				  "prices":[
                     {
                        "currency":"PLN",
                        "value":1500,
                        "valueText":"15,00 z\u0142"
                     }
                  ],
                  "minPrice":{
                     "currency":"PLN",
                     "value":1500,
                     "valueText":"15,00 z\u0142"
                  }
               },
			   {
                  "id":"444218303",
                  "type":"dotpay-card",
                  "name":"Karta",
				  "prices":[
                     {
                        "currency":"PLN",
                        "value":1500,
                        "valueText":"15,00 z\u0142"
                     }
                  ],
                  "minPrice":{
                     "currency":"PLN",
                     "value":1500,
                     "valueText":"15,00 z\u0142"
                  }
               },
			   {
                  "id":"444218303",
                  "type":"dotpay-card",
                  "name":"Karta",
				  "prices":[
                     {
                        "currency":"PLN",
                        "value":1500,
                        "valueText":"15,00 z\u0142"
                     }
                  ],
                  "minPrice":{
                     "currency":"PLN",
                     "value":1500,
                     "valueText":"15,00 z\u0142"
                  }
               },
               {
                  "id":"410811850",
                  "type":"code",
                  "name":"Kod"
               }
            ],
            "minPrice":{
               "currency":"PLN",
               "value":1500,
               "valueText":"15,00 z\u0142"
            },
            "minPriceOptions":[
               {
                  "id":"444218303",
                  "type":"dotpay-card"
               }
            ],
            "offerPricingPlans":[
               {
                  "id":"31",
                  "validFrom":"2020-12-14T23:00:00Z",
                  "validTo":"2020-12-30T23:00:00Z",
                  "pricingPlan":{
                     "id":"15",
                     "name":"Promocyjny Android - Testowy PP",
                     "rules":[
                        {
                           "id":1,
                           "type":"login",
                           "name":"Regulamin Serwisu",
                           "description":"Regulamin Serwisu",
                           "agreeDescription":"Zapozna\u0142em si\u0119 i akceptuj\u0119 Regulamin Serwisu",
                           "version":1,
                           "required":false,
                           "requiredAnswer":false,
                           "sources":[
                              {
                                 "type":"html",
                                 "url":"https:\/\/redirector.redefine.pl\/versions\/RegulaminCyfrowy_Polsat_GO_25_05_2018.html"
                              },
                              {
                                 "type":"pdf",
                                 "url":"https:\/\/redirector.redefine.pl\/versions\/RegulaminCyfrowy_Polsat_GO_25_05_2018.pdf"
                              },
                              {
                                 "type":"txt",
                                 "url":"https:\/\/redirector.redefine.pl\/versions\/RegulaminCyfrowy_Polsat_GO_25_05_2018.txt"
                              }
                           ]
                        }
                     ],
                     "items":[
                        {
                           "optionType":"dotpay-card",
                           "validFrom":"2020-12-03T23:00:00Z",
                           "validTo":"2020-12-17T23:00:00Z",
                           "currency":"PLN",
                           "valueModifier":{
                              "type":"percentage",
                              "value":30
                           }
                        },
                        {
                           "optionType":"dotpay-blik",
                           "validFrom":"2020-12-03T23:00:00Z",
                           "validTo":"2020-12-17T23:00:00Z",
                           "currency":"PLN",
                           "valueModifier":{
                              "type":"percentage",
                              "value":50
                           }
                        }
                     ]
                  }
               }
            ],
            "minPriceIncludingPricingPlans":{
               "currency":"PLN",
               "value":1500,
               "valueText":"15,00 z\u0142",
               "valueIncludingPricingPlans":450,
               "valueTextIncludingPricingPlans":"4,50 z\u0142"
            },
            "minPriceOptionsIncludingPricingPlans":[
               {
                  "id":"444218303",
                  "type":"dotpay-card"
               }
            ]
         },
         {
            "id":"el",
            "type":"online",
            "name":"Dost\u0119p na 30 dni",
            "description":"- IPLA PREMIUM dla OTT (niecykliczny) ",
            "accessDuration":2592000,
            "accessText":"30 dni",
            "options":[
               {
                  "id":"450016243",
                  "type":"dotpay-card",
                  "name":"Karta",
                  "prices":[
                     {
                        "currency":"PLN",
                        "value":1500,
                        "valueText":"15,00 z\u0142"
                     }
                  ],
                  "minPrice":{
                     "currency":"PLN",
                     "value":1500,
                     "valueText":"15,00 z\u0142"
                  },
                  "rules":[
                     {
                        "id":8,
                        "type":"registration",
                        "name":"Regulamin IPLA",
                        "description":"Regulamin",
                        "agreeDescription":"O\u015bwiadczam, \u017ce zapozna\u0142em si\u0119 i akceptuj\u0119 postanowienia regulaminu",
                        "version":1,
                        "required":true,
                        "requiredAnswer":true,
                        "sources":[
                           {
                              "type":"html",
                              "url":"http:\/\/redirector.redefine.pl\/versions\/regulaminIpla.html"
                           },
                           {
                              "type":"pdf",
                              "url":"https:\/\/redirector.redefine.pl\/versions\/RegulaminIpla.pdf"
                           }
                        ]
                     },
                     {
                        "id":37,
                        "type":"payment",
                        "name":"Regulamin \u015bwiadczenia us\u0142ugi",
                        "description":"Regulamin \u015bwiadczenia us\u0142ugi",
                        "agreeDescription":"Wyra\u017cam zgod\u0119 na rozpocz\u0119cie wykonywania \u015bwiadczenia przed up\u0142ywem terminu do odst\u0105pienia od umowy (przed up\u0142ywem 14 dni od dnia zawarcia umowy). Przyjmuj\u0119 do wiadomo\u015bci, \u017ce rozpocz\u0119cie \u015bwiadczenia po wyra\u017ceniu zgody powoduje utrat\u0119 prawa do odst\u0105pienia od umowy",
                        "version":1,
                        "required":true,
                        "requiredAnswer":true,
                        "sources":[
                           
                        ]
                     }
                  ]
               },
               {
                  "id":"469207415",
                  "type":"code",
                  "name":"Kod"
               },
               {
                  "id":"474117154",
                  "type":"dotpay-blik",
                  "name":"Blik",
                  "prices":[
                     {
                        "currency":"PLN",
                        "value":1500,
                        "valueText":"15,00 z\u0142"
                     }
                  ],
                  "minPrice":{
                     "currency":"PLN",
                     "value":1500,
                     "valueText":"15,00 z\u0142"
                  },
                  "rules":[
                     {
                        "id":8,
                        "type":"registration",
                        "name":"Regulamin IPLA",
                        "description":"Regulamin",
                        "agreeDescription":"O\u015bwiadczam, \u017ce zapozna\u0142em si\u0119 i akceptuj\u0119 postanowienia regulaminu",
                        "version":1,
                        "required":true,
                        "requiredAnswer":true,
                        "sources":[
                           {
                              "type":"html",
                              "url":"http:\/\/redirector.redefine.pl\/versions\/regulaminIpla.html"
                           },
                           {
                              "type":"pdf",
                              "url":"https:\/\/redirector.redefine.pl\/versions\/RegulaminIpla.pdf"
                           }
                        ]
                     },
                     {
                        "id":37,
                        "type":"payment",
                        "name":"Regulamin \u015bwiadczenia us\u0142ugi",
                        "description":"Regulamin \u015bwiadczenia us\u0142ugi",
                        "agreeDescription":"Wyra\u017cam zgod\u0119 na rozpocz\u0119cie wykonywania \u015bwiadczenia przed up\u0142ywem terminu do odst\u0105pienia od umowy (przed up\u0142ywem 14 dni od dnia zawarcia umowy). Przyjmuj\u0119 do wiadomo\u015bci, \u017ce rozpocz\u0119cie \u015bwiadczenia po wyra\u017ceniu zgody powoduje utrat\u0119 prawa do odst\u0105pienia od umowy",
                        "version":1,
                        "required":true,
                        "requiredAnswer":true,
                        "sources":[
                           
                        ]
                     }
                  ]
               }
            ],
            "minPrice":{
               "currency":"PLN",
               "value":1500,
               "valueText":"15,00 z\u0142"
            },
            "minPriceOptions":[
               {
                  "id":"450016243",
                  "type":"dotpay-card"
               },
               {
                  "id":"474117154",
                  "type":"dotpay-blik"
               }
            ]
         },
		 {
            "id":"el",
            "type":"online",
            "name":"Dost\u0119p na 30 dni",
            "description":"- IPLA PREMIUM dla OTT (niecykliczny) ",
            "accessDuration":2592000,
            "accessText":"30 dni",
            "options":[
               {
                  "id":"450016243",
                  "type":"dotpay-card",
                  "name":"Karta",
                  "prices":[
                     {
                        "currency":"PLN",
                        "value":1500,
                        "valueText":"15,00 z\u0142"
                     }
                  ],
                  "minPrice":{
                     "currency":"PLN",
                     "value":1500,
                     "valueText":"15,00 z\u0142"
                  },
                  "rules":[
                     {
                        "id":8,
                        "type":"registration",
                        "name":"Regulamin IPLA",
                        "description":"Regulamin",
                        "agreeDescription":"O\u015bwiadczam, \u017ce zapozna\u0142em si\u0119 i akceptuj\u0119 postanowienia regulaminu",
                        "version":1,
                        "required":true,
                        "requiredAnswer":true,
                        "sources":[
                           {
                              "type":"html",
                              "url":"http:\/\/redirector.redefine.pl\/versions\/regulaminIpla.html"
                           },
                           {
                              "type":"pdf",
                              "url":"https:\/\/redirector.redefine.pl\/versions\/RegulaminIpla.pdf"
                           }
                        ]
                     },
                     {
                        "id":37,
                        "type":"payment",
                        "name":"Regulamin \u015bwiadczenia us\u0142ugi",
                        "description":"Regulamin \u015bwiadczenia us\u0142ugi",
                        "agreeDescription":"Wyra\u017cam zgod\u0119 na rozpocz\u0119cie wykonywania \u015bwiadczenia przed up\u0142ywem terminu do odst\u0105pienia od umowy (przed up\u0142ywem 14 dni od dnia zawarcia umowy). Przyjmuj\u0119 do wiadomo\u015bci, \u017ce rozpocz\u0119cie \u015bwiadczenia po wyra\u017ceniu zgody powoduje utrat\u0119 prawa do odst\u0105pienia od umowy",
                        "version":1,
                        "required":true,
                        "requiredAnswer":true,
                        "sources":[
                           
                        ]
                     }
                  ]
               },
               {
                  "id":"469207415",
                  "type":"code",
                  "name":"Kod"
               },
               {
                  "id":"474117154",
                  "type":"dotpay-blik",
                  "name":"Blik",
                  "prices":[
                     {
                        "currency":"PLN",
                        "value":1500,
                        "valueText":"15,00 z\u0142"
                     }
                  ],
                  "minPrice":{
                     "currency":"PLN",
                     "value":1500,
                     "valueText":"15,00 z\u0142"
                  },
                  "rules":[
                     {
                        "id":8,
                        "type":"registration",
                        "name":"Regulamin IPLA",
                        "description":"Regulamin",
                        "agreeDescription":"O\u015bwiadczam, \u017ce zapozna\u0142em si\u0119 i akceptuj\u0119 postanowienia regulaminu",
                        "version":1,
                        "required":true,
                        "requiredAnswer":true,
                        "sources":[
                           {
                              "type":"html",
                              "url":"http:\/\/redirector.redefine.pl\/versions\/regulaminIpla.html"
                           },
                           {
                              "type":"pdf",
                              "url":"https:\/\/redirector.redefine.pl\/versions\/RegulaminIpla.pdf"
                           }
                        ]
                     },
                     {
                        "id":37,
                        "type":"payment",
                        "name":"Regulamin \u015bwiadczenia us\u0142ugi",
                        "description":"Regulamin \u015bwiadczenia us\u0142ugi",
                        "agreeDescription":"Wyra\u017cam zgod\u0119 na rozpocz\u0119cie wykonywania \u015bwiadczenia przed up\u0142ywem terminu do odst\u0105pienia od umowy (przed up\u0142ywem 14 dni od dnia zawarcia umowy). Przyjmuj\u0119 do wiadomo\u015bci, \u017ce rozpocz\u0119cie \u015bwiadczenia po wyra\u017ceniu zgody powoduje utrat\u0119 prawa do odst\u0105pienia od umowy",
                        "version":1,
                        "required":true,
                        "requiredAnswer":true,
                        "sources":[
                           
                        ]
                     }
                  ]
               }
            ],
            "minPrice":{
               "currency":"PLN",
               "value":1500,
               "valueText":"15,00 z\u0142"
            },
            "minPriceOptions":[
               {
                  "id":"450016243",
                  "type":"dotpay-card"
               },
               {
                  "id":"474117154",
                  "type":"dotpay-blik"
               }
            ]
         },
		 {
            "id":"el",
            "type":"online",
            "name":"Dost\u0119p na 30 dni",
            "description":"- IPLA PREMIUM dla OTT (niecykliczny) ",
            "accessDuration":2592000,
            "accessText":"30 dni",
            "options":[
               {
                  "id":"450016243",
                  "type":"dotpay-card",
                  "name":"Karta",
                  "prices":[
                     {
                        "currency":"PLN",
                        "value":1500,
                        "valueText":"15,00 z\u0142"
                     }
                  ],
                  "minPrice":{
                     "currency":"PLN",
                     "value":1500,
                     "valueText":"15,00 z\u0142"
                  },
                  "rules":[
                     {
                        "id":8,
                        "type":"registration",
                        "name":"Regulamin IPLA",
                        "description":"Regulamin",
                        "agreeDescription":"O\u015bwiadczam, \u017ce zapozna\u0142em si\u0119 i akceptuj\u0119 postanowienia regulaminu",
                        "version":1,
                        "required":true,
                        "requiredAnswer":true,
                        "sources":[
                           {
                              "type":"html",
                              "url":"http:\/\/redirector.redefine.pl\/versions\/regulaminIpla.html"
                           },
                           {
                              "type":"pdf",
                              "url":"https:\/\/redirector.redefine.pl\/versions\/RegulaminIpla.pdf"
                           }
                        ]
                     },
                     {
                        "id":37,
                        "type":"payment",
                        "name":"Regulamin \u015bwiadczenia us\u0142ugi",
                        "description":"Regulamin \u015bwiadczenia us\u0142ugi",
                        "agreeDescription":"Wyra\u017cam zgod\u0119 na rozpocz\u0119cie wykonywania \u015bwiadczenia przed up\u0142ywem terminu do odst\u0105pienia od umowy (przed up\u0142ywem 14 dni od dnia zawarcia umowy). Przyjmuj\u0119 do wiadomo\u015bci, \u017ce rozpocz\u0119cie \u015bwiadczenia po wyra\u017ceniu zgody powoduje utrat\u0119 prawa do odst\u0105pienia od umowy",
                        "version":1,
                        "required":true,
                        "requiredAnswer":true,
                        "sources":[
                           
                        ]
                     }
                  ]
               },
               {
                  "id":"469207415",
                  "type":"code",
                  "name":"Kod"
               },
               {
                  "id":"474117154",
                  "type":"dotpay-blik",
                  "name":"Blik",
                  "prices":[
                     {
                        "currency":"PLN",
                        "value":1500,
                        "valueText":"15,00 z\u0142"
                     }
                  ],
                  "minPrice":{
                     "currency":"PLN",
                     "value":1500,
                     "valueText":"15,00 z\u0142"
                  },
                  "rules":[
                     {
                        "id":8,
                        "type":"registration",
                        "name":"Regulamin IPLA",
                        "description":"Regulamin",
                        "agreeDescription":"O\u015bwiadczam, \u017ce zapozna\u0142em si\u0119 i akceptuj\u0119 postanowienia regulaminu",
                        "version":1,
                        "required":true,
                        "requiredAnswer":true,
                        "sources":[
                           {
                              "type":"html",
                              "url":"http:\/\/redirector.redefine.pl\/versions\/regulaminIpla.html"
                           },
                           {
                              "type":"pdf",
                              "url":"https:\/\/redirector.redefine.pl\/versions\/RegulaminIpla.pdf"
                           }
                        ]
                     },
                     {
                        "id":37,
                        "type":"payment",
                        "name":"Regulamin \u015bwiadczenia us\u0142ugi",
                        "description":"Regulamin \u015bwiadczenia us\u0142ugi",
                        "agreeDescription":"Wyra\u017cam zgod\u0119 na rozpocz\u0119cie wykonywania \u015bwiadczenia przed up\u0142ywem terminu do odst\u0105pienia od umowy (przed up\u0142ywem 14 dni od dnia zawarcia umowy). Przyjmuj\u0119 do wiadomo\u015bci, \u017ce rozpocz\u0119cie \u015bwiadczenia po wyra\u017ceniu zgody powoduje utrat\u0119 prawa do odst\u0105pienia od umowy",
                        "version":1,
                        "required":true,
                        "requiredAnswer":true,
                        "sources":[
                           
                        ]
                     }
                  ]
               }
            ],
            "minPrice":{
               "currency":"PLN",
               "value":1500,
               "valueText":"15,00 z\u0142"
            },
            "minPriceOptions":[
               {
                  "id":"450016243",
                  "type":"dotpay-card"
               },
               {
                  "id":"474117154",
                  "type":"dotpay-blik"
               }
            ]
         },
		 {
            "id":"el",
            "type":"online",
            "name":"Dost\u0119p na 30 dni",
            "description":"- IPLA PREMIUM dla OTT (niecykliczny) ",
            "accessDuration":2592000,
            "accessText":"30 dni",
            "options":[
               {
                  "id":"450016243",
                  "type":"dotpay-card",
                  "name":"Karta",
                  "prices":[
                     {
                        "currency":"PLN",
                        "value":1500,
                        "valueText":"15,00 z\u0142"
                     }
                  ],
                  "minPrice":{
                     "currency":"PLN",
                     "value":1500,
                     "valueText":"15,00 z\u0142"
                  },
                  "rules":[
                     {
                        "id":8,
                        "type":"registration",
                        "name":"Regulamin IPLA",
                        "description":"Regulamin",
                        "agreeDescription":"O\u015bwiadczam, \u017ce zapozna\u0142em si\u0119 i akceptuj\u0119 postanowienia regulaminu",
                        "version":1,
                        "required":true,
                        "requiredAnswer":true,
                        "sources":[
                           {
                              "type":"html",
                              "url":"http:\/\/redirector.redefine.pl\/versions\/regulaminIpla.html"
                           },
                           {
                              "type":"pdf",
                              "url":"https:\/\/redirector.redefine.pl\/versions\/RegulaminIpla.pdf"
                           }
                        ]
                     },
                     {
                        "id":37,
                        "type":"payment",
                        "name":"Regulamin \u015bwiadczenia us\u0142ugi",
                        "description":"Regulamin \u015bwiadczenia us\u0142ugi",
                        "agreeDescription":"Wyra\u017cam zgod\u0119 na rozpocz\u0119cie wykonywania \u015bwiadczenia przed up\u0142ywem terminu do odst\u0105pienia od umowy (przed up\u0142ywem 14 dni od dnia zawarcia umowy). Przyjmuj\u0119 do wiadomo\u015bci, \u017ce rozpocz\u0119cie \u015bwiadczenia po wyra\u017ceniu zgody powoduje utrat\u0119 prawa do odst\u0105pienia od umowy",
                        "version":1,
                        "required":true,
                        "requiredAnswer":true,
                        "sources":[
                           
                        ]
                     }
                  ]
               },
               {
                  "id":"469207415",
                  "type":"code",
                  "name":"Kod"
               },
               {
                  "id":"474117154",
                  "type":"dotpay-blik",
                  "name":"Blik",
                  "prices":[
                     {
                        "currency":"PLN",
                        "value":1500,
                        "valueText":"15,00 z\u0142"
                     }
                  ],
                  "minPrice":{
                     "currency":"PLN",
                     "value":1500,
                     "valueText":"15,00 z\u0142"
                  },
                  "rules":[
                     {
                        "id":8,
                        "type":"registration",
                        "name":"Regulamin IPLA",
                        "description":"Regulamin",
                        "agreeDescription":"O\u015bwiadczam, \u017ce zapozna\u0142em si\u0119 i akceptuj\u0119 postanowienia regulaminu",
                        "version":1,
                        "required":true,
                        "requiredAnswer":true,
                        "sources":[
                           {
                              "type":"html",
                              "url":"http:\/\/redirector.redefine.pl\/versions\/regulaminIpla.html"
                           },
                           {
                              "type":"pdf",
                              "url":"https:\/\/redirector.redefine.pl\/versions\/RegulaminIpla.pdf"
                           }
                        ]
                     },
                     {
                        "id":37,
                        "type":"payment",
                        "name":"Regulamin \u015bwiadczenia us\u0142ugi",
                        "description":"Regulamin \u015bwiadczenia us\u0142ugi",
                        "agreeDescription":"Wyra\u017cam zgod\u0119 na rozpocz\u0119cie wykonywania \u015bwiadczenia przed up\u0142ywem terminu do odst\u0105pienia od umowy (przed up\u0142ywem 14 dni od dnia zawarcia umowy). Przyjmuj\u0119 do wiadomo\u015bci, \u017ce rozpocz\u0119cie \u015bwiadczenia po wyra\u017ceniu zgody powoduje utrat\u0119 prawa do odst\u0105pienia od umowy",
                        "version":1,
                        "required":true,
                        "requiredAnswer":true,
                        "sources":[
                           
                        ]
                     }
                  ]
               }
            ],
            "minPrice":{
               "currency":"PLN",
               "value":1500,
               "valueText":"15,00 z\u0142"
            },
            "minPriceOptions":[
               {
                  "id":"450016243",
                  "type":"dotpay-card"
               },
               {
                  "id":"474117154",
                  "type":"dotpay-blik"
               }
            ]
         },
		 {
            "id":"el",
            "type":"online",
            "name":"Dost\u0119p na 30 dni",
            "description":"- IPLA PREMIUM dla OTT (niecykliczny) ",
            "accessDuration":2592000,
            "accessText":"30 dni",
            "options":[
               {
                  "id":"450016243",
                  "type":"dotpay-card",
                  "name":"Karta",
                  "prices":[
                     {
                        "currency":"PLN",
                        "value":1500,
                        "valueText":"15,00 z\u0142"
                     }
                  ],
                  "minPrice":{
                     "currency":"PLN",
                     "value":1500,
                     "valueText":"15,00 z\u0142"
                  },
                  "rules":[
                     {
                        "id":8,
                        "type":"registration",
                        "name":"Regulamin IPLA",
                        "description":"Regulamin",
                        "agreeDescription":"O\u015bwiadczam, \u017ce zapozna\u0142em si\u0119 i akceptuj\u0119 postanowienia regulaminu",
                        "version":1,
                        "required":true,
                        "requiredAnswer":true,
                        "sources":[
                           {
                              "type":"html",
                              "url":"http:\/\/redirector.redefine.pl\/versions\/regulaminIpla.html"
                           },
                           {
                              "type":"pdf",
                              "url":"https:\/\/redirector.redefine.pl\/versions\/RegulaminIpla.pdf"
                           }
                        ]
                     },
                     {
                        "id":37,
                        "type":"payment",
                        "name":"Regulamin \u015bwiadczenia us\u0142ugi",
                        "description":"Regulamin \u015bwiadczenia us\u0142ugi",
                        "agreeDescription":"Wyra\u017cam zgod\u0119 na rozpocz\u0119cie wykonywania \u015bwiadczenia przed up\u0142ywem terminu do odst\u0105pienia od umowy (przed up\u0142ywem 14 dni od dnia zawarcia umowy). Przyjmuj\u0119 do wiadomo\u015bci, \u017ce rozpocz\u0119cie \u015bwiadczenia po wyra\u017ceniu zgody powoduje utrat\u0119 prawa do odst\u0105pienia od umowy",
                        "version":1,
                        "required":true,
                        "requiredAnswer":true,
                        "sources":[
                           
                        ]
                     }
                  ]
               },
               {
                  "id":"469207415",
                  "type":"code",
                  "name":"Kod"
               },
               {
                  "id":"474117154",
                  "type":"dotpay-blik",
                  "name":"Blik",
                  "prices":[
                     {
                        "currency":"PLN",
                        "value":1500,
                        "valueText":"15,00 z\u0142"
                     }
                  ],
                  "minPrice":{
                     "currency":"PLN",
                     "value":1500,
                     "valueText":"15,00 z\u0142"
                  },
                  "rules":[
                     {
                        "id":8,
                        "type":"registration",
                        "name":"Regulamin IPLA",
                        "description":"Regulamin",
                        "agreeDescription":"O\u015bwiadczam, \u017ce zapozna\u0142em si\u0119 i akceptuj\u0119 postanowienia regulaminu",
                        "version":1,
                        "required":true,
                        "requiredAnswer":true,
                        "sources":[
                           {
                              "type":"html",
                              "url":"http:\/\/redirector.redefine.pl\/versions\/regulaminIpla.html"
                           },
                           {
                              "type":"pdf",
                              "url":"https:\/\/redirector.redefine.pl\/versions\/RegulaminIpla.pdf"
                           }
                        ]
                     },
                     {
                        "id":37,
                        "type":"payment",
                        "name":"Regulamin \u015bwiadczenia us\u0142ugi",
                        "description":"Regulamin \u015bwiadczenia us\u0142ugi",
                        "agreeDescription":"Wyra\u017cam zgod\u0119 na rozpocz\u0119cie wykonywania \u015bwiadczenia przed up\u0142ywem terminu do odst\u0105pienia od umowy (przed up\u0142ywem 14 dni od dnia zawarcia umowy). Przyjmuj\u0119 do wiadomo\u015bci, \u017ce rozpocz\u0119cie \u015bwiadczenia po wyra\u017ceniu zgody powoduje utrat\u0119 prawa do odst\u0105pienia od umowy",
                        "version":1,
                        "required":true,
                        "requiredAnswer":true,
                        "sources":[
                           
                        ]
                     }
                  ]
               }
            ],
            "minPrice":{
               "currency":"PLN",
               "value":1500,
               "valueText":"15,00 z\u0142"
            },
            "minPriceOptions":[
               {
                  "id":"450016243",
                  "type":"dotpay-card"
               },
               {
                  "id":"474117154",
                  "type":"dotpay-blik"
               }
            ]
         },
		 {
            "id":"el",
            "type":"online",
            "name":"Dost\u0119p na 30 dni",
            "description":"- IPLA PREMIUM dla OTT (niecykliczny) ",
            "accessDuration":2592000,
            "accessText":"30 dni",
            "options":[
               {
                  "id":"450016243",
                  "type":"dotpay-card",
                  "name":"Karta",
                  "prices":[
                     {
                        "currency":"PLN",
                        "value":1500,
                        "valueText":"15,00 z\u0142"
                     }
                  ],
                  "minPrice":{
                     "currency":"PLN",
                     "value":1500,
                     "valueText":"15,00 z\u0142"
                  },
                  "rules":[
                     {
                        "id":8,
                        "type":"registration",
                        "name":"Regulamin IPLA",
                        "description":"Regulamin",
                        "agreeDescription":"O\u015bwiadczam, \u017ce zapozna\u0142em si\u0119 i akceptuj\u0119 postanowienia regulaminu",
                        "version":1,
                        "required":true,
                        "requiredAnswer":true,
                        "sources":[
                           {
                              "type":"html",
                              "url":"http:\/\/redirector.redefine.pl\/versions\/regulaminIpla.html"
                           },
                           {
                              "type":"pdf",
                              "url":"https:\/\/redirector.redefine.pl\/versions\/RegulaminIpla.pdf"
                           }
                        ]
                     },
                     {
                        "id":37,
                        "type":"payment",
                        "name":"Regulamin \u015bwiadczenia us\u0142ugi",
                        "description":"Regulamin \u015bwiadczenia us\u0142ugi",
                        "agreeDescription":"Wyra\u017cam zgod\u0119 na rozpocz\u0119cie wykonywania \u015bwiadczenia przed up\u0142ywem terminu do odst\u0105pienia od umowy (przed up\u0142ywem 14 dni od dnia zawarcia umowy). Przyjmuj\u0119 do wiadomo\u015bci, \u017ce rozpocz\u0119cie \u015bwiadczenia po wyra\u017ceniu zgody powoduje utrat\u0119 prawa do odst\u0105pienia od umowy",
                        "version":1,
                        "required":true,
                        "requiredAnswer":true,
                        "sources":[
                           
                        ]
                     }
                  ]
               },
               {
                  "id":"469207415",
                  "type":"code",
                  "name":"Kod"
               },
               {
                  "id":"474117154",
                  "type":"dotpay-blik",
                  "name":"Blik",
                  "prices":[
                     {
                        "currency":"PLN",
                        "value":1500,
                        "valueText":"15,00 z\u0142"
                     }
                  ],
                  "minPrice":{
                     "currency":"PLN",
                     "value":1500,
                     "valueText":"15,00 z\u0142"
                  },
                  "rules":[
                     {
                        "id":8,
                        "type":"registration",
                        "name":"Regulamin IPLA",
                        "description":"Regulamin",
                        "agreeDescription":"O\u015bwiadczam, \u017ce zapozna\u0142em si\u0119 i akceptuj\u0119 postanowienia regulaminu",
                        "version":1,
                        "required":true,
                        "requiredAnswer":true,
                        "sources":[
                           {
                              "type":"html",
                              "url":"http:\/\/redirector.redefine.pl\/versions\/regulaminIpla.html"
                           },
                           {
                              "type":"pdf",
                              "url":"https:\/\/redirector.redefine.pl\/versions\/RegulaminIpla.pdf"
                           }
                        ]
                     },
                     {
                        "id":37,
                        "type":"payment",
                        "name":"Regulamin \u015bwiadczenia us\u0142ugi",
                        "description":"Regulamin \u015bwiadczenia us\u0142ugi",
                        "agreeDescription":"Wyra\u017cam zgod\u0119 na rozpocz\u0119cie wykonywania \u015bwiadczenia przed up\u0142ywem terminu do odst\u0105pienia od umowy (przed up\u0142ywem 14 dni od dnia zawarcia umowy). Przyjmuj\u0119 do wiadomo\u015bci, \u017ce rozpocz\u0119cie \u015bwiadczenia po wyra\u017ceniu zgody powoduje utrat\u0119 prawa do odst\u0105pienia od umowy",
                        "version":1,
                        "required":true,
                        "requiredAnswer":true,
                        "sources":[
                           
                        ]
                     }
                  ]
               }
            ],
            "minPrice":{
               "currency":"PLN",
               "value":1500,
               "valueText":"15,00 z\u0142"
            },
            "minPriceOptions":[
               {
                  "id":"450016243",
                  "type":"dotpay-card"
               },
               {
                  "id":"474117154",
                  "type":"dotpay-blik"
               }
            ]
         },
		 {
            "id":"el",
            "type":"online",
            "name":"Dost\u0119p na 30 dni",
            "description":"- IPLA PREMIUM dla OTT (niecykliczny) ",
            "accessDuration":2592000,
            "accessText":"30 dni",
            "options":[
               {
                  "id":"450016243",
                  "type":"dotpay-card",
                  "name":"Karta",
                  "prices":[
                     {
                        "currency":"PLN",
                        "value":1500,
                        "valueText":"15,00 z\u0142"
                     }
                  ],
                  "minPrice":{
                     "currency":"PLN",
                     "value":1500,
                     "valueText":"15,00 z\u0142"
                  },
                  "rules":[
                     {
                        "id":8,
                        "type":"registration",
                        "name":"Regulamin IPLA",
                        "description":"Regulamin",
                        "agreeDescription":"O\u015bwiadczam, \u017ce zapozna\u0142em si\u0119 i akceptuj\u0119 postanowienia regulaminu",
                        "version":1,
                        "required":true,
                        "requiredAnswer":true,
                        "sources":[
                           {
                              "type":"html",
                              "url":"http:\/\/redirector.redefine.pl\/versions\/regulaminIpla.html"
                           },
                           {
                              "type":"pdf",
                              "url":"https:\/\/redirector.redefine.pl\/versions\/RegulaminIpla.pdf"
                           }
                        ]
                     },
                     {
                        "id":37,
                        "type":"payment",
                        "name":"Regulamin \u015bwiadczenia us\u0142ugi",
                        "description":"Regulamin \u015bwiadczenia us\u0142ugi",
                        "agreeDescription":"Wyra\u017cam zgod\u0119 na rozpocz\u0119cie wykonywania \u015bwiadczenia przed up\u0142ywem terminu do odst\u0105pienia od umowy (przed up\u0142ywem 14 dni od dnia zawarcia umowy). Przyjmuj\u0119 do wiadomo\u015bci, \u017ce rozpocz\u0119cie \u015bwiadczenia po wyra\u017ceniu zgody powoduje utrat\u0119 prawa do odst\u0105pienia od umowy",
                        "version":1,
                        "required":true,
                        "requiredAnswer":true,
                        "sources":[
                           
                        ]
                     }
                  ]
               },
               {
                  "id":"469207415",
                  "type":"code",
                  "name":"Kod"
               },
               {
                  "id":"474117154",
                  "type":"dotpay-blik",
                  "name":"Blik",
                  "prices":[
                     {
                        "currency":"PLN",
                        "value":1500,
                        "valueText":"15,00 z\u0142"
                     }
                  ],
                  "minPrice":{
                     "currency":"PLN",
                     "value":1500,
                     "valueText":"15,00 z\u0142"
                  },
                  "rules":[
                     {
                        "id":8,
                        "type":"registration",
                        "name":"Regulamin IPLA",
                        "description":"Regulamin",
                        "agreeDescription":"O\u015bwiadczam, \u017ce zapozna\u0142em si\u0119 i akceptuj\u0119 postanowienia regulaminu",
                        "version":1,
                        "required":true,
                        "requiredAnswer":true,
                        "sources":[
                           {
                              "type":"html",
                              "url":"http:\/\/redirector.redefine.pl\/versions\/regulaminIpla.html"
                           },
                           {
                              "type":"pdf",
                              "url":"https:\/\/redirector.redefine.pl\/versions\/RegulaminIpla.pdf"
                           }
                        ]
                     },
                     {
                        "id":37,
                        "type":"payment",
                        "name":"Regulamin \u015bwiadczenia us\u0142ugi",
                        "description":"Regulamin \u015bwiadczenia us\u0142ugi",
                        "agreeDescription":"Wyra\u017cam zgod\u0119 na rozpocz\u0119cie wykonywania \u015bwiadczenia przed up\u0142ywem terminu do odst\u0105pienia od umowy (przed up\u0142ywem 14 dni od dnia zawarcia umowy). Przyjmuj\u0119 do wiadomo\u015bci, \u017ce rozpocz\u0119cie \u015bwiadczenia po wyra\u017ceniu zgody powoduje utrat\u0119 prawa do odst\u0105pienia od umowy",
                        "version":1,
                        "required":true,
                        "requiredAnswer":true,
                        "sources":[
                           
                        ]
                     }
                  ]
               }
            ],
            "minPrice":{
               "currency":"PLN",
               "value":1500,
               "valueText":"15,00 z\u0142"
            },
            "minPriceOptions":[
               {
                  "id":"450016243",
                  "type":"dotpay-card"
               },
               {
                  "id":"474117154",
                  "type":"dotpay-blik"
               }
            ]
         },
		 {
            "id":"el",
            "type":"online",
            "name":"Dost\u0119p na 30 dni",
            "description":"- IPLA PREMIUM dla OTT (niecykliczny) ",
            "accessDuration":2592000,
            "accessText":"30 dni",
            "options":[
               {
                  "id":"450016243",
                  "type":"dotpay-card",
                  "name":"Karta",
                  "prices":[
                     {
                        "currency":"PLN",
                        "value":1500,
                        "valueText":"15,00 z\u0142"
                     }
                  ],
                  "minPrice":{
                     "currency":"PLN",
                     "value":1500,
                     "valueText":"15,00 z\u0142"
                  },
                  "rules":[
                     {
                        "id":8,
                        "type":"registration",
                        "name":"Regulamin IPLA",
                        "description":"Regulamin",
                        "agreeDescription":"O\u015bwiadczam, \u017ce zapozna\u0142em si\u0119 i akceptuj\u0119 postanowienia regulaminu",
                        "version":1,
                        "required":true,
                        "requiredAnswer":true,
                        "sources":[
                           {
                              "type":"html",
                              "url":"http:\/\/redirector.redefine.pl\/versions\/regulaminIpla.html"
                           },
                           {
                              "type":"pdf",
                              "url":"https:\/\/redirector.redefine.pl\/versions\/RegulaminIpla.pdf"
                           }
                        ]
                     },
                     {
                        "id":37,
                        "type":"payment",
                        "name":"Regulamin \u015bwiadczenia us\u0142ugi",
                        "description":"Regulamin \u015bwiadczenia us\u0142ugi",
                        "agreeDescription":"Wyra\u017cam zgod\u0119 na rozpocz\u0119cie wykonywania \u015bwiadczenia przed up\u0142ywem terminu do odst\u0105pienia od umowy (przed up\u0142ywem 14 dni od dnia zawarcia umowy). Przyjmuj\u0119 do wiadomo\u015bci, \u017ce rozpocz\u0119cie \u015bwiadczenia po wyra\u017ceniu zgody powoduje utrat\u0119 prawa do odst\u0105pienia od umowy",
                        "version":1,
                        "required":true,
                        "requiredAnswer":true,
                        "sources":[
                           
                        ]
                     }
                  ]
               },
               {
                  "id":"469207415",
                  "type":"code",
                  "name":"Kod"
               },
               {
                  "id":"474117154",
                  "type":"dotpay-blik",
                  "name":"Blik",
                  "prices":[
                     {
                        "currency":"PLN",
                        "value":1500,
                        "valueText":"15,00 z\u0142"
                     }
                  ],
                  "minPrice":{
                     "currency":"PLN",
                     "value":1500,
                     "valueText":"15,00 z\u0142"
                  },
                  "rules":[
                     {
                        "id":8,
                        "type":"registration",
                        "name":"Regulamin IPLA",
                        "description":"Regulamin",
                        "agreeDescription":"O\u015bwiadczam, \u017ce zapozna\u0142em si\u0119 i akceptuj\u0119 postanowienia regulaminu",
                        "version":1,
                        "required":true,
                        "requiredAnswer":true,
                        "sources":[
                           {
                              "type":"html",
                              "url":"http:\/\/redirector.redefine.pl\/versions\/regulaminIpla.html"
                           },
                           {
                              "type":"pdf",
                              "url":"https:\/\/redirector.redefine.pl\/versions\/RegulaminIpla.pdf"
                           }
                        ]
                     },
                     {
                        "id":37,
                        "type":"payment",
                        "name":"Regulamin \u015bwiadczenia us\u0142ugi",
                        "description":"Regulamin \u015bwiadczenia us\u0142ugi",
                        "agreeDescription":"Wyra\u017cam zgod\u0119 na rozpocz\u0119cie wykonywania \u015bwiadczenia przed up\u0142ywem terminu do odst\u0105pienia od umowy (przed up\u0142ywem 14 dni od dnia zawarcia umowy). Przyjmuj\u0119 do wiadomo\u015bci, \u017ce rozpocz\u0119cie \u015bwiadczenia po wyra\u017ceniu zgody powoduje utrat\u0119 prawa do odst\u0105pienia od umowy",
                        "version":1,
                        "required":true,
                        "requiredAnswer":true,
                        "sources":[
                           
                        ]
                     }
                  ]
               }
            ],
            "minPrice":{
               "currency":"PLN",
               "value":1500,
               "valueText":"15,00 z\u0142"
            },
            "minPriceOptions":[
               {
                  "id":"450016243",
                  "type":"dotpay-card"
               },
               {
                  "id":"474117154",
                  "type":"dotpay-blik"
               }
            ]
         }
      ],
      "minPrice":{
         "currency":"PLN",
         "value":1500,
         "valueText":"15,00 z\u0142",
         "higherPricesExist":true
      },
      "minPriceOffers":[
         {
            "id":"ej",
            "type":"online"
         },
         {
            "id":"el",
            "type":"online"
         }
      ],
      "minPriceIncludingPricingPlans":{
         "currency":"PLN",
         "value":1500,
         "valueText":"15,00 z\u0142",
         "higherPricesExist":true,
         "valueIncludingPricingPlans":450,
         "valueTextIncludingPricingPlans":"4,50 z\u0142"
      },
      "minPriceOffersIncludingPricingPlans":[
         {
            "id":"ej",
            "type":"online"
         }
      ]
   },
   "id":7912576466463040849,
   "jsonrpc":"2.0"
}"""
    }
}
