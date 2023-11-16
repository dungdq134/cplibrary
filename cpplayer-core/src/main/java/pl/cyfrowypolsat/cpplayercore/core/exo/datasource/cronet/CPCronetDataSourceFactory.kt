package pl.cyfrowypolsat.cpplayercore.core.exo.datasource.cronet

import androidx.media3.datasource.HttpDataSource
import androidx.media3.datasource.HttpDataSource.RequestProperties
import androidx.media3.datasource.cronet.CronetDataSource
import org.chromium.net.CronetEngine
import org.chromium.net.UrlRequest
import java.util.concurrent.Executor


class CPCronetDataSourceFactory(private val userAgent: String,
                                private val cronetEngine: CronetEngine,
                                private val executor: Executor,
                                private val defaultRequestProperties: RequestProperties = RequestProperties()) : HttpDataSource.Factory {

    override fun createDataSource(): HttpDataSource {
        return CPCronetDataSource(
                cronetEngine = cronetEngine,
                executor = executor,
                requestPriority = UrlRequest.Builder.REQUEST_PRIORITY_MEDIUM,
                connectTimeoutMs = CronetDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS,
                readTimeoutMs = CronetDataSource.DEFAULT_READ_TIMEOUT_MILLIS,
                resetTimeoutOnRedirects = false,
                handleSetCookieRequests = false,
                userAgent,
                defaultRequestProperties = defaultRequestProperties,
                contentTypePredicate = null,
                keepPostFor302Redirects = false)
    }

    override fun setDefaultRequestProperties(defaultRequestProperties: MutableMap<String, String>): HttpDataSource.Factory {
        this.defaultRequestProperties.clearAndSet(defaultRequestProperties)
        return this
    }
}