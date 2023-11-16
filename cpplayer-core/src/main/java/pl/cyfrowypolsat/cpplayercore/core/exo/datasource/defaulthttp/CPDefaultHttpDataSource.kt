package pl.cyfrowypolsat.cpplayercore.core.exo.datasource.defaulthttp

import android.net.Uri
import androidx.media3.datasource.DataSpec
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.HttpDataSource
import java.util.Date

class CPDefaultHttpDataSource(userAgent: String,
                              connectTimeoutMillis: Int,
                              readTimeoutMillis: Int,
                              allowCrossProtocolRedirects: Boolean,
                              defaultRequestProperties: HttpDataSource.RequestProperties?) : DefaultHttpDataSource(userAgent, connectTimeoutMillis, readTimeoutMillis, allowCrossProtocolRedirects, defaultRequestProperties) {
    companion object {
        const val RESET_TIMEOUT = 60000L
    }

    private var attachToNodeTime: Date? = null
    private var manifestUrl: String? = null


    override fun open(dataSpec: DataSpec): Long {
        return makeRequest(dataSpec)
    }

    private fun makeRequest(dataSpec: DataSpec): Long {
        if (checkIfRequestForManifest(dataSpec.uri)) {
            if (manifestUrl == null) {
                manifestUrl = dataSpec.uri.toString()
                val result = super.open(dataSpec)
                attachToNodeTime = Date()
                return result
            } else if (checkIfShouldDetachFromNode()) {
                val newDataSpec = dataSpec.withUri(Uri.parse(manifestUrl))
                val result = super.open(newDataSpec)
                attachToNodeTime = Date()
                return result
            }
        }

        return super.open(dataSpec)
    }

    private fun checkIfShouldDetachFromNode(): Boolean {
        attachToNodeTime?.let {
            return Date().time >= it.time + RESET_TIMEOUT
        }
        return false
    }


    private fun checkIfRequestForManifest(uri: Uri): Boolean {
        if (uri.toString().contains(".mpd")) {
            return true
        }
        return false
    }
}