package pl.cyfrowypolsat.cpdata.api.common.gson

import com.google.gson.*
import pl.cyfrowypolsat.cpdata.BuildConfig
import pl.cyfrowypolsat.cpdata.api.common.enums.Cpid
import pl.cyfrowypolsat.cpdata.api.common.model.cpidobject.*
import java.lang.reflect.Type

class CpidObjectDeserializer : JsonDeserializer<CpidObject> {

    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement,
                             typeOfT: Type,
                             context: JsonDeserializationContext): CpidObject? {
        val jObject: JsonObject? = json as JsonObject
        val cpid: Int? = jObject?.get("cpid")?.asInt

        return if (cpid != null) {
            when (cpid) {
                Cpid.CATEGORY.type -> context.deserialize<CategoryResult>(json, CategoryResult::class.java)
                Cpid.PACKET.type -> context.deserialize<PacketResult>(json, PacketResult::class.java)
                Cpid.VOD.type -> context.deserialize<MediaListItemResult>(json, MediaListItemResult::class.java)
                Cpid.LIVE.type -> context.deserialize<MediaListItemResult>(json, MediaListItemResult::class.java)
                Cpid.CHANNEL_PROGRAM.type -> context.deserialize<ChannelProgramItemResult>(json, ChannelProgramItemResult::class.java)
                else -> null
            }
        } else {
            null
        }
    }

}