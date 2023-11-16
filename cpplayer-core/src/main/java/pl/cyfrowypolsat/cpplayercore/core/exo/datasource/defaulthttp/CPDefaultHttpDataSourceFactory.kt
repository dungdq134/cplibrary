package pl.cyfrowypolsat.cpplayercore.core.exo.datasource.defaulthttp

import androidx.media3.datasource.DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS
import androidx.media3.datasource.DefaultHttpDataSource.DEFAULT_READ_TIMEOUT_MILLIS
import androidx.media3.datasource.HttpDataSource
import androidx.media3.datasource.TransferListener

/**
 * @param userAgent The User-Agent string that should be used.
 * @param transferListener An optional TransferListener.
 * @param connectTimeoutMillis The connection timeout that should be used when requesting remote
 * data, in milliseconds. A timeout of zero is interpreted as an infinite timeout.
 * @param readTimeoutMillis The read timeout that should be used when requesting remote data, in
 * milliseconds. A timeout of zero is interpreted as an infinite timeout.
 * @param allowCrossProtocolRedirects Whether cross-protocol redirects (i.e. redirects from HTTP
 * to HTTPS and vice versa) are enabled.
 */
class CPDefaultHttpDataSourceFactory(val userAgent: String,
                                     val transferListener: TransferListener? = null,
                                     val connectTimeoutMillis: Int = DEFAULT_CONNECT_TIMEOUT_MILLIS,
                                     val readTimeoutMillis: Int = DEFAULT_READ_TIMEOUT_MILLIS,
                                     val allowCrossProtocolRedirects: Boolean = false) : HttpDataSource.BaseFactory() {

    override fun createDataSourceInternal(defaultRequestProperties: HttpDataSource.RequestProperties): HttpDataSource {
        val dataSource = CPDefaultHttpDataSource(
                userAgent,
                connectTimeoutMillis,
                readTimeoutMillis,
                allowCrossProtocolRedirects,
                defaultRequestProperties)
        if (transferListener != null) {
            dataSource.addTransferListener(transferListener)
        }
        return dataSource
    }
}
