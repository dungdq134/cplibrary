package pl.cyfrowypolsat.cpdata.api.common.gson

import com.google.gson.*
import pl.cyfrowypolsat.cpdata.api.navigation.response.preplaydata.PlayerOverlays
import pl.cyfrowypolsat.cpdata.api.navigation.response.preplaydata.TeravoltPlayerOverlay
import java.lang.reflect.Type

class PlayerOverlaysObjectDeserializer : JsonDeserializer<PlayerOverlays> {

    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement,
                             typeOfT: Type,
                             context: JsonDeserializationContext): PlayerOverlays {
        val jArray: JsonArray = if (json.isJsonArray) { json.asJsonArray } else {
            return PlayerOverlays(null)
        }

        for (json in jArray) {
            val jObject: JsonObject = json as JsonObject
            val type: String? = jObject.get("type")?.asString
            if(type == "teravolt") {
                val obj = context.deserialize<TeravoltPlayerOverlay>(json, TeravoltPlayerOverlay::class.java)
                return PlayerOverlays(obj)
            }
        }
        return PlayerOverlays(null)
    }

}