package pl.cyfrowypolsat.cpcommon.domain.usecase

import android.util.Base64
import java.net.URI
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import javax.inject.Inject


class PrepareImageUrlUseCase
@Inject constructor(private val cdnUrl: String?, private val key: String?) {

    fun prepareBestImageSrc(baseSrc: String?, width: Float, height: Float? = null): String? {
        baseSrc ?: return null
        key ?: return null
        cdnUrl ?: return null

        val urlResolutionPart = prepareResolutionPart(key, baseSrc, width, height)
        val urlSuffix = URI(baseSrc).path
        return cdnUrl.plus(urlResolutionPart).plus(urlSuffix)
    }

    private fun prepareResolutionPart(key: String,
                                      baseSrc: String?,
                                      width: Float,
                                      height: Float?): String {
        val signatureData = createResolutionData(width, height).plus(URI(baseSrc).path)
        val signature = prepareSignature(signatureData, key)
        return signature.plus("/")
                .plus(createResolutionData(width, height))
    }
    private fun createResolutionData(width: Float, height: Float?): String {
        return width.toInt().toString()
                .plus("x")
                .plus(height?.toInt()?.toString() ?: "-")
    }

    private fun prepareSignature(data: String, key: String): String {
        val signingKey = SecretKeySpec(key.toByteArray(), "HmacSHA1")
        val mac = Mac.getInstance("HmacSHA1")
        mac.init(signingKey)
        val rawHmac = mac.doFinal(data.toByteArray())
        return Base64.encodeToString(rawHmac, Base64.NO_WRAP)
                .replace("+", "-")
                .replace("/", "_")
                .replace("=", ",")
                .substring(0, 12)
    }

}
