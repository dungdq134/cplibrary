package pl.cyfrowypolsat.cpplayercore.core.droppedframes

import androidx.media3.common.C
import androidx.media3.exoplayer.analytics.AnalyticsListener
import androidx.media3.exoplayer.source.MediaLoadData
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import kotlin.math.min

class DroppedFramesManager(private val trackSelector: DefaultTrackSelector,
                           private val maxQuality: Int) : AnalyticsListener {

    companion object {
        private const val MAX_DROPPED_FRAMES_PER_SECOND = 0.2f
        private const val STANDARD_VIDEO_FRAME_RATE = 25
        private const val MEDIUM_VIDEO_QUALITY = 1079
    }

    private var currentFrameRate = STANDARD_VIDEO_FRAME_RATE

    override fun onDroppedVideoFrames(eventTime: AnalyticsListener.EventTime,
                                      droppedFrames: Int,
                                      elapsedMs: Long) {
        if (elapsedMs < 1000) return
        if (droppedFrames <= 0) return
        val elapsedSeconds = elapsedMs / 1000
        val droppedFramesPerSecond = droppedFrames / elapsedSeconds.toFloat()
        if (droppedFramesPerSecond > MAX_DROPPED_FRAMES_PER_SECOND) {
            if (currentFrameRate > STANDARD_VIDEO_FRAME_RATE) {
                val parameters = trackSelector.buildUponParameters()
                parameters.setMaxVideoFrameRate(STANDARD_VIDEO_FRAME_RATE)
                trackSelector.setParameters(parameters)
            } else {
                val parameters = trackSelector.buildUponParameters()
                parameters.setMaxVideoSize(Int.MAX_VALUE, min(maxQuality, MEDIUM_VIDEO_QUALITY))
                trackSelector.setParameters(parameters)
            }
        }
    }

    override fun onDownstreamFormatChanged(eventTime: AnalyticsListener.EventTime,
                                           mediaLoadData: MediaLoadData) {
        if (mediaLoadData.trackType == C.TRACK_TYPE_VIDEO) {
            val format = mediaLoadData.trackFormat ?: return
            currentFrameRate = format.frameRate.toInt()
        }
    }

}