package pl.cyfrowypolsat.cpplayercore.core.download

import android.content.Context
import androidx.media3.common.C
import androidx.media3.exoplayer.DefaultRenderersFactory
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.exoplayer.drm.*
import androidx.media3.datasource.cronet.CronetDataSource
import androidx.media3.datasource.cronet.CronetUtil
import androidx.media3.exoplayer.offline.DownloadHelper
import androidx.media3.exoplayer.offline.DownloadRequest
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.HttpDataSource
import io.reactivex.rxjava3.core.Observable
import pl.cyfrowypolsat.cpplayercore.configuration.PlayerConfig
import pl.cyfrowypolsat.cpplayercore.core.extensions.getFirstFormatWithDrmInitData
import pl.cyfrowypolsat.cpplayercore.core.extensions.logTrackSelections
import pl.cyfrowypolsat.cpplayercore.core.extensions.setAudioTracksSelection
import pl.cyfrowypolsat.cpplayercore.core.extensions.setVideoTracksSelection
import java.io.IOException
import java.util.concurrent.Executors


internal class BuildDownloadRequestUseCase(private val playerConfig: PlayerConfig,
                                           private val maxQuality: Int,
                                           private val context: Context) {

    private val mediaItem = buildMediaItem()
    private val dataSourceFactory = createDataSourceFactory(context)
    private val trackSelector = createTrackSelector(context)

    fun buildDownloadRequest(): Observable<DownloadRequest> {
        return prepareDownloadHelper().flatMap { downloadHelper ->
            downloadHelper.setVideoTracksSelection(trackSelector, maxQuality)
            downloadHelper.setAudioTracksSelection(trackSelector)
            downloadHelper.logTrackSelections()
            createDrmSessionManager(playerConfig.mediaDrmCallback)?.let {
                downloadWidevineOfflineLicense(downloadHelper, it).flatMap { keySetId ->
                    Observable.just(buildDownloadRequest(downloadHelper, keySetId)).doOnComplete {
                        downloadHelper.release()
                    }
                }
            } ?: Observable.just(buildDownloadRequest(downloadHelper)).doOnComplete {
                downloadHelper.release()
            }
        }
    }

    private fun prepareDownloadHelper(): Observable<DownloadHelper> {
        val dh = DownloadHelper.forMediaItem(
                mediaItem,
                trackSelector.parameters,
                DefaultRenderersFactory(context),
                dataSourceFactory,
                createDrmSessionManager(playerConfig.mediaDrmCallback)
        )

        return Observable.create<DownloadHelper> { emitter ->
            val downloadHelperCallback = object : DownloadHelper.Callback {
                override fun onPrepared(helper: DownloadHelper) {
                    emitter.onNext(helper)
                }

                override fun onPrepareError(helper: DownloadHelper, e: IOException) {
                    emitter.onError(e)
                }
            }
            dh.prepare(downloadHelperCallback)
        }
    }

    private fun downloadWidevineOfflineLicense(helper: DownloadHelper,
                                               drmSessionManager: DefaultDrmSessionManager): Observable<ByteArray> {
        val format = helper.getFirstFormatWithDrmInitData() ?: return Observable.error(NullPointerException())

        val offlineLicenseHelper = OfflineLicenseHelper(drmSessionManager, DrmSessionEventListener.EventDispatcher())
        val keySetId = offlineLicenseHelper.downloadLicense(format)
        return Observable.just(keySetId)
    }

    private fun createDataSourceFactory(context: Context): HttpDataSource.Factory {
        val cronetEngine = CronetUtil.buildCronetEngine(context)
        return if (cronetEngine != null) {
            CronetDataSource.Factory(cronetEngine, Executors.newSingleThreadExecutor())
        } else {
            DefaultHttpDataSource.Factory();
        }
    }

    private fun createDrmSessionManager(mediaDrmCallback: MediaDrmCallback?): DefaultDrmSessionManager? {
        return mediaDrmCallback?.let {
            DefaultDrmSessionManager.Builder()
                    .setUuidAndExoMediaDrmProvider(C.WIDEVINE_UUID, FrameworkMediaDrm.DEFAULT_PROVIDER)
                    .build(it)
        }
    }

    private fun createTrackSelector(context: Context): DefaultTrackSelector {
        val trackSelector = DefaultTrackSelector(context)
        val builder = DefaultTrackSelector.ParametersBuilder(context)
        trackSelector.setParameters(builder)
        return trackSelector
    }

    private fun buildMediaItem(): MediaItem {
        val mediaMetadata = MediaMetadata.Builder()
                .setTitle(playerConfig.title)
                .build()
        return MediaItem.Builder()
                .setMediaId(playerConfig.id)
                .setMediaMetadata(mediaMetadata)
                .setUri(playerConfig.url)
                .build()
    }

    private fun buildDownloadRequest(helper: DownloadHelper,
                                     keySetId: ByteArray? = null): DownloadRequest {
        return helper
                .getDownloadRequest(playerConfig.id, null)
                .copyWithKeySetId(keySetId)
    }
}