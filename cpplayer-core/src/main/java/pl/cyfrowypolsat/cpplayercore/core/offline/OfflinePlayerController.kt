package pl.cyfrowypolsat.cpplayercore.core.offline

import android.content.Context
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.drm.DefaultDrmSessionManager
import androidx.media3.exoplayer.drm.DrmSessionManagerProvider
import androidx.media3.exoplayer.drm.FrameworkMediaDrm
import androidx.media3.exoplayer.offline.DownloadRequest
import androidx.media3.common.AdViewProvider
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.datasource.cache.SimpleCache
import pl.cyfrowypolsat.cpplayercore.configuration.PlayerConfig
import pl.cyfrowypolsat.cpplayercore.core.PlayerController
import pl.cyfrowypolsat.cpplayercore.core.exo.datasource.defaulthttp.CPDefaultHttpDataSourceFactory
import pl.cyfrowypolsat.cpplayercore.events.playerview.PlayerViewListener


class OfflinePlayerController(private val downloadCache: SimpleCache,
                              private val downloadRequest: DownloadRequest,
                              playerConfig: PlayerConfig,
                              adViewProvider: AdViewProvider,
                              context: Context,
                              playerViewListener: PlayerViewListener? = null) : PlayerController(playerConfig, adViewProvider, context, playerViewListener) {

    override fun createDataSourceFactory(): DefaultDataSource.Factory {
        return DefaultDataSource.Factory(context, buildReadOnlyCacheDataSource())
    }

    private fun buildReadOnlyCacheDataSource(): CacheDataSource.Factory {
        val upstreamFactory = DefaultDataSource.Factory(context, CPDefaultHttpDataSourceFactory(playerConfig.userAgent))
        return CacheDataSource.Factory()
                .setCache(downloadCache)
                .setUpstreamDataSourceFactory(upstreamFactory)
                .setCacheWriteDataSinkFactory(null)
                .setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)
    }

    override fun createDrmSessionManagerProvider(): DrmSessionManagerProvider {
        return DrmSessionManagerProvider {
            val offlineDrmSessionManager: DefaultDrmSessionManager = DefaultDrmSessionManager.Builder()
                    .setUuidAndExoMediaDrmProvider(C.WIDEVINE_UUID, FrameworkMediaDrm.DEFAULT_PROVIDER)
                    .build(OfflineMediaDrmCallback())
            downloadRequest.keySetId?.let { offlineDrmSessionManager.setMode(DefaultDrmSessionManager.MODE_QUERY, it) }
            offlineDrmSessionManager
        }
    }

    override fun createMediaItem(): MediaItem? {
        return downloadRequest.toMediaItem().buildUpon()
                .setSubtitleConfigurations(createSubtitleConfigurations())
                .build()
    }
}