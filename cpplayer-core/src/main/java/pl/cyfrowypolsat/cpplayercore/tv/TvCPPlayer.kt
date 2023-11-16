package pl.cyfrowypolsat.cpplayercore.tv

import android.app.Activity
import android.os.Handler
import android.util.TypedValue
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.annotation.IdRes
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commitNow
import androidx.leanback.widget.Action
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.transition.Fade
import androidx.transition.Slide
import androidx.media3.common.C
import androidx.media3.common.text.Cue
import androidx.media3.common.text.CueGroup
import androidx.media3.common.AdViewProvider
import androidx.media3.ui.SubtitleView
import com.teravolt.mobile.tvx_video_plugin.TvxViewInterface
import io.flutter.embedding.android.FlutterSurfaceView
import io.flutter.embedding.android.FlutterView
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import pl.cyfrowypolsat.cpcommon.presentation.extensions.gone
import pl.cyfrowypolsat.cpcommon.presentation.extensions.isAfterOnSaveInstanceState
import pl.cyfrowypolsat.cpcommon.presentation.extensions.visible
import pl.cyfrowypolsat.cpplayercore.configuration.OverlayType
import pl.cyfrowypolsat.cpplayercore.configuration.PlayerConfig
import pl.cyfrowypolsat.cpplayercore.core.PlayerController
import pl.cyfrowypolsat.cpplayercore.core.PlayerException
import pl.cyfrowypolsat.cpplayercore.core.seealso.SeeAlsoItem
import pl.cyfrowypolsat.cpplayercore.core.subtitlesettings.contrast.SubtitleContrastSettingsManager
import pl.cyfrowypolsat.cpplayercore.core.subtitlesettings.textsize.SubtitleSizeSettingsManager
import pl.cyfrowypolsat.cpplayercore.core.teravolt.TeravoltController
import pl.cyfrowypolsat.cpplayercore.core.teravolt.TeravoltException
import pl.cyfrowypolsat.cpplayercore.core.teravolt.TeravoltMatchItem
import pl.cyfrowypolsat.cpplayercore.core.trackselection.TrackSelectionManager
import pl.cyfrowypolsat.cpplayercore.events.player.PlayerListener
import pl.cyfrowypolsat.cpplayercore.events.playerview.PlayerViewListener
import pl.cyfrowypolsat.cpplayercore.tv.actions.TvPlayerOverlayAction
import pl.cyfrowypolsat.cpplayercore.tv.actions.TvSettingsAction
import pl.cyfrowypolsat.cpplayercore.tv.actions.TvStartOverAction
import pl.cyfrowypolsat.cpplayercore.tv.actions.TvSubtitlesAndAudioAction
import pl.cyfrowypolsat.cpplayercore.tv.nextepisode.TvNextEpisodeFragment
import pl.cyfrowypolsat.cpplayercore.tv.seealso.TvSeeAlsoFragment
import pl.cyfrowypolsat.cpplayercore.tv.settings.TvSettingsFragment
import pl.cyfrowypolsat.cpplayercore.tv.skipintro.TvSkipIntroFragment
import pl.cyfrowypolsat.cpplayercore.tv.trackselection.TvTrackSelectionFragment
import pl.cyfrowypolsat.cpplayercore.utils.hasMultiWindowSupport
import timber.log.Timber
import java.util.*

