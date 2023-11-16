package pl.cyfrowypolsat.cpdata.api.common.jsonrpc

import android.util.Base64
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import pl.cyfrowypolsat.cpdata.api.common.jackson.SortedCollectionJsonSerializer
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class JsonRPCGetParamsBuilder
@Inject constructor() {
    val mapper = jacksonObjectMapper()

    init {
        val module = SimpleModule()
        module.addSerializer(Collection::class.java, SortedCollectionJsonSerializer())
        mapper.registerModule(module)
    }

    fun buildGetParams(jsonRPCParams: JsonRPCParams) : String {
        val json = prepareJson(jsonRPCParams)
        val base64 = toBase64(json)
        return base64
    }

    private fun prepareJson(jsonRPCParams: JsonRPCParams): String {
        val json = mapper.writeValueAsString(jsonRPCParams)
        val jsonMin = json.trim().replace(" ", "").replace("\n", "")
        Timber.d("json min: $jsonMin \n")
        return jsonMin
    }

    private fun toBase64(value: String): String {
        val valueBase64 = Base64.encodeToString(value.toByteArray(), Base64.NO_WRAP)
        return valueBase64
    }

}
