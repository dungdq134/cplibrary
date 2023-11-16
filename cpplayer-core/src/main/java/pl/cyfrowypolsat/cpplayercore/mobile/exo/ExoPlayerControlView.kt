package pl.cyfrowypolsat.cpplayercore.mobile.exo

import android.content.Context
import android.content.res.Configuration
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.media3.common.C
import androidx.media3.ui.DefaultTimeBar
import androidx.media3.ui.PlayerControlView
import androidx.media3.ui.TimeBar
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import pl.cyfrowypolsat.cpchromecast.presentation.mediaroutebutton.ChromecastMediaRouteButton
import pl.cyfrowypolsat.cpcommon.core.extensions.dp
import pl.cyfrowypolsat.cpcommon.domain.model.enums.MediaBadgeType
import pl.cyfrowypolsat.cpcommon.presentation.extensions.*
import pl.cyfrowypolsat.cpcommon.presentation.ratingsystem.MediaBadgesView
import pl.cyfrowypolsat.cpcommon.presentation.utils.getAgeGroupDrawable
import pl.cyfrowypolsat.cpcommon.presentation.utils.setAgeGroupContentDescription
import pl.cyfrowypolsat.cpplayercore.R
import pl.cyfrowypolsat.cpplayercore.core.extensions.getDisplayedDuration
import pl.cyfrowypolsat.cpplayercore.core.extensions.getLivePlayerTime
import pl.cyfrowypolsat.cpplayercore.core.extensions.getPlayerTime
import pl.cyfrowypolsat.cpplayercore.core.extensions.seekToDate
import pl.cyfrowypolsat.cpplayercore.core.startover.StartOverProvider
import timber.log.Timber
import java.util.*
import kotlin.math.max
import kotlin.math.min


interface ExoPlayerControlViewListener {
    fun onBackButtonClicked()
    fun onSubtitlesAndAudioButtonClicked()
    fun onSettingsButtonClicked()
    fun onPadlockButtonClicked()
    fun onTeravoltButtonClicked()
}

