package pl.cyfrowypolsat.cpstats.core.json

import com.google.gson.*
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.*

class UTCDateSerializer : JsonSerializer<Date> {
    override fun serialize(src: Date?,
                           typeOfSrc: Type?,
                           context: JsonSerializationContext?): JsonElement {
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US).apply { timeZone = TimeZone.getTimeZone("UTC") }
        return src?.let {  JsonPrimitive(formatter.format(it)) } ?: JsonNull.INSTANCE
    }
}