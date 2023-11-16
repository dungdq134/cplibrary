package pl.cyfrowypolsat.cpplayercore.core.teravolt

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import com.teravolt.mobile.tvx_video_plugin.models.AudioTrackInfo
import com.teravolt.mobile.tvx_video_plugin.models.MatchInfo
import com.teravolt.mobile.tvx_video_plugin.models.TvxVideo
import com.teravolt.mobile.tvx_video_plugin.models.TvxVideoCallback
import com.teravolt.mobile.tvx_video_plugin.models.VideoQualityInfo
import pl.cyfrowypolsat.cpplayercore.core.trackselection.TrackSelectionManager
import timber.log.Timber

/*
    code from TVX Example
 */
class TeravoltVideo(val context: Context,
                    val tvxVideoCallback: TvxVideoCallback,
                    val player: ExoPlayer) : TvxVideo {
    companion object{
        private val SECOND = 1000L
        private val CUSTOM_DELAY_PARAM_NAME = "delay_to_add"
        private val CUSTOM_DELAY_WORKAROUND_ENABLED = false
    }

    private var trackSelectionManager: TrackSelectionManager? = null

    var trackSelector: DefaultTrackSelector? = null
    var customDuration: Long = 0
    var customDelay: Long = 0

    var tvxVideoListener: Player.Listener? = object : Player.Listener {
        override fun onIsPlayingChanged(isPlaying: Boolean) {
            tvxVideoCallback.setPlaying(isPlaying)
            super.onIsPlayingChanged(isPlaying)
        }

        override fun onPlaybackStateChanged(playbackState: Int) {
            tvxVideoCallback.setBuffering(playbackState == Player.STATE_BUFFERING)
            super.onPlaybackStateChanged(playbackState)
        }
    }

    override fun prepareUrl(context: Context,
                            matchInfo: MatchInfo,
                            done: () -> Unit) {
        Timber.d("prepareUrl: $matchInfo")
        customDelay = getCustomDelayValue(matchInfo)

        tvxVideoListener?.let { player.addListener(it) }
        trackSelectionManager = TrackSelectionManager.build(trackSelector, context)

        val mainHandler = Handler(Looper.getMainLooper())

        var lastDuration = player.duration
        var lastPosition = player.currentPosition
        var lastWidth = player.videoFormat?.width ?: 0
        var lastHeight = player.videoFormat?.height ?: 0
        customDuration = lastDuration

        tvxVideoCallback.setDuration(kotlin.math.max(0, player.duration))
        tvxVideoCallback.setPosition(player.currentPosition)
        tvxVideoCallback.setVideoWidth(player.videoFormat?.width ?: 0)
        tvxVideoCallback.setVideoHeight(player.videoFormat?.height ?: 0)

        mainHandler.post(object : Runnable {
            override fun run() {
                val playerDuration = player.duration + customDelay
                val playerCurrentPosition = player.currentPosition + customDelay

                if (lastDuration != playerDuration) {
                    tvxVideoCallback.setDuration(playerDuration)
                    lastDuration = playerDuration
                    customDuration = lastDuration
                } else {
                    customDuration += SECOND
                    tvxVideoCallback.setDuration(customDuration)
                }
                if (lastPosition != playerCurrentPosition) {
                    tvxVideoCallback.setPosition(playerCurrentPosition)
                    lastPosition = playerCurrentPosition
                }
                if (lastWidth != (player.videoFormat?.width ?: 0)) {
                    tvxVideoCallback.setVideoWidth(player.videoFormat?.width ?: 0)
                    lastWidth = player.videoFormat?.width ?: 0
                }
                if (lastHeight != (player.videoFormat?.height ?: 0)) {
                    tvxVideoCallback.setVideoHeight(player.videoFormat?.height ?: 0)
                    lastHeight = player.videoFormat?.height ?: 0
                }

                mainHandler.postDelayed(this, SECOND)
            }
        })
        done()
    }

    override fun seekTo(position: Long,
                        done: () -> Unit) {
        Timber.d("seekTo($position)")
        player.seekTo(position + customDelay)
        done()
    }

    override fun setVolume(volume: Double) {
        player.volume = volume.toFloat()
    }

    override fun pause() {
        player.playWhenReady = false
        tvxVideoCallback.setPlaying(false)
    }

    override fun play() {
        player.playWhenReady = true
        tvxVideoCallback.setPlaying(true)
    }

    override fun disposePlayer() {
        Timber.d("disposePlayer")
        tvxVideoListener = null
    }

    override fun getAvailableAudioTracks(): List<AudioTrackInfo> {
        return listOf() //i think it is unnecessary
    }

    override fun getAvailableVideoQualities(): List<VideoQualityInfo> {
        return listOf() //i think it is unnecessary
    }

    override fun getView(context: Context): View {
        //for renderAsOverlay = true this method is unnecessary - return anything
        return View(context)
    }

    override fun selectAudioTrack(audioTrackId: String) {
        //do nothing - i dont see any quality change option
    }

    override fun selectVideoQuality(videoQualityId: String) {
        //do nothing - i dont see any quality change option
    }

    private fun getCustomDelayValue(matchInfo: MatchInfo) : Long {
        if(!CUSTOM_DELAY_WORKAROUND_ENABLED) {
            return 0L
        }
        val delayToAddParam = matchInfo.metaData.firstOrNull { it.contains("$CUSTOM_DELAY_PARAM_NAME=") }?.split("=")
        if(delayToAddParam != null && delayToAddParam.size == 2){
            val delayToAddValue = delayToAddParam[1].toLongOrNull() ?: 0L
            return delayToAddValue * -1
        }
        return 0L
    }
}