package pl.cyfrowypolsat.cpplayer.drm

import android.net.Uri
import android.util.Base64
import androidx.media3.common.C
import androidx.media3.common.util.Util
import androidx.media3.exoplayer.drm.ExoMediaDrm
import androidx.media3.exoplayer.drm.MediaDrmCallback
import androidx.media3.datasource.DataSourceInputStream
import androidx.media3.datasource.DataSpec
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.HttpDataSource
import pl.cyfrowypolsat.cpdata.api.drm.DrmService
import pl.cyfrowypolsat.cpdata.api.drm.request.GetWidevineLicenseParams
import pl.cyfrowypolsat.cpdata.api.drm.response.GetWidevineLicenseResult
import java.io.IOException
import java.util.*


class WidevineDrmCallback(private val drmService: DrmService,
                          private val userAgent: String,
                          private val getWidevineLicenseUrl: String,
                          private val getWidevineLicenseParams: GetWidevineLicenseParams,
                          private val licenseListeners: List<LicenseListener> = listOf()) : MediaDrmCallback {

    override fun executeProvisionRequest(uuid: UUID,
                                         request: ExoMediaDrm.ProvisionRequest): ByteArray {
        val url = request.defaultUrl + "&signedRequest=" + String(request.data)
        val httpDataSourceFactory = DefaultHttpDataSource.Factory()
                .setUserAgent(userAgent)
        return executePost(httpDataSourceFactory, url, Util.EMPTY_BYTE_ARRAY)
    }

    override fun executeKeyRequest(uuid: UUID, request: ExoMediaDrm.KeyRequest): ByteArray {
        licenseListeners.forEach { it.onLicenseRequestStarted() }
        val `object` = Base64.encodeToString(request.data, Base64.URL_SAFE)
        getWidevineLicenseParams.`object` = `object`

        try {
            val result = drmService.getWidevineLicense(getWidevineLicenseUrl, getWidevineLicenseParams).blockingFirst()
            val license = result.`object`.license
            licenseListeners.forEach { it.onLicenseRequestCompleted(getLicenseInfo(result)) }

            return Base64.decode(license, Base64.DEFAULT)
        } catch (ex: Exception) {
            licenseListeners.forEach { it.onLicenseRequestError(ex) }
            throw ex
        }
    }

    @Throws(IOException::class)
    private fun executePost(dataSourceFactory: HttpDataSource.Factory,
                            url: String,
                            data: ByteArray): ByteArray {
        val dataSource = dataSourceFactory.createDataSource()
        val dataSpec = DataSpec.Builder()
            .setUri(Uri.parse(url))
            .setHttpMethod(DataSpec.HTTP_METHOD_POST)
            .setHttpBody(data)
            .setPosition(0)
            .setLength(C.LENGTH_UNSET.toLong())
            .setKey(null)
            .build()
        val inputStream = DataSourceInputStream(dataSource, dataSpec)
        try {
            return Util.toByteArray(inputStream)
        } finally {
            Util.closeQuietly(inputStream)
        }
    }

    private fun getLicenseInfo(result: GetWidevineLicenseResult): LicenseInfo {
        val cacInfo = result.accessPolicy?.cac?.let {
            CacInfo(serviceUrl = it.service,
                    authToken = it.authToken,
                    authTokenServiceUrl = it.authTokenService.firstVersion.url)
        }
        return LicenseInfo(licenseId = result.id,
                sellModel = result.reporting?.redevents?.sellModel,
                cacInfo = cacInfo)
    }

}