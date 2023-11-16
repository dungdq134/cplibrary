package pl.cyfrowypolsat.cpplayercore.events.playerview

import android.os.Handler
import com.google.ads.interactivemedia.v3.api.AdEvent
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.Tracks
import androidx.media3.common.text.CueGroup
import io.reactivex.rxjava3.disposables.Disposable
import pl.cyfrowypolsat.cpplayercore.configuration.PlayerConfig
import pl.cyfrowypolsat.cpplayercore.core.PlayerController
import pl.cyfrowypolsat.cpplayercore.core.PlayerException
import pl.cyfrowypolsat.cpplayercore.utils.DebounceCallListener
import timber.log.Timber


class PlayerViewListenerMapper(private val playerViewListener: PlayerViewListener,
                               private val playerConfig: PlayerConfig,
                               private val playerController: PlayerController) : Player.Listener {

    companion object {
        private const val LIVE_EDGE_TOLERANCE_MS = 15000
        private const val VOD_EDGE_TOLERANCE_MS = 1000
    }

    private var currentPositionSubscription: Disposable? = null
    private var currentPosition: Long? = null
    private val seeAlsoItems = playerConfig.seeAlsoList.takeIf { it.isNotEmpty() }
    private val onVodEndedAutoplayNextEpisodeDebounce = DebounceCallListener(2000)

    init {
        playerConfig.introTimeline?.let {
            playerController.player?.createMessage { _, _ -> playerViewListener.onIntroStarted(it.stop) }
                    ?.setPosition(it.start.toLong() + 1)
                    ?.setDeleteAfterDelivery(false)
                    ?.setHandler(Handler())
                    ?.send()

            playerController.player?.createMessage { _, _ -> playerViewListener.onIntroEnded() }
                    ?.setPosition(it.stop.toLong())
                    ?.setDeleteAfterDelivery(false)
                    ?.setHandler(Handler())
                    ?.send()
        }

        playerConfig.creditsTimeline?.let {
            playerController.player?.createMessage { _, _ -> playerViewListener.onCreditsStarted() }
                    ?.setPosition(it.start.toLong())
                    ?.setDeleteAfterDelivery(false)
                    ?.setHandler(Handler())
                    ?.send()

            playerController.player?.createMessage { _, _ -> playerViewListener.onCreditsEnded() }
                    ?.setPosition(it.stop.toLong())
                    ?.setDeleteAfterDelivery(false)
                    ?.setHandler(Handler())
                    ?.send()
        }
    }

    override fun onPlayerError(e: PlaybackException) {
        val playerException = PlayerException.create(e)

        if (playerException.code == PlaybackException.ERROR_CODE_BEHIND_LIVE_WINDOW) {
            playerViewListener.onBehindLiveWindowError()
        } else {
            playerViewListener.onPlayerError(playerException)
        }
    }

    override fun onTracksChanged(tracks: Tracks) {
        playerViewListener.onTracksChanged()

        if (playerController.isPlayingAdvert()) {
            playerViewListener.onAdBlockStarted()
            playerController.player?.playWhenReady = true
        } else {
            playerViewListener.onAdBlockEnded()
        }
    }

    override fun onCues(cueGroup: CueGroup) {
        playerViewListener.onCues(cueGroup)
    }

    override fun onPlaybackStateChanged(playbackState: Int) {
        checkLiveEdge()
        if (playbackState == Player.STATE_ENDED) {
            when {
                playerConfig.hasSuccessor -> {
                    onVodEndedAutoplayNextEpisodeDebounce.call {
                        playerViewListener.onVodEndedAutoplayNextEpisode()
                    }
                }
                seeAlsoItems != null -> {
                    playerViewListener.onSeeAlsoStarted(seeAlsoItems)
                }
                else -> {
                    playerViewListener.onPlayCompleted()
                }
            }
            playerViewListener.onVodEdgeChanged(isAtVodEdge())
        }
    }

    override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
        checkLiveEdge()
    }

    override fun onPositionDiscontinuity(oldPosition: Player.PositionInfo,
                                         newPosition: Player.PositionInfo,
                                         reason: Int) {
        val introTimeline = playerConfig.introTimeline
        val creditsTimeline = playerConfig.creditsTimeline

        if(introTimeline != null && newPosition.positionMs >= introTimeline.stop) {
            playerViewListener.onIntroEnded()
        }

        if (isAtVodEdge(newPosition.positionMs)) {
            if (playerConfig.hasSuccessor) {
                onVodEndedAutoplayNextEpisodeDebounce.call {
                    playerViewListener.onVodEndedAutoplayNextEpisode()
                }
            } else if (seeAlsoItems != null) {
                playerViewListener.onSeeAlsoStarted(seeAlsoItems)
                playerViewListener.onCreditsEnded()
            }
        } else if (creditsTimeline != null && newPosition.positionMs >= creditsTimeline.start) {
            playerViewListener.onSeeAlsoEnded()
            playerViewListener.onCreditsStarted()
        } else {
            playerViewListener.onSeeAlsoEnded()
        }

        playerViewListener.onVodEdgeChanged(isAtVodEdge(newPosition.positionMs))
        notifyOnPositionUpdate(newPosition.positionMs)
    }

    private fun isAtVodEdge(currentPosition: Long? = null): Boolean {
        if(playerController.isDynamic()) return false
        val positionMs = currentPosition ?: playerController.getCurrentPosition()
        val vodEdgePosition = playerController.getDuration() - VOD_EDGE_TOLERANCE_MS
        return positionMs >= vodEdgePosition
    }

    private fun notifyOnPositionUpdate(positionMs: Long) {
        this.currentPosition = positionMs
        playerViewListener.onPositionUpdate(positionMs, playerController.getDuration())
    }

    fun release() {
        currentPositionSubscription?.dispose()
    }

    private fun checkLiveEdge() {
        if (playerController.isDynamic()) {
            val playWhenReady = playerController.player?.playWhenReady ?: true
            if (isAtLiveEdge() && playWhenReady) {
                playerViewListener.onLiveEdgeChanged(true)
            } else {
                playerViewListener.onLiveEdgeChanged(false)
            }
        }
    }

    private fun isAtLiveEdge(): Boolean {
        val defaultPosition = playerController.getDefaultPosition() ?: return true
        return playerController.getCurrentPosition() + LIVE_EDGE_TOLERANCE_MS > defaultPosition
    }

    fun onAdEvent(adEvent: AdEvent?) {
        Timber.d("adEvent type %s", adEvent?.type ?: "null")
        when (adEvent?.type) {
            AdEvent.AdEventType.ALL_ADS_COMPLETED,
            AdEvent.AdEventType.CONTENT_RESUME_REQUESTED -> {
                playerViewListener.onAdBlockEnded()
            }
            AdEvent.AdEventType.CONTENT_PAUSE_REQUESTED -> {
                playerViewListener.onAdBlockStarted()
            }
            else -> {
            }
        }

    }

    fun notifyOnPositionUpdate() {
        notifyOnPositionUpdate(playerController.getCurrentPosition())
    }

    fun maybeNotifyOnSeeAlsoStarted() {
        if (seeAlsoItems != null && isAtVodEdge()) {
            playerViewListener.onSeeAlsoStarted(seeAlsoItems)
        }
    }

    fun onPlayerCloseRequested() {
        playerViewListener.onPlayerCloseRequested()
    }
}