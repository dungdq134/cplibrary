package pl.cyfrowypolsat.cpplayercore.core.ima

import android.os.Handler
import android.os.Looper
import com.google.ads.interactivemedia.v3.api.AdEvent
import com.google.ads.interactivemedia.v3.api.player.AdMediaInfo
import com.google.ads.interactivemedia.v3.api.player.VideoAdPlayer
import com.google.ads.interactivemedia.v3.api.player.VideoProgressUpdate
import pl.cyfrowypolsat.cpplayercore.core.PlayerController

/*
This class is responsible for fixing 2 IMA SDK bugs:
1. Sometimes a CONTENT_RESUME_REQUESTED event is not being called after ad block. When this occur, we release an ads loader.
2. Sometimes IMA throw an 402 error which breaks ads displaying (ads are looping etc). When this occur, we release an ads loader.
 */

class ImaBugFixManager(private val playerController: PlayerController) : VideoAdPlayer.VideoAdPlayerCallback {

    private var contentResumeRequestedCalled = false

    fun onAdEvent(adEvent: AdEvent?) {
        if (adEvent?.type == AdEvent.AdEventType.LOG
                && isIma402Error(adEvent)) {
            playerController.releaseAdsLoader()
        }
        if (adEvent?.type == AdEvent.AdEventType.CONTENT_RESUME_REQUESTED) {
            contentResumeRequestedCalled = true
        }
    }

    override fun onEnded(adMediaInfo: AdMediaInfo?) {
        contentResumeRequestedCalled = false

        Handler(Looper.getMainLooper()).postDelayed({
            if (playerController.isPlayingAdvert().not()
                    && contentResumeRequestedCalled.not()) {
                playerController.releaseAdsLoader()
            }
        }, 1000)
    }

    private fun isIma402Error(adEvent: AdEvent?): Boolean {
        return adEvent?.adData?.get("errorCode").equals("402")
    }

    override fun onAdProgress(adMediaInfo: AdMediaInfo?, progressUpdate: VideoProgressUpdate?) {

    }

    override fun onBuffering(adMediaInfo: AdMediaInfo?) {

    }

    override fun onContentComplete() {

    }

    override fun onError(adMediaInfo: AdMediaInfo?) {

    }

    override fun onLoaded(adMediaInfo: AdMediaInfo?) {

    }

    override fun onPause(adMediaInfo: AdMediaInfo?) {

    }

    override fun onPlay(adMediaInfo: AdMediaInfo?) {

    }

    override fun onResume(adMediaInfo: AdMediaInfo?) {

    }

    override fun onVolumeChanged(adMediaInfo: AdMediaInfo?, p1: Int) {

    }

}