package pl.cyfrowypolsat.cpplayercore.tv.skipintro

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.leanback.media.PlaybackGlueHost
import androidx.leanback.media.PlayerAdapter
import pl.cyfrowypolsat.cpcommon.presentation.extensions.viewBinding
import pl.cyfrowypolsat.cpplayercore.R
import pl.cyfrowypolsat.cpplayercore.databinding.CpplCrTvFragmentSkipIntroBinding

class TvSkipIntroFragment : Fragment() {

    private val binding by viewBinding(CpplCrTvFragmentSkipIntroBinding::bind)
    private lateinit var playbackHost: PlaybackGlueHost
    private lateinit var playerAdapter: PlayerAdapter
    private var introEndPosition: Int = 0

    fun init(playbackHost: PlaybackGlueHost,
             playerAdapter: PlayerAdapter,
             introEndPosition: Int) {
        this.playbackHost = playbackHost
        this.playerAdapter = playerAdapter
        this.introEndPosition = introEndPosition
    }

    fun focusSkipIntroButton() {
        if (!isAdded) return
        binding.tvSkipIntroButton.requestFocus()
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.cppl_cr_tv_fragment_skip_intro, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvSkipIntroButton.requestFocus()
        binding.tvSkipIntroButton.setOnKeyListener(TvSkipIntroKeyListener())
        binding.tvSkipIntroButton.setOnClickListener { playerAdapter.seekTo(introEndPosition.toLong()) }
    }


    inner class TvSkipIntroKeyListener : View.OnKeyListener {
        override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {

            // Hide controls on back button
            if (keyCode == KeyEvent.KEYCODE_BACK
                    || keyCode == KeyEvent.KEYCODE_ESCAPE) {
                if (!playbackHost.isControlsOverlayVisible) {
                    return false
                }
                if (event?.action == KeyEvent.ACTION_UP) {
                    playbackHost.hideControlsOverlay(true)
                }
                return true
            }

            // Show controls on left/right/up/down
            if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT
                    || keyCode == KeyEvent.KEYCODE_DPAD_RIGHT
                    || keyCode == KeyEvent.KEYCODE_DPAD_UP
                    || keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                if (playbackHost.isControlsOverlayVisible) {
                    return false
                }
                if (event?.action == KeyEvent.ACTION_UP) {
                    playbackHost.showControlsOverlay(true)
                }
                return true
            }

            return false
        }

    }


}