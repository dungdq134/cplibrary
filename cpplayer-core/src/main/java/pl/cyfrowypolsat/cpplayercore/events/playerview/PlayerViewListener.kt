package pl.cyfrowypolsat.cpplayercore.events.playerview

import androidx.media3.common.text.CueGroup
import pl.cyfrowypolsat.cpplayercore.core.PlayerException
import pl.cyfrowypolsat.cpplayercore.core.seealso.SeeAlsoItem

interface PlayerViewListener {

    fun onPlayerError(exception: PlayerException) {

    }

    fun onBehindLiveWindowError() {

    }

    fun onTracksChanged() {

    }

    fun onPlayCompleted() {

    }

    fun onPlayerCloseRequested() {
        
    }

    fun onPositionUpdate(positionMs: Long, duration: Long) {

    }


    // See also
    fun onSeeAlsoStarted(seeAlsoItems: List<SeeAlsoItem>) {

    }

    fun onSeeAlsoEnded() {

    }


    // Intro
    fun onIntroStarted(introEndPosition: Int) {

    }

    fun onIntroEnded() {

    }


    // Vod edge
    fun onVodEdgeChanged(isAtVodEdge: Boolean) {

    }


    // Live edge
    fun onLiveEdgeChanged(isAtLiveEdge: Boolean) {

    }


    // Ads
    fun onAdBlockStarted() {

    }

    fun onAdBlockEnded() {

    }


    // Credits
    fun onCreditsStarted() {

    }

    fun onCreditsEnded() {

    }

    // Next episode
    fun onVodEndedAutoplayNextEpisode() {

    }

    // Subtitles
    fun onCues(cueGroup: CueGroup) {

    }
}