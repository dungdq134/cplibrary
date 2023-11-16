package pl.cyfrowypolsat.cpplayercore.mobile

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.Handler
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commitNow
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.transition.Fade
import androidx.media3.common.Player
import androidx.media3.datasource.cache.SimpleCache
import androidx.media3.exoplayer.offline.DownloadRequest
import androidx.media3.ui.PlayerControlView
import androidx.media3.ui.SubtitleView
import com.teravolt.mobile.tvx_video_plugin.TvxViewInterface
import io.flutter.embedding.android.FlutterSurfaceView
import io.flutter.embedding.android.FlutterView
import pl.cyfrowypolsat.cpcommon.presentation.extensions.adjustVisibleOrGone
import pl.cyfrowypolsat.cpcommon.presentation.extensions.isFragmentAttached
import pl.cyfrowypolsat.cpcommon.presentation.extensions.isInPipOrMultiWindowMode
import pl.cyfrowypolsat.cpplayercore.R
import pl.cyfrowypolsat.cpplayercore.configuration.OverlayType
import pl.cyfrowypolsat.cpplayercore.configuration.PlayerConfig
import pl.cyfrowypolsat.cpplayercore.core.PlayerController
import pl.cyfrowypolsat.cpplayercore.core.PlayerException
import pl.cyfrowypolsat.cpplayercore.core.offline.OfflinePlayerController
import pl.cyfrowypolsat.cpplayercore.core.seealso.SeeAlsoItem
import pl.cyfrowypolsat.cpplayercore.core.subtitlesettings.contrast.SubtitleContrastSettingsManager
import pl.cyfrowypolsat.cpplayercore.core.subtitlesettings.textsize.SubtitleSizeSettingsManager
import pl.cyfrowypolsat.cpplayercore.core.teravolt.TeravoltController
import pl.cyfrowypolsat.cpplayercore.core.teravolt.TeravoltException
import pl.cyfrowypolsat.cpplayercore.core.teravolt.TeravoltMatchItem
import pl.cyfrowypolsat.cpplayercore.core.trackselection.TrackSelectionManager
import pl.cyfrowypolsat.cpplayercore.events.player.PlayerListener
import pl.cyfrowypolsat.cpplayercore.events.playerview.PlayerViewListener
import pl.cyfrowypolsat.cpplayercore.mobile.exo.ExoPlayerControlView
import pl.cyfrowypolsat.cpplayercore.mobile.exo.ExoPlayerControlViewListener
import pl.cyfrowypolsat.cpplayercore.mobile.exo.ExoPlayerView
import pl.cyfrowypolsat.cpplayercore.mobile.lock.LockTouchView
import pl.cyfrowypolsat.cpplayercore.mobile.lock.UnlockFragment
import pl.cyfrowypolsat.cpplayercore.mobile.nextepisode.NextEpisodeFragment
import pl.cyfrowypolsat.cpplayercore.mobile.pip.PictureInPictureController
import pl.cyfrowypolsat.cpplayercore.mobile.seealso.SeeAlsoFragment
import pl.cyfrowypolsat.cpplayercore.mobile.seealso.adapter.SeeAlsoItemClickListener
import pl.cyfrowypolsat.cpplayercore.mobile.settings.SettingsFragment
import pl.cyfrowypolsat.cpplayercore.mobile.skipintro.SkipIntroFragment
import pl.cyfrowypolsat.cpplayercore.mobile.trackselection.TrackSelectionFragment
import pl.cyfrowypolsat.cpplayercore.utils.hasMultiWindowSupport
import java.util.*