class ExoPlayerControlView
@JvmOverloads constructor(context: Context,
                          attrs: AttributeSet? = null,
                          defStyleAttr: Int = 0) : PlayerControlView(context, attrs, defStyleAttr),
        PlayerControlView.ProgressUpdateListener,
        TimeBar.OnScrubListener {

    companion object {
        private const val REWIND_INCREMENTS_MS = 10000L
        private const val FAST_FORWARD_INCREMENT_MS = 10000L
    }

    var playerBottomControlsContainer: ViewGroup? = null
        private set
    var playerControlsAndTitleContainer: LinearLayout? = null
        private set
    var playerTopControlsContainer: ViewGroup? = null
        private set
    var playerExternalLayoutContainer: ViewGroup? = null
        private set

    private var currentTimeView: TextView? = null
    private var liveCurrentTimeView: TextView? = null
    private var durationTimeView: TextView? = null
    private var liveEdgeIndicatorView: TextView? = null
    private var timeBar: DefaultTimeBar? = null
    private var fastForwardButton: ImageButton? = null
    private var rewindButton: ImageButton? = null
    private var startOverButton: ImageButton? = null
    private var playPauseButtonContainer: FrameLayout? = null
    private var replayButton: ImageButton? = null
    private var titleTextView: TextView? = null
    private var ageRestrictionImageView: ImageView? = null
    private var titleContainer: LinearLayout? = null
    private var mediaBadgesView: MediaBadgesView? = null
    private var playerChromecastButton: ChromecastMediaRouteButton? = null
    private var teravoltButton: ImageButton? = null
    var padlockButton: ImageButton? = null
    var subtitlesAndAudioButton: ImageButton? = null
    var settingsButton: ImageButton? = null

    private var startOverSubscription: Disposable? = null

    init {
        playerBottomControlsContainer = findViewById(R.id.player_bottom_controls_container)
        playerControlsAndTitleContainer = findViewById(R.id.player_controls_and_title_container)
        playerTopControlsContainer = findViewById(R.id.player_top_controls_container)
        playerExternalLayoutContainer = findViewById(R.id.player_external_layout_container)
        timeBar = findViewById(R.id.exo_progress)
        currentTimeView = findViewById(R.id.player_current_time)
        liveCurrentTimeView = findViewById(R.id.player_live_current_time)
        durationTimeView = findViewById(R.id.player_duration_time)
        liveEdgeIndicatorView = findViewById(R.id.player_live_edge_indicator)
        fastForwardButton = findViewById(R.id.player_fast_forward_button)
        rewindButton = findViewById(R.id.player_rewind_button)
        startOverButton = findViewById(R.id.player_start_over)
        playPauseButtonContainer = findViewById(R.id.player_play_pause_container)
        replayButton = findViewById(R.id.player_replay)
        titleTextView = findViewById(R.id.player_media_title)
        ageRestrictionImageView = findViewById(R.id.player_age_restriction)
        mediaBadgesView = findViewById(R.id.player_media_badges)
        titleContainer = findViewById(R.id.player_media_title_container)
        subtitlesAndAudioButton = findViewById(R.id.player_subtitles_and_audio_button)
        settingsButton = findViewById(R.id.player_settings_button)
        padlockButton = findViewById(R.id.player_padlock_button)
        playerChromecastButton = findViewById(R.id.player_chromecast_button)
        teravoltButton = findViewById(R.id.player_teravolt_button)
        setProgressUpdateListener(this)
        timeBar?.addListener(this)
        setAnimationEnabled(false)
    }

    private val timeBuilder = StringBuilder()
    private var isLive = false
    private var isAtLiveEdge = true
    private var isAtVodEdge = false
    private var isScrubbing = false
    var areControlsLocked: Boolean = true

    fun setup(isLive: Boolean,
              mediaTitle: String,
              ageGroup: Int?,
              mediaBadges: List<MediaBadgeType>?,
              startOverProvider: StartOverProvider?,
              controlViewListener: ExoPlayerControlViewListener,
              isTeravoltOverlayEnabled: Boolean) {
        this.isLive = isLive
        setTimeLayout()
        setListeners(controlViewListener)
        titleTextView?.text = mediaTitle
        setAgeRestriction(ageGroup)
        setMediaBadges(mediaBadges)
        setTeravoltButtonVisibility(isTeravoltOverlayEnabled)
        setupStartOverButton(startOverProvider)
        setPortraitOrLandscapeStyles(resources.configuration)
    }

    fun setAgeRestriction(ageGroup: Int?) {
        ageRestrictionImageView?.let {
            it.setImageDrawable(getAgeGroupDrawable(it.context, ageGroup))
            setAgeGroupContentDescription(it, ageGroup)
        }
    }

    private fun setMediaBadges(mediaBadges: List<MediaBadgeType>?) {
        mediaBadgesView?.let {
            it.setMediaBadges(mediaBadges)
        }
    }

    fun setIsAtLiveEdge(isAtLiveEdge: Boolean) {
        this.isAtLiveEdge = isAtLiveEdge
        player?.let { setCurrentPositionLabel(it.currentPosition) }
        fastForwardButton?.isEnabled = isFastForwardEnabled()
    }

    fun setIsAtVodEdge(isAtVodEdge: Boolean) {
        this.isAtVodEdge = isAtVodEdge
        fastForwardButton?.isEnabled = isFastForwardEnabled()
    }

    fun updateTitleEndMargin(marginEnd: Int) = titleContainer?.setMarginEnd(marginEnd)
    fun fastForward() = dispatchFastForward()
    fun rewind() = dispatchRewind()
    fun isRewindEnabled(): Boolean = true

    fun isFastForwardEnabled(): Boolean {
        return if (isLive) {
            isAtLiveEdge.not()
        } else {
            isAtVodEdge.not()
        }
    }

    fun setChromecastButtonVisibility(isVisible: Boolean) {
        if (isVisible) {
            playerChromecastButton?.visibility = VISIBLE
        } else {
            playerChromecastButton?.visibility = GONE
        }
    }

    fun hideAllControls() {
        playerBottomControlsContainer?.fadeOutAndGone(500L)
        playerTopControlsContainer?.fadeOutAndGone(500L)
    }

    fun showAllControls() {
        playerBottomControlsContainer?.visibleAndFadeIn(500L)
        playerTopControlsContainer?.visibleAndFadeIn(500L)
    }

    fun setReplayButtonsState() {
        replayButton?.visible()
        playPauseButtonContainer?.gone()
    }

    fun setPlaybackButtonsState() {
        replayButton?.gone()
        playPauseButtonContainer?.visible()
    }

    fun setTeravoltButtonVisibility(isVisible: Boolean) {
        if (isVisible) {
            teravoltButton?.visibility = VISIBLE
        } else {
            teravoltButton?.visibility = GONE
        }
    }

    private fun setCurrentPositionLabel(currentPosition: Long) {
        val duration = player?.getDisplayedDuration() ?: return
        if (isLive) {
            if (isScrubbing) {
                val liveTime = duration - currentPosition
                liveCurrentTimeView?.text = timeBuilder.getLivePlayerTime(liveTime)
                liveCurrentTimeView?.visible()
                liveEdgeIndicatorView?.invisible()
            } else {
                liveCurrentTimeView?.invisible()
                liveEdgeIndicatorView?.visible()
                liveEdgeIndicatorView?.isEnabled = !isAtLiveEdge
            }
        } else {
            currentTimeView?.text = timeBuilder.getPlayerTime(currentPosition)
        }
    }

    private fun setDurationLabel(duration: Long) {
        durationTimeView?.text = timeBuilder.getPlayerTime(duration)
    }

    private fun setTimeLayout() {
        if (isLive) {
            findViewById<View>(R.id.player_time_layout).gone()
            findViewById<View>(R.id.player_live_time_layout).visible()
        } else {
            findViewById<View>(R.id.player_time_layout).visible()
            findViewById<View>(R.id.player_live_time_layout).gone()
        }
    }

    private fun setListeners(listener: ExoPlayerControlViewListener) {
        findViewById<ImageButton>(R.id.player_close_button).setOnClickListener { listener.onBackButtonClicked() }
        subtitlesAndAudioButton?.setOnClickListener { listener.onSubtitlesAndAudioButtonClicked() }
        settingsButton?.setOnClickListener { listener.onSettingsButtonClicked() }
        padlockButton?.setOnClickListener { listener.onPadlockButtonClicked() }
        teravoltButton?.setOnClickListener { listener.onTeravoltButtonClicked() }
        liveEdgeIndicatorView?.setOnClickListener { dispatchWatchLive() }
        replayButton?.setOnClickListener {
            player?.seekTo(0)
            player?.playWhenReady = true
        }
        fastForwardButton?.setOnClickListener { dispatchFastForward() }
        rewindButton?.setOnClickListener { dispatchRewind() }
    }

    private fun setupStartOverButton(startOverProvider: StartOverProvider?) {
        if (isLive) {
            startOverButton?.setOnClickListener {
                startOverProvider?.let { provider ->
                    startOverSubscription = provider.provideStartOverDate()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(this::dispatchStartOver, Timber::d)
                } ?: dispatchStartOver()
            }
            startOverButton?.visible()
        } else {
            startOverButton?.gone()
        }
    }

    override fun show() {
        if (areControlsLocked) return
        super.show()
    }

    override fun onProgressUpdate(position: Long, bufferedPosition: Long) {
        player?.let {
            val duration = it.getDisplayedDuration()
            setDurationLabel(duration)
            timeBar?.setDuration(duration)
        }
        if (!isScrubbing) {
            setCurrentPositionLabel(position)
        }
    }

    override fun onScrubMove(timeBar: TimeBar, position: Long) {
        setCurrentPositionLabel(position)
    }

    override fun onScrubStart(timeBar: TimeBar, position: Long) {
        isScrubbing = true
        setCurrentPositionLabel(position)
    }

    override fun onScrubStop(timeBar: TimeBar, position: Long, canceled: Boolean) {
        isScrubbing = false
    }


    private fun dispatchStartOver(startOverDate: Date? = null) {
        player?.seekToDate(startOverDate)
    }

    private fun dispatchWatchLive() {
        player?.seekTo(C.TIME_UNSET)
        player?.play()
    }

    private fun dispatchFastForward() {
        if (isFastForwardEnabled() && player?.isCurrentMediaItemSeekable == true) {
            seekToOffset(FAST_FORWARD_INCREMENT_MS)
        }
    }

    private fun dispatchRewind() {
        seekToOffset(-REWIND_INCREMENTS_MS)
    }

    private fun seekToOffset(offsetMs: Long) {
        val player = player ?: return
        var positionMs = player.currentPosition + offsetMs
        val durationMs = player.getDisplayedDuration()
        positionMs = min(positionMs, durationMs)
        positionMs = max(positionMs, 0)
        player.seekTo(player.currentMediaItemIndex, positionMs)
    }

    fun updateMediaTitle(mediaTitle: String) {
        titleTextView?.text = mediaTitle
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        startOverSubscription?.dispose()
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        setPortraitOrLandscapeStyles(newConfig)
    }

    private fun setPortraitOrLandscapeStyles(config: Configuration?) {
        config ?: return
        if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
            playerControlsAndTitleContainer?.orientation = LinearLayout.VERTICAL
            playerControlsAndTitleContainer?.gravity = Gravity.CENTER
            titleContainer?.setMarginTop(10.dp)
        } else {
            playerControlsAndTitleContainer?.orientation = LinearLayout.HORIZONTAL
            playerControlsAndTitleContainer?.gravity = Gravity.LEFT
            titleContainer?.setMarginTop(0)
        }
    }

}
