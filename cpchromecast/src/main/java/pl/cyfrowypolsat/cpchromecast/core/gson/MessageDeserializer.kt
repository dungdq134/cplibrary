package pl.cyfrowypolsat.cpchromecast.core.gson

import com.google.gson.*
import pl.cyfrowypolsat.cpchromecast.data.enums.MessageType
import pl.cyfrowypolsat.cpchromecast.data.model.received.*
import java.lang.reflect.Type

class MessageDeserializer : JsonDeserializer<Message> {

    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Message? {

        val jObject: JsonObject? = json as JsonObject
        val type: String? = jObject?.get("type")?.asString

        return if (type != null) {
            when (type) {
                MessageType.ERROR.type -> context.deserialize<Error>(json, Error::class.java)
                MessageType.PLAYBACK_READY.type -> context.deserialize<PlaybackReady>(json, PlaybackReady::class.java)
                MessageType.LINEAR_AD_STARTED.type -> context.deserialize<LinearAdStarted>(json, LinearAdStarted::class.java)
                MessageType.LINEAR_AD_POD_STARTED.type -> context.deserialize<LinearAdPodStarted>(json, LinearAdPodStarted::class.java)
                MessageType.LINEAR_AD_POD_ENDED.type -> context.deserialize<LinearAdPodEnded>(json, LinearAdPodEnded::class.java)
                MessageType.RECEIVER_ENVIRONMENT.type -> context.deserialize<ReceiverEnvironment>(json, ReceiverEnvironment::class.java)
                else -> null
            }
        } else {
            null
        }
    }

}