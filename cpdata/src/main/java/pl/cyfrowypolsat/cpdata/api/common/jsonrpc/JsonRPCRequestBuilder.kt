package pl.cyfrowypolsat.cpdata.api.common.jsonrpc

import pl.cyfrowypolsat.cpdata.api.common.session.SessionManager
import pl.cyfrowypolsat.cpdata.api.common.useragent.UserAgentDataBuilder
import pl.cyfrowypolsat.cpdata.common.exceptions.CustomException
import pl.cyfrowypolsat.cpdata.common.utils.getCurrentIsoDateTime
import pl.cyfrowypolsat.cpdata.local.SharedPrefs
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.abs

@Singleton
internal class JsonRPCRequestBuilder
@Inject constructor(private val sharedPrefs: SharedPrefs,
                    private val userAgentDataBuilder: UserAgentDataBuilder,
                    private val jsonRPCUidSessionKeyManager: JsonRPCUidSessionKeyManager) {

    companion object {
        private const val JSON_RPC_VERSION = "2.0"
        private val RANDOM = Random()
    }

    fun create(method: String,
               methodNamespace: String,
               params: Any,
               sessionManager: SessionManager?): JsonRPCRequest {
        if (params is JsonRPCParams) {
            if (sessionManager != null && sessionManager.sessionExists()) {
                params.authData = sessionManager.getSessionAuthData(method, methodNamespace)
            }
            params.message = JsonRPCParams.Message(getCurrentIsoDateTime(), jsonRPCUidSessionKeyManager.getUidSessionKey())
            params.clientId = sharedPrefs.clientId
            params.userAgentData = userAgentDataBuilder.build()
        } else {
            throw CustomException(params.javaClass.simpleName + " must inherit from the JsonRPCParams class.")
        }
        val id = abs(RANDOM.nextLong())

        return JsonRPCRequest(JSON_RPC_VERSION, id, method, params)
    }
}