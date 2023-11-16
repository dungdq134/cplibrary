package pl.cyfrowypolsat.cpplayercore.core.teravolt

import android.content.Context
import androidx.media3.exoplayer.ExoPlayer
import com.teravolt.mobile.tvx_video_plugin.models.MatchInfo
import com.teravolt.mobile.tvx_video_plugin.models.TvxCallVideoState
import com.teravolt.mobile.tvx_video_plugin.models.TvxEntryConfig
import com.teravolt.mobile.tvx_video_plugin.models.TvxError
import com.teravolt.mobile.tvx_video_plugin.models.TvxExitConfig
import com.teravolt.mobile.tvx_video_plugin.models.TvxExitVideoState
import com.teravolt.mobile.tvx_video_plugin.models.TvxOrientation
import com.teravolt.mobile.tvx_video_plugin.models.TvxSettingsCallback
import com.teravolt.mobile.tvx_video_plugin.models.TvxTaker
import com.teravolt.mobile.tvx_video_plugin.models.TvxVideo
import com.teravolt.mobile.tvx_video_plugin.models.TvxVideoCallback
import com.teravolt.mobile.tvx_video_plugin.models.TvxVideoSeekTo
import pl.cyfrowypolsat.cpplayercore.configuration.OverlayType
import pl.cyfrowypolsat.cpplayercore.configuration.PlayerConfig
import pl.cyfrowypolsat.cpplayercore.core.extensions.getDefaultPosition
import pl.cyfrowypolsat.cpplayercore.core.sharedprefs.PlayerSettingsSharedPrefs
import timber.log.Timber

/*
    code from TVX Example
 */
class TeravoltTaker(private val playerConfig: PlayerConfig,
                    val context: Context,
                    val player: ExoPlayer,
                    val listener: TeravoltController.Listener) : TvxTaker {

    companion object {
        private val LIVE_EDGE_TOLERANCE_MS = 15000
    }

    private val sharedPrefs = PlayerSettingsSharedPrefs(context)

    private var settingsCallback: TvxSettingsCallback? = null

    init {
        Timber.d("init")
    }

    override fun createVideo(
            context: Context,
            tvxVideoCallback: TvxVideoCallback
    ): TvxVideo {
        Timber.d("createVideo")
        return TeravoltVideo(context, tvxVideoCallback, player)
    }

    override fun onReady(settingsCallback: TvxSettingsCallback) {
        Timber.d("onReady")
        this.settingsCallback = settingsCallback

        settingsCallback.setSettings(tvxSettings)

        // TvxOrientation.automatic is also the default.
        // Can be used to force a specific orientation.
        settingsCallback.setOrientation(TvxOrientation.landscape)

        // load alert settings from storage
        val alertSettings = sharedPrefs.teravoltOverlaySettings
        if (alertSettings != null) {
            settingsCallback.setAlertSettings(alertSettings)
        }
    }

    override fun onAttachTvxOverlay(availableMatches: List<MatchInfo>,
                                    callback: (TvxEntryConfig) -> Unit) {
        Timber.d("onAttachTvxOverlay ${availableMatches.toList()}")
        val selectedMatch = availableMatches.firstOrNull { it.url == playerConfig.id }
                ?: if (tvxSettings.useStaging == true) {
                    availableMatches.first()
                } else {
                    listener.onTeravoltNoAvailableMatches()
                    return
                }

        selectedMatch.let {
            Timber.d("selectedMatch: $selectedMatch")
            callback(
                    if (shouldStartLive()) {
                        TvxEntryConfig(
                                selectedMatch = it.id,
                                wantedVideoState = TvxCallVideoState.Play,
                                seekVideoTo = TvxVideoSeekTo.Live,
                                seekVideoToMs = -1
                        )
                    } else {
                        TvxEntryConfig(
                                selectedMatch = it.id,
                                wantedVideoState = TvxCallVideoState.Play,
                                seekVideoTo = TvxVideoSeekTo.Position,
                                seekVideoToMs = player.currentPosition.toInt()
                        )
                    }
            )
        }
    }

    override fun onDetachTvxOverlay(callback: (TvxExitConfig) -> Unit) {
        Timber.d("onDetachTvxOverlay")
        callback(
                TvxExitConfig(
                        videoStateOnExit = TvxExitVideoState.Dispose
                )
        )
    }

    override fun canChangeMainMatch(selectedMatch: MatchInfo,
                                    callback: (Boolean) -> Unit) {
        Timber.d("canChangeMainMatch $selectedMatch")
        listener.onTeravoltMatchChanged()
        callback(true)
    }

    override fun onChangeMainMatch(selectedMatch: MatchInfo) {
        // called when a new match is selected
        Timber.d("onChangeMainMatch $selectedMatch")
        val position = selectedMatch.position?.takeIf { it > 0 }
        listener.onTeravoltMatchSelected(TeravoltMatchItem(selectedMatch.id, selectedMatch.url.toString(), position))
    }

    override fun onFullscreen(fullscreen: Boolean) {
        Timber.d("onFullscreen $fullscreen")
        listener.onTeravoltOverlayClose()
    }

    override fun onVideoCast(active: Boolean) {
        // Example
        Timber.d("onVideoCast")
        settingsCallback?.setVideoCastActive(active)
    }

    override fun onRecord() {

    }

    override fun onSaveAlerts(alertSettings: String) {
        // example: store user alert settings in sharedPreferences
        sharedPrefs.teravoltOverlaySettings = alertSettings
    }

    override fun onClosePlayer() {
        Timber.d("onClosePlayer")
        listener.onTeravoltOverlayClose()
    }

    override fun onError(error: TvxError,
                         errorCode: Int,
                         details: String) {
        Timber.d("onError $errorCode - $error: $details")
        listener.onTeravoltOverlayError(TeravoltException(errorCode, error, details))
    }

    override fun disposeAllPlayers() {
        Timber.d("disposeAllPlayers")
        // used for debugging
    }

    private fun shouldStartLive(): Boolean {
        return playerConfig.isOverlayAutostart(OverlayType.TERAVOLT)
                && playerConfig.startPosition.startPositionMs() == PlayerConfig.DEFAULT_START_POSITION
                && isAtLiveEdge()
    }

    private fun isAtLiveEdge(): Boolean {
        val defaultPosition = player.getDefaultPosition() ?: return true
        return player.currentPosition + LIVE_EDGE_TOLERANCE_MS > defaultPosition
    }
}
