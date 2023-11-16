package pl.cyfrowypolsat.cpplayercore.mobile.nextepisode

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.animation.doOnEnd
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import pl.cyfrowypolsat.cpcommon.core.extensions.dp
import pl.cyfrowypolsat.cpcommon.presentation.extensions.viewBinding
import pl.cyfrowypolsat.cpplayercore.R
import pl.cyfrowypolsat.cpplayercore.databinding.CpplCrMobileFragmentNextEpisodeBinding
import pl.cyfrowypolsat.cpplayercore.mobile.exo.ExoPlayerControlView

class NextEpisodeFragment : Fragment() {

    companion object {
        private const val PROGRESS_MAX = 1000
        private const val PROGRESS_DURATION = 10000L
    }

    private val binding by viewBinding(CpplCrMobileFragmentNextEpisodeBinding::bind)
    private var playerControlView: ExoPlayerControlView? = null
    private lateinit var onNextEpisodeClick: () -> Unit
    private lateinit var onNextEpisodeAutoplay: () -> Unit
    private var progressObjectAnimator: ObjectAnimator? = null
    private var autoplayEnabled = true

    fun init(playerControlView: ExoPlayerControlView?,
             autoplayEnabled: Boolean,
             onNextEpisodeClick: () -> Unit,
             onNextEpisodeAutoplay: () -> Unit) {
        this.playerControlView = playerControlView
        this.autoplayEnabled = autoplayEnabled
        this.onNextEpisodeClick = onNextEpisodeClick
        this.onNextEpisodeAutoplay = onNextEpisodeAutoplay
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.cppl_cr_mobile_fragment_next_episode, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupNextEpisodeButton()
        setupCloseButton()
        if (autoplayEnabled) {
            setupNextEpisodeProgress()
        }
        view.post {
            if (isAdded) {
                playerControlView?.updateTitleEndMargin(binding.nextEpisodeContainer.width + 24.dp)
            }
        }
    }

    private fun setupNextEpisodeButton() {
        binding.nextEpisodeButton.setOnClickListener {
            onNextEpisodeClick()
            finishFragment()
        }
    }

    private fun setupNextEpisodeProgress() {
        binding.nextEpisodeProgress.max = PROGRESS_MAX
        progressObjectAnimator = ObjectAnimator.ofInt(binding.nextEpisodeProgress, "progress", 100, PROGRESS_MAX)
                .setDuration(PROGRESS_DURATION)
        progressObjectAnimator?.addUpdateListener { valueAnimator ->
            val progress = valueAnimator.animatedValue as Int
            binding.nextEpisodeProgress.progress = progress
        }
        progressObjectAnimator?.doOnEnd {
            onNextEpisodeAutoplay()
            finishFragment()
        }
        progressObjectAnimator?.start()
    }

    private fun setupCloseButton() {
        binding.nextEpisodeCloseButton.setOnClickListener { finishFragment() }
    }

    override fun onDestroy() {
        super.onDestroy()
        playerControlView?.updateTitleEndMargin(0)
    }

    override fun onStop() {
        super.onStop()
        progressObjectAnimator?.removeAllListeners()
        progressObjectAnimator?.cancel()
    }

    private fun finishFragment() {
        parentFragmentManager.commit { remove(this@NextEpisodeFragment) }
    }
}