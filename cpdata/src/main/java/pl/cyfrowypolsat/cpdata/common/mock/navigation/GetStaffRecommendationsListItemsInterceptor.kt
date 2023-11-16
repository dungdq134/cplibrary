package pl.cyfrowypolsat.cpdata.common.mock.navigation

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okio.Buffer
import pl.cyfrowypolsat.cpdata.BuildConfig
import javax.inject.Inject

internal class GetStaffRecommendationsListItemsInterceptor
@Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var response = chain.proceed(chain.request())
        val requestBody: String = getRequestString(chain.request())
        if (BuildConfig.DEBUG) {
            if (requestBody.contains("\"getStaffRecommendationsListItems\""))
                response = getStaffRecommendationsListItemsResponse(chain)
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

    private fun getStaffRecommendationsListItemsResponse(chain: Interceptor.Chain): Response {
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
            "result": {
                "results": [
                    {
                        "type": "tv_program",
                        "value": "cms_120_2022_02_06_148755675",
                        "name": "walka: Kacper Formela - Damian Frankiewicz",
                        "posters": [],
                        "thumbnails": [
                            {
                                "type": "jpg",
                                "src": "https://ipla.pluscdn.pl/img/p/-fTixa6Isivu/1920x1080/p/cms_thumbnails/nm/nmdgji2dxqw438tvf2hpdfc39gk1xf8t.jpg",
                                "size": {
                                    "width": 1920,
                                    "height": 1080
                                }
                            },
                            {
                                "type": "jpg",
                                "src": "https://ipla.pluscdn.pl/img/p/ulsQs4_iuSh2/1366x768/p/cms_thumbnails/nm/nmdgji2dxqw438tvf2hpdfc39gk1xf8t.jpg",
                                "size": {
                                    "width": 1366,
                                    "height": 768
                                }
                            },
                            {
                                "type": "jpg",
                                "src": "https://ipla.pluscdn.pl/img/p/Casz-TbQnV4_/768x432/p/cms_thumbnails/nm/nmdgji2dxqw438tvf2hpdfc39gk1xf8t.jpg",
                                "size": {
                                    "width": 768,
                                    "height": 432
                                }
                            },
                            {
                                "type": "jpg",
                                "src": "https://ipla.pluscdn.pl/img/p/CeEA4H3pkrNG/448x252/p/cms_thumbnails/nm/nmdgji2dxqw438tvf2hpdfc39gk1xf8t.jpg",
                                "size": {
                                    "width": 448,
                                    "height": 252
                                }
                            },
                            {
                                "type": "jpg",
                                "src": "https://ipla.pluscdn.pl/img/p/t8N88BHycs55/272x153/p/cms_thumbnails/nm/nmdgji2dxqw438tvf2hpdfc39gk1xf8t.jpg",
                                "size": {
                                    "width": 272,
                                    "height": 153
                                }
                            },
                            {
                                "type": "jpg",
                                "src": "https://ipla.pluscdn.pl/img/p/xkcb3lSg8REs/304x-/p/cms_thumbnails/nm/nmdgji2dxqw438tvf2hpdfc39gk1xf8t.jpg",
                                "size": {
                                    "width": 304,
                                    "height": 166
                                }
                            }
                        ],
                        "object": {
                            "cpid": 12,
                            "id": "cms_120_2022_02_06_148755675",
                            "title": "walka: Kacper Formela - Damian Frankiewicz",
                            "genre": "FEN 33 LOTOS Fight Night",
                            "description": "Kacpra Formela świetnie radzi sobie w kategorii piórkowej i już niedługo może stanąć przed szansą walki o mistrzowski tytuł. Najpierw jednak musi pokonać Damiana Frankiewicza, który wygrał dziewięć z ostatnich dwunastu pojedynków. Oprócz walki wieczoru widzowie zobaczą jeszcze siedem starć na zasadach MMA, w tym jedno z udziałem pań. W formule K-1 zmierzą się Dominik Zadora i Pavel Sach.",
                            "date": "2022-01-31T05:00:00Z",
                            "startTime": "2022-01-31T05:00:00Z",
                            "reporting": {
                                "activityEvents": {
                                    "contentItem": {
                                        "type": "tv_program",
                                        "value": "cms_120_2022_01_31_148564878"
                                    }
                                },
                                "playerEvents": {
                                    "contentItem": {
                                        "type": "tv_program",
                                        "value": "cms_120_2022_01_31_148564878"
                                    }
                                }
                            },
                            "posters": [],
                            "thumbnails": [
                                {
                                    "type": "jpg",
                                    "src": "http://ipla.pluscdn.pl/p/redb/q6/q6o25sgjvf4nd95ndf21c7dwck4e953b.jpg",
                                    "size": {
                                        "width": 200,
                                        "height": 200
                                    }
                                },
                                {
                                    "type": "jpg",
                                    "src": "http://ipla.pluscdn.pl/p/redb/uw/uw8jv5jc41a9rpdwxc9g4ivgvuunej31.jpg",
                                    "size": {
                                        "width": 44,
                                        "height": 44
                                    }
                                },
                                {
                                    "type": "png",
                                    "src": "http://ipla.pluscdn.pl/p/redb/x5/x5r2hwb9c2xrop2mhfe5d8zdd7pg3tdj.png",
                                    "size": {
                                        "width": 93,
                                        "height": 93
                                    }
                                },
                                {
                                    "type": "jpg",
                                    "src": "http://ipla.pluscdn.pl/p/redb/kf/kf43f9dqwftvs6np7qy497tdvi6eoeuk.jpg",
                                    "size": {
                                        "width": 980,
                                        "height": 551
                                    }
                                },
                                {
                                    "type": "jpg",
                                    "src": "http://ipla.pluscdn.pl/p/redb/g5/g5qgscnyb8jw7xhzq7gaa559m17urfqm.jpg",
                                    "size": {
                                        "width": 448,
                                        "height": 252
                                    }
                                },
                                {
                                    "type": "jpg",
                                    "src": "http://ipla.pluscdn.pl/p/redb/m8/m87ksmrmzn1osuwwaft7ky1yi1pmoiz3.jpg",
                                    "size": {
                                        "width": 768,
                                        "height": 432
                                    }
                                },
                                {
                                    "type": "jpg",
                                    "src": "http://ipla.pluscdn.pl/p/redb/65/65akd2k6ggcgnh9j4kzk2ht374zmkinv.jpg",
                                    "size": {
                                        "width": 1366,
                                        "height": 768
                                    }
                                },
                                {
                                    "type": "jpg",
                                    "src": "http://ipla.pluscdn.pl/p/redb/wb/wb17v43xif878ys6o5q7br6qcjmbggdt.jpg",
                                    "size": {
                                        "width": 1920,
                                        "height": 1080
                                    }
                                },
                                {
                                    "type": "jpg",
                                    "src": "http://ipla.pluscdn.pl/p/redb/wi/wi8v8jrvkrd15payn6qv39kfqm1v99c6.jpg",
                                    "size": {
                                        "width": 2000,
                                        "height": 1125
                                    }
                                }
                            ]
                        }
                    },
                     {
                        "type": "tv_program",
                        "value": "cms_120_2022_02_06_148755666",
                        "name": "walka: Kacper Formela - Damian Frankiewicz",
                        "posters": [],
                        "thumbnails": [
                            {
                                "type": "jpg",
                                "src": "https://ipla.pluscdn.pl/img/p/-fTixa6Isivu/1920x1080/p/cms_thumbnails/nm/nmdgji2dxqw438tvf2hpdfc39gk1xf8t.jpg",
                                "size": {
                                    "width": 1920,
                                    "height": 1080
                                }
                            },
                            {
                                "type": "jpg",
                                "src": "https://ipla.pluscdn.pl/img/p/ulsQs4_iuSh2/1366x768/p/cms_thumbnails/nm/nmdgji2dxqw438tvf2hpdfc39gk1xf8t.jpg",
                                "size": {
                                    "width": 1366,
                                    "height": 768
                                }
                            },
                            {
                                "type": "jpg",
                                "src": "https://ipla.pluscdn.pl/img/p/Casz-TbQnV4_/768x432/p/cms_thumbnails/nm/nmdgji2dxqw438tvf2hpdfc39gk1xf8t.jpg",
                                "size": {
                                    "width": 768,
                                    "height": 432
                                }
                            },
                            {
                                "type": "jpg",
                                "src": "https://ipla.pluscdn.pl/img/p/CeEA4H3pkrNG/448x252/p/cms_thumbnails/nm/nmdgji2dxqw438tvf2hpdfc39gk1xf8t.jpg",
                                "size": {
                                    "width": 448,
                                    "height": 252
                                }
                            },
                            {
                                "type": "jpg",
                                "src": "https://ipla.pluscdn.pl/img/p/t8N88BHycs55/272x153/p/cms_thumbnails/nm/nmdgji2dxqw438tvf2hpdfc39gk1xf8t.jpg",
                                "size": {
                                    "width": 272,
                                    "height": 153
                                }
                            },
                            {
                                "type": "jpg",
                                "src": "https://ipla.pluscdn.pl/img/p/xkcb3lSg8REs/304x-/p/cms_thumbnails/nm/nmdgji2dxqw438tvf2hpdfc39gk1xf8t.jpg",
                                "size": {
                                    "width": 304,
                                    "height": 166
                                }
                            }
                        ],
                        "object": {
                            "cpid": 12,
                            "id": "cms_120_2022_02_06_148755666",
                            "title": "walka: Kacper Formela - Damian Frankiewicz",
                            "genre": "FEN 33 LOTOS Fight Night",
                            "description": "Kacpra Formela świetnie radzi sobie w kategorii piórkowej i już niedługo może stanąć przed szansą walki o mistrzowski tytuł. Najpierw jednak musi pokonać Damiana Frankiewicza, który wygrał dziewięć z ostatnich dwunastu pojedynków. Oprócz walki wieczoru widzowie zobaczą jeszcze siedem starć na zasadach MMA, w tym jedno z udziałem pań. W formule K-1 zmierzą się Dominik Zadora i Pavel Sach.",
                            "date": "2022-01-31T05:00:00Z",
                            "startTime": "2022-01-31T05:00:00Z",
                            "reporting": {
                                "activityEvents": {
                                    "contentItem": {
                                        "type": "tv_program",
                                        "value": "cms_120_2022_01_31_148564878"
                                    }
                                },
                                "playerEvents": {
                                    "contentItem": {
                                        "type": "tv_program",
                                        "value": "cms_120_2022_01_31_148564878"
                                    }
                                }
                            },
                            "posters": [],
                            "thumbnails": [
                                {
                                    "type": "jpg",
                                    "src": "http://ipla.pluscdn.pl/p/redb/q6/q6o25sgjvf4nd95ndf21c7dwck4e953b.jpg",
                                    "size": {
                                        "width": 200,
                                        "height": 200
                                    }
                                },
                                {
                                    "type": "jpg",
                                    "src": "http://ipla.pluscdn.pl/p/redb/uw/uw8jv5jc41a9rpdwxc9g4ivgvuunej31.jpg",
                                    "size": {
                                        "width": 44,
                                        "height": 44
                                    }
                                },
                                {
                                    "type": "png",
                                    "src": "http://ipla.pluscdn.pl/p/redb/x5/x5r2hwb9c2xrop2mhfe5d8zdd7pg3tdj.png",
                                    "size": {
                                        "width": 93,
                                        "height": 93
                                    }
                                },
                                {
                                    "type": "jpg",
                                    "src": "http://ipla.pluscdn.pl/p/redb/kf/kf43f9dqwftvs6np7qy497tdvi6eoeuk.jpg",
                                    "size": {
                                        "width": 980,
                                        "height": 551
                                    }
                                },
                                {
                                    "type": "jpg",
                                    "src": "http://ipla.pluscdn.pl/p/redb/g5/g5qgscnyb8jw7xhzq7gaa559m17urfqm.jpg",
                                    "size": {
                                        "width": 448,
                                        "height": 252
                                    }
                                },
                                {
                                    "type": "jpg",
                                    "src": "http://ipla.pluscdn.pl/p/redb/m8/m87ksmrmzn1osuwwaft7ky1yi1pmoiz3.jpg",
                                    "size": {
                                        "width": 768,
                                        "height": 432
                                    }
                                },
                                {
                                    "type": "jpg",
                                    "src": "http://ipla.pluscdn.pl/p/redb/65/65akd2k6ggcgnh9j4kzk2ht374zmkinv.jpg",
                                    "size": {
                                        "width": 1366,
                                        "height": 768
                                    }
                                },
                                {
                                    "type": "jpg",
                                    "src": "http://ipla.pluscdn.pl/p/redb/wb/wb17v43xif878ys6o5q7br6qcjmbggdt.jpg",
                                    "size": {
                                        "width": 1920,
                                        "height": 1080
                                    }
                                },
                                {
                                    "type": "jpg",
                                    "src": "http://ipla.pluscdn.pl/p/redb/wi/wi8v8jrvkrd15payn6qv39kfqm1v99c6.jpg",
                                    "size": {
                                        "width": 2000,
                                        "height": 1125
                                    }
                                }
                            ]
                        }
                    },
                     {
                        "type": "tv_program",
                        "value": "cms_120_2022_02_06_148755670",
                        "name": "walka: Kacper Formela - Damian Frankiewicz",
                        "posters": [],
                        "thumbnails": [
                            {
                                "type": "jpg",
                                "src": "https://ipla.pluscdn.pl/img/p/-fTixa6Isivu/1920x1080/p/cms_thumbnails/nm/nmdgji2dxqw438tvf2hpdfc39gk1xf8t.jpg",
                                "size": {
                                    "width": 1920,
                                    "height": 1080
                                }
                            },
                            {
                                "type": "jpg",
                                "src": "https://ipla.pluscdn.pl/img/p/ulsQs4_iuSh2/1366x768/p/cms_thumbnails/nm/nmdgji2dxqw438tvf2hpdfc39gk1xf8t.jpg",
                                "size": {
                                    "width": 1366,
                                    "height": 768
                                }
                            },
                            {
                                "type": "jpg",
                                "src": "https://ipla.pluscdn.pl/img/p/Casz-TbQnV4_/768x432/p/cms_thumbnails/nm/nmdgji2dxqw438tvf2hpdfc39gk1xf8t.jpg",
                                "size": {
                                    "width": 768,
                                    "height": 432
                                }
                            },
                            {
                                "type": "jpg",
                                "src": "https://ipla.pluscdn.pl/img/p/CeEA4H3pkrNG/448x252/p/cms_thumbnails/nm/nmdgji2dxqw438tvf2hpdfc39gk1xf8t.jpg",
                                "size": {
                                    "width": 448,
                                    "height": 252
                                }
                            },
                            {
                                "type": "jpg",
                                "src": "https://ipla.pluscdn.pl/img/p/t8N88BHycs55/272x153/p/cms_thumbnails/nm/nmdgji2dxqw438tvf2hpdfc39gk1xf8t.jpg",
                                "size": {
                                    "width": 272,
                                    "height": 153
                                }
                            },
                            {
                                "type": "jpg",
                                "src": "https://ipla.pluscdn.pl/img/p/xkcb3lSg8REs/304x-/p/cms_thumbnails/nm/nmdgji2dxqw438tvf2hpdfc39gk1xf8t.jpg",
                                "size": {
                                    "width": 304,
                                    "height": 166
                                }
                            }
                        ],
                        "object": {
                            "cpid": 12,
                            "id": "cms_120_2022_02_06_148755670",
                            "title": "walka: Kacper Formela - Damian Frankiewicz",
                            "genre": "FEN 33 LOTOS Fight Night",
                            "description": "Kacpra Formela świetnie radzi sobie w kategorii piórkowej i już niedługo może stanąć przed szansą walki o mistrzowski tytuł. Najpierw jednak musi pokonać Damiana Frankiewicza, który wygrał dziewięć z ostatnich dwunastu pojedynków. Oprócz walki wieczoru widzowie zobaczą jeszcze siedem starć na zasadach MMA, w tym jedno z udziałem pań. W formule K-1 zmierzą się Dominik Zadora i Pavel Sach.",
                            "date": "2022-01-31T05:00:00Z",
                            "startTime": "2022-01-31T05:00:00Z",
                            "reporting": {
                                "activityEvents": {
                                    "contentItem": {
                                        "type": "tv_program",
                                        "value": "cms_120_2022_01_31_148564878"
                                    }
                                },
                                "playerEvents": {
                                    "contentItem": {
                                        "type": "tv_program",
                                        "value": "cms_120_2022_01_31_148564878"
                                    }
                                }
                            },
                            "posters": [],
                            "thumbnails": [
                                {
                                    "type": "jpg",
                                    "src": "http://ipla.pluscdn.pl/p/redb/q6/q6o25sgjvf4nd95ndf21c7dwck4e953b.jpg",
                                    "size": {
                                        "width": 200,
                                        "height": 200
                                    }
                                },
                                {
                                    "type": "jpg",
                                    "src": "http://ipla.pluscdn.pl/p/redb/uw/uw8jv5jc41a9rpdwxc9g4ivgvuunej31.jpg",
                                    "size": {
                                        "width": 44,
                                        "height": 44
                                    }
                                },
                                {
                                    "type": "png",
                                    "src": "http://ipla.pluscdn.pl/p/redb/x5/x5r2hwb9c2xrop2mhfe5d8zdd7pg3tdj.png",
                                    "size": {
                                        "width": 93,
                                        "height": 93
                                    }
                                },
                                {
                                    "type": "jpg",
                                    "src": "http://ipla.pluscdn.pl/p/redb/kf/kf43f9dqwftvs6np7qy497tdvi6eoeuk.jpg",
                                    "size": {
                                        "width": 980,
                                        "height": 551
                                    }
                                },
                                {
                                    "type": "jpg",
                                    "src": "http://ipla.pluscdn.pl/p/redb/g5/g5qgscnyb8jw7xhzq7gaa559m17urfqm.jpg",
                                    "size": {
                                        "width": 448,
                                        "height": 252
                                    }
                                },
                                {
                                    "type": "jpg",
                                    "src": "http://ipla.pluscdn.pl/p/redb/m8/m87ksmrmzn1osuwwaft7ky1yi1pmoiz3.jpg",
                                    "size": {
                                        "width": 768,
                                        "height": 432
                                    }
                                },
                                {
                                    "type": "jpg",
                                    "src": "http://ipla.pluscdn.pl/p/redb/65/65akd2k6ggcgnh9j4kzk2ht374zmkinv.jpg",
                                    "size": {
                                        "width": 1366,
                                        "height": 768
                                    }
                                },
                                {
                                    "type": "jpg",
                                    "src": "http://ipla.pluscdn.pl/p/redb/wb/wb17v43xif878ys6o5q7br6qcjmbggdt.jpg",
                                    "size": {
                                        "width": 1920,
                                        "height": 1080
                                    }
                                },
                                {
                                    "type": "jpg",
                                    "src": "http://ipla.pluscdn.pl/p/redb/wi/wi8v8jrvkrd15payn6qv39kfqm1v99c6.jpg",
                                    "size": {
                                        "width": 2000,
                                        "height": 1125
                                    }
                                }
                            ]
                        }
                    },
                    {
                        "type": "tv_program",
                        "value": "cms_120_2022_02_06_148755662",
                        "name": "walka: Kacper Formela - Damian Frankiewicz",
                        "posters": [],
                        "thumbnails": [
                            {
                                "type": "jpg",
                                "src": "https://ipla.pluscdn.pl/img/p/-fTixa6Isivu/1920x1080/p/cms_thumbnails/nm/nmdgji2dxqw438tvf2hpdfc39gk1xf8t.jpg",
                                "size": {
                                    "width": 1920,
                                    "height": 1080
                                }
                            },
                            {
                                "type": "jpg",
                                "src": "https://ipla.pluscdn.pl/img/p/ulsQs4_iuSh2/1366x768/p/cms_thumbnails/nm/nmdgji2dxqw438tvf2hpdfc39gk1xf8t.jpg",
                                "size": {
                                    "width": 1366,
                                    "height": 768
                                }
                            },
                            {
                                "type": "jpg",
                                "src": "https://ipla.pluscdn.pl/img/p/Casz-TbQnV4_/768x432/p/cms_thumbnails/nm/nmdgji2dxqw438tvf2hpdfc39gk1xf8t.jpg",
                                "size": {
                                    "width": 768,
                                    "height": 432
                                }
                            },
                            {
                                "type": "jpg",
                                "src": "https://ipla.pluscdn.pl/img/p/CeEA4H3pkrNG/448x252/p/cms_thumbnails/nm/nmdgji2dxqw438tvf2hpdfc39gk1xf8t.jpg",
                                "size": {
                                    "width": 448,
                                    "height": 252
                                }
                            },
                            {
                                "type": "jpg",
                                "src": "https://ipla.pluscdn.pl/img/p/t8N88BHycs55/272x153/p/cms_thumbnails/nm/nmdgji2dxqw438tvf2hpdfc39gk1xf8t.jpg",
                                "size": {
                                    "width": 272,
                                    "height": 153
                                }
                            },
                            {
                                "type": "jpg",
                                "src": "https://ipla.pluscdn.pl/img/p/xkcb3lSg8REs/304x-/p/cms_thumbnails/nm/nmdgji2dxqw438tvf2hpdfc39gk1xf8t.jpg",
                                "size": {
                                    "width": 304,
                                    "height": 166
                                }
                            }
                        ],
                        "object": {
                            "cpid": 12,
                            "id": "cms_120_2022_02_06_148755662",
                            "title": "walka: Kacper Formela - Damian Frankiewicz",
                            "genre": "FEN 33 LOTOS Fight Night",
                            "description": "Kacpra Formela świetnie radzi sobie w kategorii piórkowej i już niedługo może stanąć przed szansą walki o mistrzowski tytuł. Najpierw jednak musi pokonać Damiana Frankiewicza, który wygrał dziewięć z ostatnich dwunastu pojedynków. Oprócz walki wieczoru widzowie zobaczą jeszcze siedem starć na zasadach MMA, w tym jedno z udziałem pań. W formule K-1 zmierzą się Dominik Zadora i Pavel Sach.",
                            "date": "2022-01-31T05:00:00Z",
                            "startTime": "2022-01-31T05:00:00Z",
                            "reporting": {
                                "activityEvents": {
                                    "contentItem": {
                                        "type": "tv_program",
                                        "value": "cms_120_2022_01_31_148564878"
                                    }
                                },
                                "playerEvents": {
                                    "contentItem": {
                                        "type": "tv_program",
                                        "value": "cms_120_2022_01_31_148564878"
                                    }
                                }
                            },
                            "posters": [],
                            "thumbnails": [
                                {
                                    "type": "jpg",
                                    "src": "http://ipla.pluscdn.pl/p/redb/q6/q6o25sgjvf4nd95ndf21c7dwck4e953b.jpg",
                                    "size": {
                                        "width": 200,
                                        "height": 200
                                    }
                                },
                                {
                                    "type": "jpg",
                                    "src": "http://ipla.pluscdn.pl/p/redb/uw/uw8jv5jc41a9rpdwxc9g4ivgvuunej31.jpg",
                                    "size": {
                                        "width": 44,
                                        "height": 44
                                    }
                                },
                                {
                                    "type": "png",
                                    "src": "http://ipla.pluscdn.pl/p/redb/x5/x5r2hwb9c2xrop2mhfe5d8zdd7pg3tdj.png",
                                    "size": {
                                        "width": 93,
                                        "height": 93
                                    }
                                },
                                {
                                    "type": "jpg",
                                    "src": "http://ipla.pluscdn.pl/p/redb/kf/kf43f9dqwftvs6np7qy497tdvi6eoeuk.jpg",
                                    "size": {
                                        "width": 980,
                                        "height": 551
                                    }
                                },
                                {
                                    "type": "jpg",
                                    "src": "http://ipla.pluscdn.pl/p/redb/g5/g5qgscnyb8jw7xhzq7gaa559m17urfqm.jpg",
                                    "size": {
                                        "width": 448,
                                        "height": 252
                                    }
                                },
                                {
                                    "type": "jpg",
                                    "src": "http://ipla.pluscdn.pl/p/redb/m8/m87ksmrmzn1osuwwaft7ky1yi1pmoiz3.jpg",
                                    "size": {
                                        "width": 768,
                                        "height": 432
                                    }
                                },
                                {
                                    "type": "jpg",
                                    "src": "http://ipla.pluscdn.pl/p/redb/65/65akd2k6ggcgnh9j4kzk2ht374zmkinv.jpg",
                                    "size": {
                                        "width": 1366,
                                        "height": 768
                                    }
                                },
                                {
                                    "type": "jpg",
                                    "src": "http://ipla.pluscdn.pl/p/redb/wb/wb17v43xif878ys6o5q7br6qcjmbggdt.jpg",
                                    "size": {
                                        "width": 1920,
                                        "height": 1080
                                    }
                                },
                                {
                                    "type": "jpg",
                                    "src": "http://ipla.pluscdn.pl/p/redb/wi/wi8v8jrvkrd15payn6qv39kfqm1v99c6.jpg",
                                    "size": {
                                        "width": 2000,
                                        "height": 1125
                                    }
                                }
                            ]
                        }
                    } 
                ],
                "offset": 0,
                "count": 1,
                "limit": 50,
                "total": 1
            },
            "id": 3854137016299357700,
            "jsonrpc": "2.0"
        }
        """.trimIndent()
    }
}
