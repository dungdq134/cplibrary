package pl.cyfrowypolsat.cpplayercore.mobile.pip

import android.app.Activity
import android.app.PendingIntent
import android.app.PictureInPictureParams
import android.app.RemoteAction
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.drawable.Icon
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.media3.common.Player
import androidx.media3.common.Tracks
import androidx.media3.session.MediaSession
import pl.cyfrowypolsat.cpplayercore.BuildConfig
import pl.cyfrowypolsat.cpplayercore.R
import pl.cyfrowypolsat.cpplayercore.configuration.PlayerConfig
import pl.cyfrowypolsat.cpplayercore.core.PlayerController
import pl.cyfrowypolsat.cpplayercore.mobile.exo.ExoPlayerControlView
import timber.log.Timber


interface PIPActionListener {
    fun onPIPActionClick(action: String)
}

abstract class PictureInPictureController
constructor(private val activity: Activity,
            private val playerConfig: PlayerConfig,
            private val generatePIPParams: () -> PictureInPictureParams) : PIPActionListener, Player.Listener, MediaSession.Callback {

    companion object {
        const val PIP_ACTION_REWIND = BuildConfig.LIBRARY_PACKAGE_NAME + ".action.PIP_ACTION_REWIND"
        const val PIP_ACTION_PLAY = BuildConfig.LIBRARY_PACKAGE_NAME + ".action.PIP_ACTION_PLAY"
        const val PIP_ACTION_PAUSE = BuildConfig.LIBRARY_PACKAGE_NAME + ".action.PIP_ACTION_PAUSE"
        const val PIP_ACTION_FORWARD = BuildConfig.LIBRARY_PACKAGE_NAME + ".action.PIP_ACTION_FORWARD"

        @RequiresApi(Build.VERSION_CODES.O)
        fun createPIPAction(activity: Activity,
                            iconResource: Int,
                            titleResource: Int,
                            action: String): RemoteAction {
            return RemoteAction(
                    Icon.createWithResource(activity, iconResource),
                    activity.getString(titleResource),
                    activity.getString(titleResource),
                    PendingIntent.getBroadcast(activity, 0, Intent(action), PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE))
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun generatePIPParams(activity: Activity,
                              playerController: PlayerController,
                              playerControlView: ExoPlayerControlView?): PictureInPictureParams {

            val remoteActions: MutableList<RemoteAction> = ArrayList()
            if (!playerController.isPlayingAdvert() && playerControlView?.isRewindEnabled() == true) {
                remoteActions.add(createPIPAction(activity, R.drawable.cppl_cr_common_ic_rewind_10sec,
                        R.string.cppl_cr_pip_rewind, PIP_ACTION_REWIND))
            }

            playerController.player?.let {
                if (it.isPlaying) {
                    remoteActions.add(createPIPAction(activity, R.drawable.cppl_cr_common_ic_pause,
                            R.string.cppl_cr_pip_pause, PIP_ACTION_PAUSE))
                } else {
                    remoteActions.add(createPIPAction(activity, R.drawable.cppl_cr_common_ic_play,
                            R.string.cppl_cr_pip_play, PIP_ACTION_PLAY))
                }
            }

            if (!playerController.isPlayingAdvert() && playerControlView?.isFastForwardEnabled() == true) {
                remoteActions.add(createPIPAction(activity, R.drawable.cppl_cr_common_ic_fast_forward_10sec,
                        R.string.cppl_cr_pip_forward, PIP_ACTION_FORWARD))
            }

            val params = PictureInPictureParams.Builder()
            return params
                    .setActions(remoteActions)
                    .build()

        }
    }

    abstract fun pipActionRew()
    abstract fun pipActionPlay()
    abstract fun pipActionPause()
    abstract fun pipActionFfwd()
    private var mediaSession: MediaSession? = null

    override fun onPIPActionClick(action: String) {
        when (action) {
            PIP_ACTION_REWIND -> {
                pipActionRew()
            }
            PIP_ACTION_PLAY -> {
                pipActionPlay()
            }
            PIP_ACTION_PAUSE -> {
                pipActionPause()
            }
            PIP_ACTION_FORWARD -> {
                pipActionFfwd()
            }
        }
    }

    override fun onPlaybackStateChanged(state: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            updatePIPParams()
        }
    }

    override fun onIsPlayingChanged(isPlaying: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            updatePIPParams()
        }
    }

    override fun onTracksChanged(tracks: Tracks) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            updatePIPParams()
        }
    }

    private val mRemoteActionReceiver = RemoteActionReceiver(this)

    fun registerReceiver() {
        val intentFilter = IntentFilter()
        intentFilter.addAction(PIP_ACTION_REWIND)
        intentFilter.addAction(PIP_ACTION_PLAY)
        intentFilter.addAction(PIP_ACTION_PAUSE)
        intentFilter.addAction(PIP_ACTION_FORWARD)
        activity.registerReceiver(mRemoteActionReceiver, intentFilter)
    }

    fun setMediaSession(playerController: PlayerController) {
        val player = playerController.player ?: return
        if (mediaSession != null) return

        mediaSession = MediaSession.Builder(activity, player)
                .setCallback(this)
                .build()
    }

    fun release() {
        activity.unregisterReceiver(mRemoteActionReceiver)
        mediaSession?.release()
        mediaSession = null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun enterPictureInPictureMode() {
        try {
            activity.enterPictureInPictureMode(generatePIPParams())
        } catch (exception: IllegalStateException) {
            Timber.e(exception)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updatePIPParams() {
        if (activity.isInPictureInPictureMode) {
            activity.setPictureInPictureParams(generatePIPParams())
        }
    }

    private class RemoteActionReceiver(private val pipActionListener: PIPActionListener) : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.action?.let { pipActionListener.onPIPActionClick(it) }
        }
    }
}