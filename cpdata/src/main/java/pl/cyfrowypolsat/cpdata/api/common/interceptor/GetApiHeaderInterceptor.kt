package pl.cyfrowypolsat.cpdata.api.common.interceptor

import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import pl.cyfrowypolsat.cpdata.api.common.jsonrpc.JsonRPCUidSessionKeyManager
import pl.cyfrowypolsat.cpdata.api.common.session.SessionManager
import pl.cyfrowypolsat.cpdata.api.common.useragent.UserAgentDataBuilder
import pl.cyfrowypolsat.cpdata.api.system.response.Services
import pl.cyfrowypolsat.cpdata.common.manager.AccountManager
import pl.cyfrowypolsat.cpdata.common.utils.DeviceIdGenerator
import pl.cyfrowypolsat.cpdata.common.utils.getCurrentIsoDateTime
import pl.cyfrowypolsat.cpdata.local.SharedPrefs
import java.io.IOException
import javax.inject.Inject

internal class GetApiHeaderInterceptor
@Inject constructor(private val sharedPrefs: SharedPrefs,
                    private val userAgentDataBuilder: UserAgentDataBuilder,
                    private val deviceIdGenerator: DeviceIdGenerator,
                    private val sessionManager: SessionManager,
                    private val accountManager: AccountManager,
                    private val jsonRPCUidSessionKeyManager: JsonRPCUidSessionKeyManager) : Interceptor {


    private val SUPPORTED_NAMESPACE_LIST = Services.supportedNamespaceList()

    companion object {
        const val USER_AGENT_HEADER_DATA_PORTAL = "X-User-Agent-Data-Portal"
        const val USER_AGENT_HEADER_DATA_DEVICE_TYPE = "X-User-Agent-Data-Device-Type"
        const val USER_AGENT_HEADER_DATA_APPLICATION = "X-User-Agent-Data-Application"
        const val USER_AGENT_HEADER_DATA_PLAYER = "X-User-Agent-Data-Player"
        const val USER_AGENT_HEADER_DATA_BUILD = "X-User-Agent-Data-Build"
        const val USER_AGENT_HEADER_DATA_OS = "X-User-Agent-Data-Os"
        const val USER_AGENT_HEADER_DATA_OS_INFO = "X-User-Agent-Data-Os-Info"
        const val USER_AGENT_HEADER_DATA_WIDEVINE = "X-User-Agent-Data-Widevine"

        const val DEVICE_ID_HEADER_TYPE = "X-Device-Id-Type"
        const val DEVICE_ID_HEADER_VALUE = "X-Device-Id-Value"

        const val CLIENT_ID_HEADER = "X-Client-Id"

        const val CLIENT_CONTEXT_TOKEN_HEADER = "X-Client-Context-Token"

        const val MESSAGE_HEADER_ID = "X-Message-Id"
        const val MESSAGE_HEADER_TIMESTAMP = "X-Message-Timestamp"

        const val AUTH_DATA_HEADER_SESSION_ID = "X-Auth-Data-Session-Id"
        const val AUTH_DATA_HEADER_SESSION_TOKEN = "X-Auth-Data-Session-Token"
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val url = chain.request().url
        val methodName = getMethodFromUrl(url)
        val namespace = getNamespaceFromUrl(url)
        val requestBuilder = chain.request().newBuilder()

        addUserAgentDataHeader(requestBuilder)
        addDeviceIdHeader(requestBuilder)
        addClientHeader(requestBuilder)
        addMessageIdHeader(requestBuilder)
        addSessionHeader(requestBuilder, namespace, methodName)

        return chain.proceed(requestBuilder.build())
    }

    private fun addUserAgentDataHeader(requestBuilder: Request.Builder) {
        userAgentDataBuilder.build().let {
            requestBuilder.addHeader(USER_AGENT_HEADER_DATA_PORTAL, it.portal)
            requestBuilder.addHeader(USER_AGENT_HEADER_DATA_DEVICE_TYPE, it.deviceType)
            requestBuilder.addHeader(USER_AGENT_HEADER_DATA_APPLICATION, it.application)
            requestBuilder.addHeader(USER_AGENT_HEADER_DATA_PLAYER, it.player)
            requestBuilder.addHeader(USER_AGENT_HEADER_DATA_BUILD, it.build.toString())
            requestBuilder.addHeader(USER_AGENT_HEADER_DATA_OS, it.os)
            requestBuilder.addHeader(USER_AGENT_HEADER_DATA_WIDEVINE, it.widevine.toString())
        }
    }

    private fun addDeviceIdHeader(requestBuilder: Request.Builder) {
        deviceIdGenerator.generateDeviceId().let {
            requestBuilder.addHeader(DEVICE_ID_HEADER_TYPE, it.type)
            requestBuilder.addHeader(DEVICE_ID_HEADER_VALUE, it.value)
        }
    }

    private fun addClientHeader(requestBuilder: Request.Builder) {
        sharedPrefs.clientId.let { if (it.isNotEmpty()) requestBuilder.addHeader(CLIENT_ID_HEADER, it) }
        sharedPrefs.clientContextToken.let { if (it.isNotEmpty()) requestBuilder.addHeader(CLIENT_CONTEXT_TOKEN_HEADER, it) }
    }

    private fun addMessageIdHeader(requestBuilder: Request.Builder) {
        requestBuilder.addHeader(MESSAGE_HEADER_ID, jsonRPCUidSessionKeyManager.getUidSessionKey())
        requestBuilder.addHeader(MESSAGE_HEADER_TIMESTAMP, getCurrentIsoDateTime())
    }

    private fun addSessionHeader(requestBuilder: Request.Builder,
                                 methodNamespace: String?,
                                 methodName: String?) {
        if (sessionManager.sessionExists() && methodName != null && methodNamespace != null) {
            accountManager.getSession()?.id?.let { requestBuilder.addHeader(AUTH_DATA_HEADER_SESSION_ID, it) }
            val sessionToken = sessionManager.createSessionToken(methodName, methodNamespace)
            requestBuilder.addHeader(AUTH_DATA_HEADER_SESSION_TOKEN, sessionToken)
        }
    }

    private fun getNamespaceFromUrl(url: HttpUrl): String? {
        url.encodedPathSegments.forEach {
            if (SUPPORTED_NAMESPACE_LIST.contains(it)) {
                return it
            }
        }
        return null
    }

    private fun getMethodFromUrl(url: HttpUrl): String? {
        return url.queryParameter("method").toString()
    }

}
