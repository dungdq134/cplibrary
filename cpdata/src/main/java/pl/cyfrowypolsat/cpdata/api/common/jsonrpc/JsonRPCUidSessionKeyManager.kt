package pl.cyfrowypolsat.cpdata.api.common.jsonrpc

import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class JsonRPCUidSessionKeyManager
@Inject constructor() {

    private var requestCounter = 0
    private var uidSessionKey: String? = null

    fun getUidSessionKey(): String {
        var uuid = if (uidSessionKey != null) {
            uidSessionKey!!
        } else {
            UUID.randomUUID().toString()
        }
        uidSessionKey = uuid

        uuid += String.format(Locale("pl"), "-%03d", requestCounter)
        requestCounter++
        return uuid
    }

}