package pl.cyfrowypolsat.cpstats.core

import java.util.*

object TraceIdGenerator {
    fun generate(): String {
        return UUID.randomUUID().toString()
    }
}