package pl.cyfrowypolsat.cpdata.common.mock.payments

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okio.Buffer
import pl.cyfrowypolsat.cpdata.BuildConfig
import javax.inject.Inject

internal class GetPaymentCardsInterceptor
@Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var response = chain.proceed(chain.request())
        val requestBody: String = getRequestString(chain.request())
        if (BuildConfig.DEBUG) {
            if (requestBody.contains("\"getPaymentCards\"")) {
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
   "result":[
      {
         "id":"123123123",
         "maskedNumber":"xxxx xxxx xxxx 1111",
         "brand":{
            "name":"name",
            "type":"visa"
         },
         "expirationDate":{
            "month":10,
            "year":2020
         },
         "operator":"dotpay",
		 "default": false
      },
	  {
         "id":"45654645646",
         "maskedNumber":"xxxx xxxx xxxx 2222",
         "brand":{
            "name":"name",
            "type":"visa"
         },
         "expirationDate":{
            "month":11,
            "year":2020
         },
         "operator":"dotpay",
		 "default": true
      },
	  {
         "id":"68767686768",
         "maskedNumber":"xxxx xxxx xxxx 3333",
         "brand":{
            "name":"name",
            "type":"visa"
         },
         "expirationDate":{
            "month":12,
            "year":2020
         },
         "operator":"dotpay",
		 "default": false
      },
	  {
         "id":"3523534545",
         "maskedNumber":"xxxx xxxx xxxx 4444",
         "brand":{
            "name":"name",
            "type":"visa"
         },
         "expirationDate":{
            "month":1,
            "year":2020
         },
         "operator":"dotpay",
		 "default": false
      },
	  {
         "id":"4564564564",
         "maskedNumber":"xxxx xxxx xxxx 5555",
         "brand":{
            "name":"name",
            "type":"visa"
         },
         "expirationDate":{
            "month":2,
            "year":2020
         },
         "operator":"dotpay",
		 "default": false
      },
	  {
         "id":"67767768678",
         "maskedNumber":"xxxx xxxx xxxx 6666",
         "brand":{
            "name":"name",
            "type":"visa"
         },
         "expirationDate":{
            "month":3,
            "year":2020
         },
         "operator":"dotpay",
		 "default": false
      },
	  {
         "id":"97887978979",
         "maskedNumber":"xxxx xxxx xxxx 7777",
         "brand":{
            "name":"name",
            "type":"visa"
         },
         "expirationDate":{
            "month":4,
            "year":2020
         },
         "operator":"dotpay",
		 "default": false
      },
	  {
         "id":"97656342354",
         "maskedNumber":"xxxx xxxx xxxx 8888",
         "brand":{
            "name":"name",
            "type":"visa"
         },
         "expirationDate":{
            "month":5,
            "year":2020
         },
         "operator":"dotpay",
		 "default": false
      }
   ],
   "id":5854043051966459311,
   "jsonrpc":"2.0"
}"""
    }
}
