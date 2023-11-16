package pl.cyfrowypolsat.cpplayercore.core.teravolt

import android.app.Activity
import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.media3.exoplayer.ExoPlayer
import com.teravolt.mobile.tvx_video_plugin.TvxEngine
import com.teravolt.mobile.tvx_video_plugin.TvxTakerConnector
import com.teravolt.mobile.tvx_video_plugin.TvxView
import pl.cyfrowypolsat.cpplayercore.configuration.PlayerConfig
import timber.log.Timber

class TeravoltController(private val playerConfig: PlayerConfig,
                         val context: Context,
                         val player: ExoPlayer,
                         val tvxEngine: TvxEngine,
                         val listener: Listener) {

    interface Listener {
        fun onTeravoltNoAvailableMatches()
        fun onTeravoltOverlayError(exception: TeravoltException)
        fun onTeravoltOverlayClose()
        fun onTeravoltMatchChanged()
        fun onTeravoltMatchSelected(teravoltMatchItem: TeravoltMatchItem)
    }

    private var teravoltTaker: TeravoltTaker? = null

    fun prepare(tvxView: TvxView,
                activity: Activity,
                lifecycle: Lifecycle) : Boolean {
        try{
            if (!tvxEngine.isEngineInitialized || TvxTakerConnector.tvxTaker == null) {
                teravoltTaker = TeravoltTaker(playerConfig, context, player, listener)
                TvxTakerConnector.tvxTaker = teravoltTaker
                TvxEngine.preheatEngine(context)
            }

            tvxEngine.attachViewToEngine(tvxView, activity, lifecycle)
            tvxEngine.resume()
            return true
        }catch (t: Throwable) {
            Timber.e(t)
        }
        return false
    }

    fun resume() {
        tvxEngine.resume()
    }

    fun pause() {
        tvxEngine.pause()
    }

    fun stop() {
        tvxEngine.stop()
    }

    fun release() {
        TvxTakerConnector.tvxTaker?.let {
            try {
                tvxEngine.detachViewFromEngine()
                tvxEngine.stop()
                tvxEngine.destroy()
            } catch (t: Throwable) {
                Timber.e(t)
            }
        }
        TvxTakerConnector.tvxTaker = null
    }
}