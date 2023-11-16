package pl.cyfrowypolsat.cpplayercore.core.exo

import android.content.Context
import androidx.media3.exoplayer.DefaultRenderersFactory
import androidx.media3.exoplayer.audio.AudioCapabilities
import androidx.media3.exoplayer.audio.AudioSink
import androidx.media3.exoplayer.audio.DefaultAudioSink

class CPDefaultRenderersFactory(context: Context) : DefaultRenderersFactory(context) {

    override fun buildAudioSink(context: Context,
                                enableFloatOutput: Boolean,
                                enableAudioTrackPlaybackParams: Boolean,
                                enableOffload: Boolean): AudioSink? {
        return DefaultAudioSink.Builder()
                .setAudioCapabilities(AudioCapabilities.DEFAULT_AUDIO_CAPABILITIES)
                .setEnableFloatOutput(enableFloatOutput)
                .setEnableAudioTrackPlaybackParams(enableAudioTrackPlaybackParams)
                .setOffloadMode(
                        if (enableOffload) DefaultAudioSink.OFFLOAD_MODE_ENABLED_GAPLESS_REQUIRED else DefaultAudioSink.OFFLOAD_MODE_DISABLED)
                .build()
    }

}