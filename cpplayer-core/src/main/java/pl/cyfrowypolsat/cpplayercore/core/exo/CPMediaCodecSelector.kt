package pl.cyfrowypolsat.cpplayercore.core.exo

import androidx.media3.common.MimeTypes
import androidx.media3.exoplayer.mediacodec.MediaCodecInfo
import androidx.media3.exoplayer.mediacodec.MediaCodecSelector
import androidx.media3.exoplayer.mediacodec.MediaCodecUtil


class CPMediaCodecSelector : MediaCodecSelector {

    companion object {
        private val BLACK_LISTED_MIME_TYPES = listOf(MimeTypes.AUDIO_E_AC3)
    }

    override fun getDecoderInfos(mimeType: String,
                                 requiresSecureDecoder: Boolean,
                                 requiresTunnelingDecoder: Boolean): List<MediaCodecInfo> {
        return if (BLACK_LISTED_MIME_TYPES.contains(mimeType)) {
            listOf()
        } else {
            MediaCodecUtil.getDecoderInfos(mimeType, requiresSecureDecoder, requiresTunnelingDecoder)
        }
    }

}