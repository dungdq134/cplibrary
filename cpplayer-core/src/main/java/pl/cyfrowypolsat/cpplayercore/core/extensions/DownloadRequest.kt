package pl.cyfrowypolsat.cpplayercore.core.extensions

import androidx.media3.exoplayer.offline.DownloadRequest


fun DownloadRequest.copyWithData(data: ByteArray): DownloadRequest {
    return DownloadRequest.Builder(id, uri)
            .setMimeType(mimeType)
            .setStreamKeys(streamKeys)
            .setKeySetId(keySetId)
            .setCustomCacheKey(customCacheKey)
            .setData(data)
            .build()
}