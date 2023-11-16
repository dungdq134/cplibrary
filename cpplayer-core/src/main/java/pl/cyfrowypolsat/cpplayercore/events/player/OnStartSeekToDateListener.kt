package pl.cyfrowypolsat.cpplayercore.events.player

import androidx.media3.common.Player
import java.util.*

class OnStartSeekToDateListener(private val date: Date, val onSeekTo: (date: Date) -> Unit) :
    Player.Listener {

    override fun onRenderedFirstFrame() {
        super.onRenderedFirstFrame()
        onSeekTo(date)
    }
}