class TvCPPlayer(private val playerConfig: PlayerConfig,
                 private val activity: Activity,
                 adViewProvider: AdViewProvider,
                 private val subtitleView: SubtitleView,
                 private val playerListener: PlayerListener,
                 private val fragmentManager: FragmentManager,
                 @IdRes private val overlayContainerId: Int,
                 private val playerControlsPaddingBottom: Int,
                 private val lifecycle: Lifecycle,
                 private val teravoltPlayerOverlayLayout: FrameLayout,
                 private val playbackUILayout: FrameLayout) : LifecycleObserver,
        PlayerViewListener,
        TvPlayerGlueClickedListener,
        TvxViewInterface,
        TeravoltController.Listener {

    companion object {
        private const val NOT_RESIZED_SUBTITLES_TEXT_SIZE = 22f
        private const val SUBTITLES_AND_AUDIO_FRAGMENT_TAG = "SUBTITLES_AND_AUDIO_FRAGMENT_TAG"
        private const val SETTINGS_FRAGMENT_TAG = "SETTINGS_FRAGMENT_TAG"
        private const val SKIP_INTRO_FRAGMENT_TAG = "SKIP_INTRO_FRAGMENT_TAG"
        private const val NEXT_EPISODE_FRAGMENT_TAG = "NEXT_EPISODE_FRAGMENT_TAG"
        private const val SEE_ALSO_FRAGMENT_TAG = "SEE_ALSO_FRAGMENT_TAG"
        private const val UPDATE_DELAY = 200
    }

    private val playerController = PlayerController(playerConfig, adViewProvider, activity, this)
    private var playerAdapter: TvLeanbackPlayerAdapter? = null
    private var trackSelectionManager: TrackSelectionManager? = null
    private val subtitleSizeSettingsManager = SubtitleSizeSettingsManager(activity, NOT_RESIZED_SUBTITLES_TEXT_SIZE)
    private val subtitleContrastSettingsManager = SubtitleContrastSettingsManager(activity)
    var playerGlue: TvPlayerGlue? = null
    private var playAfterResume = false
    private var startOverSubscription: Disposable? = null

    private var teravoltController: TeravoltController? = null
    private var showTeravoltOverlay: Boolean = false

    fun seekToDate(date: Date? = null) {
        playerAdapter?.seekTo(playerController.getPositionForDate(date))
    }

    fun notifyOnPositionUpdate() {
        playerController.notifyOnPositionUpdate()
    }

    fun onHideControls() {
        focusSkipIntroButton()
    }

    fun isPlaybackEnded(): Boolean {
        return playerController.isPlaybackEnded()
    }

    private fun initialize() {
        playerController.initializePlayer()
        playerAdapter = TvLeanbackPlayerAdapter(activity, playerController.player, UPDATE_DELAY)
        playerGlue = TvPlayerGlue(activity = activity,
                mediaTitle = playerConfig.title,
                ageGroup = playerConfig.ageGroup,
                mediaBadges = playerConfig.mediaBadges,
                isLive = playerConfig.isLive(),
                primaryActions = createStartPrimaryActions(),
                secondaryActions = createSecondaryActions(),
                playerGlueClickedListener = this,
                playerControlsPaddingBottom = playerControlsPaddingBottom,
                leanbackPlayerAdapter = playerAdapter!!)
        playerListener.playerInitialized(playerConfig)

        val textSize = subtitleSizeSettingsManager.getSelectedSubtitleTextSize()
        val textStyle = subtitleContrastSettingsManager.getProperCaptionStyle()
        subtitleView.setFixedTextSize(TypedValue.COMPLEX_UNIT_SP, textSize)
        subtitleView.setStyle(textStyle)

        if (playerConfig.isOverlayEnabled(OverlayType.TERAVOLT)) {
            val player = playerController.player ?: return
            teravoltController = TeravoltController(playerConfig, activity, player, tvxEngine, this)
            if (playerConfig.isOverlayAutostart(OverlayType.TERAVOLT)) {
                showTeravoltOverlay = true
            }
        }
    }

    private fun release() {
        playerController.releasePlayer()
        playerGlue = null
        playerAdapter = null
        trackSelectionManager = null
        clearOverlays()
        startOverSubscription?.dispose()
    }

    fun releaseAll() {
        release()
        playerController.releaseAdsLoader()
    }

    private fun createStartPrimaryActions(): List<Action> {
        val primaryActions = mutableListOf<Action>()
        if (playerConfig.isLive()) {
            primaryActions.add(TvStartOverAction(activity))
        }
        return primaryActions
    }

    private fun createSecondaryActions(): List<Action> {
        val secondaryActions = mutableListOf<Action>()
        trackSelectionManager?.let {
            if (it.isAudioSelectionAvailable() || it.isSubtitleSelectionAvailable()) {
                secondaryActions.add(TvSubtitlesAndAudioAction(activity))
            }
            if (it.isSubtitleSelectionAvailable()) {
                secondaryActions.add(TvSettingsAction(activity))
            }
        }

        if(playerConfig.isOverlayEnabled(OverlayType.TERAVOLT) ){
            secondaryActions.add(TvPlayerOverlayAction(activity))
        }
        return secondaryActions
    }

    override fun actionClicked(action: Action) {
        when (action) {
            is TvStartOverAction -> {
                playerConfig.startOverProvider?.let { provider ->
                    startOverSubscription = provider.provideStartOverDate()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(this::seekToDate, Timber::d)
                } ?: seekToDate()
            }
            is TvSubtitlesAndAudioAction -> {
                val trackTypes = trackSelectionManager?.getSubtitlesAndAudioTrackTypes() ?: listOf()
                showTrackSelectionFragment(trackTypes, SUBTITLES_AND_AUDIO_FRAGMENT_TAG)
            }
            is TvSettingsAction -> showSettingsFragment(SETTINGS_FRAGMENT_TAG)
            is TvPlayerOverlayAction -> showTeravoltOverlay()
        }
    }

    override fun watchLiveClicked() {
        playerAdapter?.seekTo(C.TIME_UNSET)
        playerAdapter?.play()
    }

    override fun backClicked() {
        finishPlayerActivity()
    }

    private fun finishPlayerActivity() {
        Handler().post { activity.finish() }
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
        playerGlue?.updateSecondaryActions(createSecondaryActions())
    }

    override fun onSeeAlsoStarted(seeAlsoItems: List<SeeAlsoItem>) {
        showSeeAlsoFragment(seeAlsoItems)
        playerGlue?.changePlayPauseIcons(true)
    }

    override fun onSeeAlsoEnded() {
        finishFragment(SEE_ALSO_FRAGMENT_TAG)
        playerGlue?.changePlayPauseIcons(false)
    }

    override fun onIntroStarted(introEndPosition: Int) {
        showSkipIntroFragment(introEndPosition)
    }

    override fun onIntroEnded() {
        finishFragment(SKIP_INTRO_FRAGMENT_TAG)
    }

    override fun onCreditsStarted() {
        showNextEpisodeFragment()
    }

    override fun onLiveEdgeChanged(isAtLiveEdge: Boolean) {
        playerGlue?.setIsAtLiveEdge(isAtLiveEdge)
    }

    override fun onPlayCompleted() {
        finishPlayerActivity()
    }

    override fun onPlayerCloseRequested() {
        finishPlayerActivity()
    }

    override fun onPositionUpdate(positionMs: Long, duration: Long) {
        playerListener.onPositionUpdate(positionMs, duration)
    }

    override fun onAdBlockStarted() {
        clearOverlays()
        playerListener.onAdBlockStarted()
    }

    override fun onAdBlockEnded() {
        playerListener.onAdBlockEnded()
    }

    override fun onVodEndedAutoplayNextEpisode() {
        playerListener.onNextEpisodeAutoPlay(false)
    }

    override fun onCues(cueGroup: CueGroup) {
        val mutatedCues = cueGroup.cues.map {
            if (it.line == -1f) it.buildUpon().setLine(-2.3f, Cue.LINE_TYPE_NUMBER).build() else it
        }
        subtitleView.setCues(mutatedCues)
    }

    // Teravolt

    private fun showTeravoltOverlay() {
        val flutterSurfaceView = FlutterSurfaceView(activity, true)
        val teravoltPlayerOverlayView = FlutterView(activity, flutterSurfaceView).apply {
            layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
            )
        }
        teravoltPlayerOverlayLayout.removeAllViews()
        teravoltPlayerOverlayLayout.addView(teravoltPlayerOverlayView)

        val result = teravoltController?.prepare(teravoltPlayerOverlayView, activity, lifecycle) ?: false
        if(result) {
            playerController.setOverlayVisibility(OverlayType.TERAVOLT, true)
            playbackUILayout.gone()
            showTeravoltOverlay = true
        }
    }

    private fun hideTeravoltOverlay() {
        playerController.setOverlayVisibility(OverlayType.TERAVOLT, false)
        teravoltPlayerOverlayLayout.removeAllViews()
        teravoltController?.release()
        playbackUILayout.visible()
        showTeravoltOverlay = false
    }


    override fun onTeravoltNoAvailableMatches() {
        hideTeravoltOverlay()
    }

    override fun onTeravoltOverlayError(exception: TeravoltException) {
        playerController.onOverlayError(exception)
        hideTeravoltOverlay()
        playerController.releasePlayer()
        playerListener.onPlayerError(PlayerException(exception, exception.code))
    }

    override fun onTeravoltOverlayClose() {
        hideTeravoltOverlay()
    }

    override fun onTeravoltMatchChanged() {
        teravoltPlayerOverlayLayout.removeAllViews()
    }

    override fun onTeravoltMatchSelected(teravoltMatchItem: TeravoltMatchItem) {
        playerListener.onTeravoltItemClicked(teravoltMatchItem)
    }


    // Track selection
    private fun showTrackSelectionFragment(trackTypes: List<Int>, tag: String) {
        val trackSelectionManager = trackSelectionManager ?: return
        if (activity.isAfterOnSaveInstanceState()) return
        val fragment = TvTrackSelectionFragment()
        fragment.init(trackSelectionManager = trackSelectionManager,
                trackTypes = trackTypes)
        fragment.enterTransition = Slide(Gravity.END)
        fragment.exitTransition = Slide(Gravity.END)
        fragmentManager.commitNow { replace(overlayContainerId, fragment, tag) }
        playerGlue?.host?.hideControlsOverlay(true)
    }

    // Settings
    private fun showSettingsFragment(tag: String) {
        if (activity.isAfterOnSaveInstanceState()) return
        val fragment = TvSettingsFragment()
        fragment.init(subtitleSizeSettingsManager, subtitleContrastSettingsManager, {
            subtitleView.setFixedTextSize(TypedValue.COMPLEX_UNIT_SP, it.textSize)
        }, {
            subtitleView.setStyle(subtitleContrastSettingsManager.getProperCaptionStyle())
        })
        fragment.enterTransition = Slide(Gravity.END)
        fragment.exitTransition = Slide(Gravity.END)
        fragmentManager.commitNow { replace(overlayContainerId, fragment, tag) }
        playerGlue?.host?.hideControlsOverlay(true)
    }

    // Skip intro
    private fun showSkipIntroFragment(introEndPosition: Int) {
        if (fragmentManager.findFragmentByTag(SKIP_INTRO_FRAGMENT_TAG) != null) return
        if (activity.isAfterOnSaveInstanceState()) return
        val playerGlueHost = playerGlue?.host ?: return
        val playerAdapter = playerAdapter ?: return
        val fragment = TvSkipIntroFragment()
        fragment.init(playerGlueHost, playerAdapter, introEndPosition)
        fragment.enterTransition = Fade()
        fragmentManager.commitNow { replace(overlayContainerId, fragment, SKIP_INTRO_FRAGMENT_TAG) }
    }

    private fun focusSkipIntroButton() {
        val skipIntroFragment = fragmentManager.findFragmentByTag(SKIP_INTRO_FRAGMENT_TAG)
                as? TvSkipIntroFragment
        skipIntroFragment?.focusSkipIntroButton()
    }


    // Next episode
    private fun showNextEpisodeFragment() {
        if (fragmentManager.findFragmentByTag(NEXT_EPISODE_FRAGMENT_TAG) != null) return
        if (activity.isAfterOnSaveInstanceState()) return
        val fragment = TvNextEpisodeFragment()
        fragment.init(playerConfig.autoplayNextEpisodeTimerEnabled, { playerListener.onNextEpisodeButtonClicked(false) }, { playerListener.onNextEpisodeAutoPlay(false) })
        fragment.enterTransition = Fade()
        fragmentManager.commitNow { replace(overlayContainerId, fragment, NEXT_EPISODE_FRAGMENT_TAG) }
        playerGlue?.host?.hideControlsOverlay(true)
    }

    fun focusNextEpisodeButton() {
        val nextEpisodeFragment = fragmentManager.findFragmentByTag(NEXT_EPISODE_FRAGMENT_TAG)
                as? TvNextEpisodeFragment
        nextEpisodeFragment?.focusNextEpisodeButton()
    }


    // See also
    private fun showSeeAlsoFragment(seeAlsoItems: List<SeeAlsoItem>) {
        if (fragmentManager.findFragmentByTag(SEE_ALSO_FRAGMENT_TAG) != null) return
        if (activity.isAfterOnSaveInstanceState()) return
        val fragment = TvSeeAlsoFragment()
        fragment.init(seeAlsoItems = seeAlsoItems,
                seeAlsoItemClickListener = { seeAlsoItem -> playerListener.onSeeAlsoItemClicked(seeAlsoItem, false) },
                seeAlsoItemAutoPlayListener = { seeAlsoItem -> playerListener.onSeeAlsoItemAutoPlay(seeAlsoItem, false) },
                onFocusAboveContent = { playerGlue?.focusSecondaryActions() },
                onFocusBelowContent = { playerGlue?.focusPrimaryActions() },
                onCloseListener = { finishPlayerActivity() })
        fragment.enterTransition = Fade()
        fragment.exitTransition = Fade()
        fragmentManager.commitNow { replace(overlayContainerId, fragment, SEE_ALSO_FRAGMENT_TAG) }
    }


    // Lifecycle events
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        if (hasMultiWindowSupport()) {
            initialize()
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        if (!hasMultiWindowSupport()) {
            initialize()
        }
        if (playAfterResume) {
            playerAdapter?.play()
            playAfterResume = false
        }
        if(showTeravoltOverlay) {
            Handler().postDelayed({
                showTeravoltOverlay()
            }, 1000)
        }
        teravoltController?.resume()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        if (playerController.isPlaying() && !playerController.isPlayingAdvert()) {
            playAfterResume = true
            playerAdapter?.pause()
        }
        if (!hasMultiWindowSupport()) {
            release()
        }
        teravoltController?.pause()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        if (hasMultiWindowSupport()) {
            release()
        }
        teravoltController?.stop()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        teravoltController?.release()
        playerController.releaseAdsLoader()
    }


    private fun finishFragment(tag: String) {
        val fragment = fragmentManager.findFragmentByTag(tag)
        fragment?.let { fragmentManager.commitNow { remove(it) } }
    }

    private fun clearOverlays() {
        finishFragment(SUBTITLES_AND_AUDIO_FRAGMENT_TAG)
        finishFragment(SKIP_INTRO_FRAGMENT_TAG)
        finishFragment(NEXT_EPISODE_FRAGMENT_TAG)
        finishFragment(SEE_ALSO_FRAGMENT_TAG)
    }

}