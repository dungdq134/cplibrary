package pl.cyfrowypolsat.cpplayercore.mobile.skipintro

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import pl.cyfrowypolsat.cpcommon.core.extensions.dp
import pl.cyfrowypolsat.cpcommon.presentation.extensions.setMarginBottom
import pl.cyfrowypolsat.cpcommon.presentation.extensions.viewBinding
import pl.cyfrowypolsat.cpplayercore.R
import pl.cyfrowypolsat.cpplayercore.databinding.CpplCrMobileFragmentSkipIntroBinding
import pl.cyfrowypolsat.cpplayercore.mobile.exo.ExoPlayerControlView

class SkipIntroFragment : Fragment() {

    private val binding by viewBinding(CpplCrMobileFragmentSkipIntroBinding::bind)

    private var playerControlView: ExoPlayerControlView? = null
    private var introEndPosition: Int = 0

    fun init(playerControlView: ExoPlayerControlView?,
             introEndPosition: Int) {
        this.playerControlView = playerControlView
        this.introEndPosition = introEndPosition
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.cppl_cr_mobile_fragment_skip_intro, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.skipIntroButton.setOnClickListener { playerControlView?.player?.seekTo(introEndPosition.toLong()) }
        view.post {
            if (isAdded) {
                setPortraitOrLandscapeStyles(resources.configuration)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        playerControlView?.updateTitleEndMargin(0)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        setPortraitOrLandscapeStyles(newConfig)
    }

    private fun setPortraitOrLandscapeStyles(config: Configuration?) {
        config ?: return
        if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
            binding.skipIntroButton.setMarginBottom(84.dp)
            playerControlView?.updateTitleEndMargin(0)
        } else {
            binding.skipIntroButton.setMarginBottom(0)
            playerControlView?.updateTitleEndMargin(binding.skipIntroButton.width + 24.dp)
        }
    }

}