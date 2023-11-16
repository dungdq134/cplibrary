package pl.cyfrowypolsat.cpdata.api.common.jackson

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.*
import java.io.IOException


class SortedCollectionJsonSerializer : JsonSerializer<Collection<*>?>() {
    
    @Throws(IOException::class)
    override fun serialize(value: Collection<*>?, gen: JsonGenerator, serializers: SerializerProvider?) {
        if (value == null) {
            gen.writeNull()
            return
        }
        gen.writeStartArray()
        val newValue = maybeSortValues(value)
        for (item in newValue) {
            gen.writeObject(item)
        }
        gen.writeEndArray()
    }

    @Suppress("UNCHECKED_CAST")
    private fun maybeSortValues(value: Collection<*>): Collection<*> {
        if (value.any { it == null }) return value
        if (value.any { Comparable::class.java.isAssignableFrom(it!!.javaClass).not() }) return value
        return value.sortedBy { it as Comparable<Any> }
    }
}