class CPPlayerView
@JvmOverloads constructor(context: Context,
                          attrs: AttributeSet? = null,
                          defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr),
        LifecycleObserver,
        PlayerViewListener,
        ExoPlayerControlViewListener,
        TvxViewInterface,
        TeravoltController.Listener {

    companion object {
        private const val NOT_RESIZED_SUBTITLES_TEXT_SIZE = 18f
        private const val SUBTITLES_AND_AUDIO_FRAGMENT_TAG = "SUBTITLES_AND_AUDIO_FRAGMENT_TAG"
        private const val SETTINGS_FRAGMENT_TAG = "SETTINGS_FRAGMENT_TAG"
        private const val SKIP_INTRO_FRAGMENT_TAG = "SKIP_INTRO_FRAGMENT_TAG"
        private const val NEXT_EPISODE_FRAGMENT_TAG = "NEXT_EPISODE_FRAGMENT_TAG"
        private const val SEE_ALSO_FRAGMENT_TAG = "SEE_ALSO_FRAGMENT_TAG"
        private const val UNLOCK_FRAGMENT_TAG = "LOCK_FRAGMENT_TAG"
    }

    private var playerControlView: ExoPlayerControlView? = null
    private var playerView: ExoPlayerView? = null
    private var trackSelectionManager: TrackSelectionManager? = null
    private var subtitleSizeSettingsManager = SubtitleSizeSettingsManager(context, NOT_RESIZED_SUBTITLES_TEXT_SIZE)
    private var subtitleContrastSettingsManager = SubtitleContrastSettingsManager(context)
    private var playerLockTouchView: LockTouchView? = null
    private var isPlayerUILocked = false

    private var teravoltPlayerOverlayLayout: FrameLayout? = null
    private var teravoltController: TeravoltController? = null
    private var showTeravoltOverlay: Boolean = false

    init {
        LayoutInflater.from(context).inflate(R.layout.cppl_cr_mobile_cp_player_layout, this, true)
        playerControlView = findViewById(androidx.media3.ui.R.id.exo_controller)
        playerView = findViewById(R.id.player_view)
        playerLockTouchView = findViewById(R.id.player_lock_touch_view)
        playerLockTouchView?.onSingleTap = { toggleUnlockFragmentVisibility() }
        teravoltPlayerOverlayLayout = findViewById(R.id.player_teravolt_overlay_layout)
    }

    private lateinit var playerController: PlayerController
    private lateinit var playerConfig: PlayerConfig
    private lateinit var activity: ComponentActivity
    private lateinit var lifecycle: Lifecycle
    private lateinit var fragmentManager: FragmentManager
    private lateinit var playerListener: PlayerListener
    private var pictureInPictureController: PictureInPictureController? = null

    private fun getSubtitleView(): SubtitleView? = findViewById(androidx.media3.ui.R.id.exo_subtitles)

    fun setup(playerConfig: PlayerConfig,
              activity: ComponentActivity,
              fragmentManager: FragmentManager,
              playerListener: PlayerListener,
              lifecycle: Lifecycle,
              downloadRequest: DownloadRequest? = null,
              downloadCache: SimpleCache? = null) {
        this.playerConfig = playerConfig
        this.activity = activity
        this.lifecycle = lifecycle
        this.fragmentManager = fragmentManager
        this.playerListener = playerListener
        playerController = if (downloadRequest != null && downloadCache != null) {
            OfflinePlayerController(downloadCache, downloadRequest, playerConfig, playerView!!, activity, this)
        } else {
            PlayerController(playerConfig, playerView!!, activity, this)
        }
        playerControlView?.setup(isLive = playerConfig.isLive(),
                mediaTitle = playerConfig.title,
                ageGroup = playerConfig.ageGroup,
                mediaBadges = playerConfig.mediaBadges,
                startOverProvider = playerConfig.startOverProvider,
                controlViewListener = this,
                isTeravoltOverlayEnabled = playerConfig.isOverlayEnabled(OverlayType.TERAVOLT))
        lifecycle.removeObserver(this)
        lifecycle.addObserver(this)

        val textSize = subtitleSizeSettingsManager.getSelectedSubtitleTextSize()
        val textStyle = subtitleContrastSettingsManager.getProperCaptionStyle()
        Handler().post {
            getSubtitleView()?.setFixedTextSize(TypedValue.COMPLEX_UNIT_SP, textSize)
            getSubtitleView()?.setStyle(textStyle)
        }
        lockPlayerUI(playerConfig.lockPlayerUI)
    }

    fun setControllerVisibilityListener(onVisibilityChange: (visibility: Int) -> Unit) {
        playerView?.additionalVisibilityListener = PlayerControlView.VisibilityListener { onVisibilityChange(it) }
        playerControlView?.let { onVisibilityChange(it.visibility) }
    }

    fun getBottomControlsContainer(): ViewGroup? {
        return playerControlView?.playerBottomControlsContainer
    }

    fun getExternalLayoutContainer(): ViewGroup? {
        return playerControlView?.playerExternalLayoutContainer
    }

    fun hideAllControls() {
        playerControlView?.hideAllControls()
    }

    fun showAllControls() {
        playerControlView?.showAllControls()
    }

    fun blockControlsHideOnTouch() {
        playerControlView?.showTimeoutMs = 0
        playerView?.controllerHideOnTouch = false
    }

    fun unblockControlsHideOnTouch() {
        playerView?.controllerShowTimeoutMs = PlayerControlView.DEFAULT_SHOW_TIMEOUT_MS
        playerView?.controllerHideOnTouch = true
    }

    private fun toggleUnlockFragmentVisibility() {
        if (fragmentManager.isFragmentAttached(UNLOCK_FRAGMENT_TAG)) {
            finishFragment(UNLOCK_FRAGMENT_TAG)
        } else {
            showUnlockFragment()
        }
    }

    private fun lockPlayerUI(lock: Boolean) {
        isPlayerUILocked = lock
        playerLockTouchView?.isTouchForwardingLocked = lock
        playerControlView?.areControlsLocked = lock
    }

    private fun initialize() {
        playerController.initializePlayer()
        playerView?.player = playerController.player
        playerListener.playerInitialized(playerConfig)

        val player = playerController.player ?: return

        if (playerConfig.isOverlayEnabled(OverlayType.TERAVOLT)) {
            teravoltController = TeravoltController(playerConfig, context, player, tvxEngine, this)
            if (playerConfig.isOverlayAutostart(OverlayType.TERAVOLT)) {
                showTeravoltOverlay = true
            }
        }
    }

    private fun release() {
        teravoltController?.release()
        playerController.releasePlayer()
        clearOverlays()
    }

    fun releaseAll() {
        release()
        playerController.releaseAdsLoader()
        pictureInPictureController?.release()
        lifecycle.removeObserver(this)
    }

    private fun finishPlayerActivity() {
        Handler().post { activity.finishAndRemoveTask() }
    }

    fun play() {
        val state = playerController.player?.playbackState
        if (state == Player.STATE_ENDED) {
            playerController.player?.seekTo(0)
        }
        playerController.player?.play()
    }

    fun pause() {
        playerController.player?.pause()
    }

    fun seekToDate(date: Date) {
        playerController.seekToDate(date)
    }

    fun notifyOnPositionUpdate() {
        playerController.notifyOnPositionUpdate()
    }


    // PlayerView listener
    override fun onPlayerError(exception: PlayerException) {
        releaseAll()
        playerListener.onPlayerError(exception)
    }

    override fun onBehindLiveWindowError() {
        playerController.clearStartPosition()
        initialize()
    }

    override fun onTracksChanged() {
        trackSelectionManager = TrackSelectionManager.build(playerController.trackSelector, activity)
        trackSelectionManager?.let {
            playerControlView?.subtitlesAndAudioButton?.adjustVisibleOrGone {
                it.isAudioSelectionAvailable() || it.isSubtitleSelectionAvailable()
            }
            playerControlView?.settingsButton?.adjustVisibleOrGone {
                it.isSubtitleSelectionAvailable()
            }
        }
    }

    override fun onSeeAlsoStarted(seeAlsoItems: List<SeeAlsoItem>) {
        showSeeAlsoFragment(seeAlsoItems) {
            maybeShowControlsInReplayState()
        }
    }

    override fun onSeeAlsoEnded() {
        finishFragment(SEE_ALSO_FRAGMENT_TAG)
        playerControlView?.setPlaybackButtonsState()
        unblockControlsHideOnTouch()
    }

    override fun onIntroStarted(introEndPosition: Int) {
        showSkipIntroFragment(introEndPosition) {
            playerControlView?.hide()
            unblockControlsHideOnTouch()
        }
    }

    override fun onIntroEnded() {
        finishFragment(SKIP_INTRO_FRAGMENT_TAG)
    }

    override fun onCreditsStarted() {
        showNextEpisodeFragment {
            playerControlView?.hide()
            unblockControlsHideOnTouch()
        }
    }

    override fun onVodEdgeChanged(isAtVodEdge: Boolean) {
        playerControlView?.setIsAtVodEdge(isAtVodEdge)
    }

    override fun onLiveEdgeChanged(isAtLiveEdge: Boolean) {
        playerControlView?.setIsAtLiveEdge(isAtLiveEdge)
    }

    override fun onPlayCompleted() {
        finishPlayerActivity()
    }

    override fun onPositionUpdate(positionMs: Long, duration: Long) {
        playerListener.onPositionUpdate(positionMs, duration)
    }

    override fun onVodEndedAutoplayNextEpisode() {
        playerListener.onNextEpisodeAutoPlay(isPlayerUILocked)
    }

    override fun onAdBlockStarted() {
        clearOverlays()
        playerLockTouchView?.isTouchForwardingLocked = false
    }

    override fun onAdBlockEnded() {
        playerLockTouchView?.isTouchForwardingLocked = isPlayerUILocked
    }

    // PlayerControlView listener
    override fun onBackButtonClicked() {
        finishPlayerActivity()
    }

    override fun onSubtitlesAndAudioButtonClicked() {
        val trackTypes = trackSelectionManager?.getSubtitlesAndAudioTrackTypes() ?: listOf()
        showTrackSelectionFragment(trackTypes) {
            playerControlView?.hide()
            unblockControlsHideOnTouch()
        }
    }

    override fun onSettingsButtonClicked() {
        showSettingsFragment {
            playerControlView?.hide()
            unblockControlsHideOnTouch()
        }
    }

    override fun onPadlockButtonClicked() {
        showUnlockFragment {
            playerControlView?.hide()
            lockPlayerUI(true)
        }
    }

    // Teravolt
    override fun onTeravoltButtonClicked() {
        showTeravoltOverlay()
    }

    private fun showTeravoltOverlay() {
       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && activity.isInPictureInPictureMode) {
            return
        }

        val flutterSurfaceView = FlutterSurfaceView(activity, true)
        val teravoltPlayerOverlayView = FlutterView(activity, flutterSurfaceView).apply {
            layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
            )
        }
        teravoltPlayerOverlayLayout?.removeAllViews()
        teravoltPlayerOverlayLayout?.addView(teravoltPlayerOverlayView)

        val result = teravoltController?.prepare(teravoltPlayerOverlayView, activity, lifecycle) ?: false
        if(result) {
            playerController.setOverlayVisibility(OverlayType.TERAVOLT, true)
            playerControlView?.areControlsLocked = true
            playerControlView?.hide()
            showTeravoltOverlay = true
        } else {
            playerControlView?.areControlsLocked = false
        }
    }

    private fun hideTeravoltOverlay() {
        playerController.setOverlayVisibility(OverlayType.TERAVOLT, false)
        playerControlView?.areControlsLocked = false
        teravoltPlayerOverlayLayout?.removeAllViews()
        teravoltController?.release()
        playerControlView?.show()
        showTeravoltOverlay = false
    }


    override fun onTeravoltNoAvailableMatches() {
        hideTeravoltOverlay()
    }

    override fun onTeravoltOverlayError(exception: TeravoltException) {
        playerController.onOverlayError(exception)
        hideTeravoltOverlay()
        playerListener.onPlayerError(PlayerException(exception, exception.code))
    }

    override fun onTeravoltOverlayClose() {
        hideTeravoltOverlay()
    }

    override fun onTeravoltMatchChanged() {
        teravoltPlayerOverlayLayout?.removeAllViews()
    }

    override fun onTeravoltMatchSelected(teravoltMatchItem: TeravoltMatchItem) {
        playerListener.onTeravoltItemClicked(teravoltMatchItem)
    }

    // Track selection
    private fun showTrackSelectionFragment(trackTypes: List<Int>, showSuccess: () -> Unit = {}) {
        val trackSelectionManager = trackSelectionManager ?: return
        if (playerController.isReleased()) return
        val fragment = TrackSelectionFragment()
        fragment.init(trackSelectionManager = trackSelectionManager,
                trackTypes = trackTypes)
        fragment.enterTransition = Fade()
        fragment.exitTransition = Fade()
        fragmentManager.commitNow { replace(R.id.player_overlay, fragment, SUBTITLES_AND_AUDIO_FRAGMENT_TAG) }
        showSuccess()
    }

    // Settings
    private fun showSettingsFragment(showSuccess: () -> Unit = {}) {
        if (playerController.isReleased()) return
        val fragment = SettingsFragment()
        fragment.init(subtitleSizeSettingsManager, subtitleContrastSettingsManager, {
            getSubtitleView()?.setFixedTextSize(TypedValue.COMPLEX_UNIT_SP, it.textSize)
        }, {
            getSubtitleView()?.setStyle(subtitleContrastSettingsManager.getProperCaptionStyle())
        })
        fragment.enterTransition = Fade()
        fragment.exitTransition = Fade()
        fragmentManager.commitNow { replace(R.id.player_overlay, fragment, SETTINGS_FRAGMENT_TAG) }
        showSuccess()
    }


    // Skip intro
    private fun showSkipIntroFragment(introEndPosition: Int, showSuccess: () -> Unit = {}) {
        if (activity.isInPipOrMultiWindowMode()) return
        if (fragmentManager.isFragmentAttached(SKIP_INTRO_FRAGMENT_TAG)) return
        if (playerController.isReleased()) return
        val fragment = SkipIntroFragment()
        fragment.init(playerControlView, introEndPosition)
        fragment.enterTransition = Fade()
        fragment.exitTransition = Fade()
        fragmentManager.commitNow { replace(R.id.player_overlay, fragment, SKIP_INTRO_FRAGMENT_TAG) }
        showSuccess()
    }

    // Next episode
    private fun showNextEpisodeFragment(showSuccess: () -> Unit = {}) {
        if (activity.isInPipOrMultiWindowMode()) return
        if (fragmentManager.isFragmentAttached(NEXT_EPISODE_FRAGMENT_TAG)) return
        if (playerController.isReleased()) return
        val fragment = NextEpisodeFragment()
        fragment.init(playerControlView, playerConfig.autoplayNextEpisodeTimerEnabled, { playerListener.onNextEpisodeButtonClicked(isPlayerUILocked) }, { playerListener.onNextEpisodeAutoPlay(isPlayerUILocked) })
        fragment.enterTransition = Fade()
        fragment.exitTransition = Fade()
        fragmentManager.commitNow { replace(R.id.player_overlay, fragment, NEXT_EPISODE_FRAGMENT_TAG) }
        showSuccess()
    }

    // See also
    private fun showSeeAlsoFragment(seeAlsoItems: List<SeeAlsoItem>, showSuccess: () -> Unit = {}) {
        if (activity.isInPipOrMultiWindowMode()) return
        if (fragmentManager.isFragmentAttached(SEE_ALSO_FRAGMENT_TAG)) return
        if (playerController.isReleased()) return
        val fragment = SeeAlsoFragment()
        fragment.init(seeAlsoItems = seeAlsoItems,
                listener = object : SeeAlsoItemClickListener {
                    override fun onSeeAlsoItemClick(seeAlsoItem: SeeAlsoItem) {
                        playerListener.onSeeAlsoItemClicked(seeAlsoItem, isPlayerUILocked)
                    }
                    override fun onSeeAlsoItemAutoPlay(seeAlsoItem: SeeAlsoItem) {
                        playerListener.onSeeAlsoItemAutoPlay(seeAlsoItem, isPlayerUILocked)
                    }
                })
        fragment.enterTransition = Fade()
        fragment.exitTransition = Fade()
        fragmentManager.commitNow { replace(R.id.player_overlay, fragment, SEE_ALSO_FRAGMENT_TAG) }
        showSuccess()
    }

    // Unlock
    private fun showUnlockFragment(showSuccess: () -> Unit = {}) {
        if (activity.isInPipOrMultiWindowMode()) return
        if (fragmentManager.isFragmentAttached(UNLOCK_FRAGMENT_TAG)) return
        if (playerController.isReleased()) return
        val fragment = UnlockFragment()
        fragment.onUnlock = {
            lockPlayerUI(false)
            maybeShowControlsInReplayState()
        }
        fragment.showTimeoutMs = playerView?.controllerShowTimeoutMs
        fragment.enterTransition = Fade()
        fragment.exitTransition = Fade()
        fragmentManager.commitNow { replace(R.id.player_unlock_overlay, fragment, UNLOCK_FRAGMENT_TAG) }
        showSuccess()
    }

    private fun maybeShowControlsInReplayState() {
        if (fragmentManager.isFragmentAttached(SEE_ALSO_FRAGMENT_TAG).not()) return
        if (isPlayerUILocked) return

        playerControlView?.setReplayButtonsState()
        blockControlsHideOnTouch()
        playerControlView?.show()
    }

    private fun finishFragment(tag: String) {
        val fragment = fragmentManager.findFragmentByTag(tag)
        fragment?.let { fragmentManager.commitNow(true) { remove(it) } }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            pictureInPictureController = object : PictureInPictureController(activity,
                    playerConfig,
                    { generatePIPParams(activity, playerController, playerControlView) }) {
                override fun pipActionRew() {
                    playerControlView?.rewind()
                }

                override fun pipActionPause() {
                    pause()
                }

                override fun pipActionPlay() {
                    play()
                }

                override fun pipActionFfwd() {
                    playerControlView?.fastForward()
                }
            }
        }

        pictureInPictureController?.registerReceiver()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        if (hasMultiWindowSupport()) {
            initialize()
            playerView?.onResume()

            pictureInPictureController?.setMediaSession(playerController)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        if (!hasMultiWindowSupport()) {
            initialize()
            playerView?.onResume()
        }

        // restore controls after returning from PiP mode
        playerView?.useController = true
        pictureInPictureController?.let { playerController.player?.removeListener(it) }
        if(showTeravoltOverlay) {
            showTeravoltOverlay()
        }
        teravoltController?.resume()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        if (!hasMultiWindowSupport()) {
            playerView?.onPause()
            release()
        }
        teravoltController?.pause()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        if (hasMultiWindowSupport()) {
            playerView?.onPause()
            release()
        }
        teravoltController?.stop()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        teravoltController?.release()
        pictureInPictureController?.release()
        playerController.releaseAdsLoader()
        playerView?.overlayFrameLayout?.removeAllViews()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun enterPIPMode() {
        playerController.player?.let {
            if (it.isPlaying || it.isPlayingAd) {
                if (showTeravoltOverlay) {
                    hideTeravoltOverlay()
                    showTeravoltOverlay = true
                }
                playerView?.useController = false
                playerControlView?.player = playerController.player
                pictureInPictureController?.let { pipController -> it.addListener(pipController) }
                pictureInPictureController?.enterPictureInPictureMode()
            }
        }
    }

    fun setChromecastButtonVisibility(isVisible: Boolean) {
        playerControlView?.setChromecastButtonVisibility(isVisible)
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        if (activity.isInPipOrMultiWindowMode()) {
            clearOverlays()
        } else {
            playerController.maybeNotifyOnSeeAlsoStarted()
        }
    }

    private fun clearOverlays() {
        finishFragment(SUBTITLES_AND_AUDIO_FRAGMENT_TAG)
        finishFragment(SKIP_INTRO_FRAGMENT_TAG)
        finishFragment(NEXT_EPISODE_FRAGMENT_TAG)
        finishFragment(SEE_ALSO_FRAGMENT_TAG)
        finishFragment(UNLOCK_FRAGMENT_TAG)
    }

    fun updateMediaTitle(mediaTitle: String) {
        playerControlView?.updateMediaTitle(mediaTitle)
    }

}
