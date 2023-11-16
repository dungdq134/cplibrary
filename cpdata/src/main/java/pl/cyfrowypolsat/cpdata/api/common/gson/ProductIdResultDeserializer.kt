package pl.cyfrowypolsat.cpdata.api.common.gson

import com.google.gson.*
import pl.cyfrowypolsat.cpdata.api.payments.response.PrePurchaseProductResult
import pl.cyfrowypolsat.cpdata.api.payments.response.ProductIdResult
import pl.cyfrowypolsat.cpdata.api.payments.response.SimpleProductIdResult
import java.lang.reflect.Type

class ProductIdResultDeserializer : JsonDeserializer<ProductIdResult> {

    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement,
                             typeOfT: Type,
                             context: JsonDeserializationContext): ProductIdResult? {
        val jObject: JsonObject? = json as JsonObject
        val productName: String? = jObject?.get("name")?.asString

        return if (productName != null) {
            context.deserialize<PrePurchaseProductResult>(json, PrePurchaseProductResult::class.java)
        } else {
            context.deserialize<SimpleProductIdResult>(json, SimpleProductIdResult::class.java)
        }
    }

}