package pl.cyfrowypolsat.cpplayercore.core.exo.datasource.cronet

import android.net.Uri
import androidx.media3.datasource.DataSpec
import androidx.media3.datasource.HttpDataSource
import androidx.media3.datasource.cronet.CronetDataSource
import com.google.common.base.Predicate
import org.chromium.net.CronetEngine
import java.util.Date
import java.util.concurrent.Executor

class CPCronetDataSource(val cronetEngine: CronetEngine,
                         val executor: Executor,
                         val requestPriority: Int,
                         val connectTimeoutMs: Int,
                         val readTimeoutMs: Int,
                         val resetTimeoutOnRedirects: Boolean,
                         val handleSetCookieRequests: Boolean,
                         val userAgent: String?,
                         val defaultRequestProperties: HttpDataSource.RequestProperties?,
                         val contentTypePredicate: Predicate<String>?,
                         val keepPostFor302Redirects: Boolean) : CronetDataSource(cronetEngine, executor, requestPriority, connectTimeoutMs, readTimeoutMs, resetTimeoutOnRedirects, handleSetCookieRequests, userAgent, defaultRequestProperties, contentTypePredicate, keepPostFor302Redirects) {

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