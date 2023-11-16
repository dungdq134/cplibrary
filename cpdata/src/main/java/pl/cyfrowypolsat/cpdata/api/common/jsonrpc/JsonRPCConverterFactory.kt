package pl.cyfrowypolsat.cpdata.api.common.jsonrpc

import okhttp3.RequestBody
import okhttp3.ResponseBody
import pl.cyfrowypolsat.cpdata.api.common.session.SessionManager
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.http.Query
import timber.log.Timber
import java.io.IOException
import java.lang.reflect.Type


internal class JsonRPCConverterFactory(private val methodNamespace: String,
                                       private val jsonRPCRequestBuilder: JsonRPCRequestBuilder,
                                       private val sessionManager: SessionManager?,
                                       private val jsonRPCGetParamsBuilder: JsonRPCGetParamsBuilder? = null) : Converter.Factory() {

    override fun responseBodyConverter(type: Type?,
                                       annotations: Array<Annotation>?,
                                       retrofit: Retrofit?): Converter<ResponseBody, *>? {
        val methodAnnotation = JsonRPCUtils.findAnnotation(annotations, JsonRPC::class.java)
                ?: return null

        val method = methodAnnotation.value
        val serviceUrl = retrofit?.baseUrl()?.toUrl()?.toExternalForm() + methodNamespace + "/"

        val rpcType = Types.newParameterizedType(JsonRPCResponse::class.java, type)
        val delegate = retrofit!!.nextResponseBodyConverter<JsonRPCResponse<*>>(this, rpcType, annotations!!)

        return Converter<ResponseBody, Any> { value ->
            val (_, result, error) = delegate.convert(value) ?: throw IOException("Unexpected error")

            result ?: if (error != null) {
                throw JsonRPCException.create(error, method, serviceUrl)
            } else {
                throw IOException("Unexpected error")
            }
        }
    }

    override fun stringConverter(type: Type,
                                 annotations: Array<out Annotation>,
                                 retrofit: Retrofit): Converter<*, String>? {
        val jsonRPCGetParamsBuilder = jsonRPCGetParamsBuilder ?: return super.stringConverter(type, annotations, retrofit)

        if(annotations.any { it.annotationClass == Query::class } && type == JsonRPCParams::class.java) {
            return Converter<JsonRPCParams, String> { value ->
                val params = jsonRPCGetParamsBuilder.buildGetParams(value)
                return@Converter params
            }
        }
        return super.stringConverter(type, annotations, retrofit)
    }

    override fun requestBodyConverter(type: Type?,
                                      annotations: Array<Annotation>?,
                                      methodAnnotations: Array<Annotation>?,
                                      retrofit: Retrofit?): Converter<*, RequestBody>? {
        val methodAnnotation = JsonRPCUtils.findAnnotation(methodAnnotations!!, JsonRPC::class.java)
                ?: return null
        val method = methodAnnotation.value

        val delegate = retrofit!!.nextRequestBodyConverter<JsonRPCRequest>(this,
                JsonRPCRequest::class.java,
                annotations!!,
                methodAnnotations)

        return Converter<Any, RequestBody> { value ->
            val jsonRPCRequest = jsonRPCRequestBuilder.create(method, methodNamespace, value, sessionManager)
            delegate.convert(jsonRPCRequest)
        }
    }


}