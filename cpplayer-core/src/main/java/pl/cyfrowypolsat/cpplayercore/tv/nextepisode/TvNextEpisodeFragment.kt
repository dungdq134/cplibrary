package pl.cyfrowypolsat.cpplayercore.tv.nextepisode

import android.animation.ObjectAnimator
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.animation.doOnEnd
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import pl.cyfrowypolsat.cpcommon.presentation.extensions.gone
import pl.cyfrowypolsat.cpcommon.presentation.extensions.viewBinding
import pl.cyfrowypolsat.cpplayercore.R
import pl.cyfrowypolsat.cpplayercore.databinding.CpplCrTvFragmentNextEpisodeBinding


class TvNextEpisodeFragment : Fragment() {

    companion object {
        private const val PROGRESS_MAX = 1000
        private const val PROGRESS_DURATION = 10000L
    }

    private val binding by viewBinding(CpplCrTvFragmentNextEpisodeBinding::bind)
    
    private lateinit var onNextEpisodeClick: () -> Unit
    private lateinit var onNextEpisodeAutoplay: () -> Unit

    private var nextEpisodeBackground: Drawable? = null
    private var progressObjectAnimator: ObjectAnimator? = null
    private var autoplayEnabled = true

    fun init(autoplayEnabled: Boolean,
             onNextEpisodeClick: () -> Unit,
             onNextEpisodeAutoplay: () -> Unit) {
        this.autoplayEnabled = autoplayEnabled
        this.onNextEpisodeClick = onNextEpisodeClick
        this.onNextEpisodeAutoplay = onNextEpisodeAutoplay
    }

    fun focusNextEpisodeButton() {
        if (!isAdded) return
        binding.tvNextEpisodePlayNextButton.requestFocus()
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.cppl_cr_tv_fragment_next_episode, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupNextEpisodeButton()
        if (autoplayEnabled) {
            setupNextEpisodeProgress()
        } else {
            if (!isAdded) return
            binding.tvNextEpisodePlayNextButton.background = nextEpisodeBackground
        }
        setupCreditsButton()
    }

    override fun onStop() {
        super.onStop()
        cancelNextEpisodeProgress()
    }

    private fun setupNextEpisodeButton() {
        nextEpisodeBackground = binding.tvNextEpisodePlayNextButton.background
        binding.tvNextEpisodePlayNextButton.background = null
        binding.tvNextEpisodePlayNextButton.setOnKeyListener(TvNextEpisodeKeyListener())
        binding.tvNextEpisodePlayNextButton.setOnClickListener { onNextEpisodeClick() }
        Handler().post {
            if (!isAdded) return@post
            binding.tvNextEpisodePlayNextButton.requestFocus()
        }
    }

    private fun setupNextEpisodeProgress() {
        binding.tvNextEpisodeProgress.max = PROGRESS_MAX
        progressObjectAnimator = ObjectAnimator.ofInt(binding.tvNextEpisodeProgress, "progress", 0, PROGRESS_MAX)
                .setDuration(PROGRESS_DURATION)
        progressObjectAnimator?.addUpdateListener { valueAnimator ->
            val progress = valueAnimator.animatedValue as Int
            binding.tvNextEpisodeProgress.progress = progress
        }
        progressObjectAnimator?.doOnEnd {
            onNextEpisodeAutoplay()
        }
        progressObjectAnimator?.start()
    }

    private fun cancelNextEpisodeProgress() {
        progressObjectAnimator?.removeAllListeners()
        progressObjectAnimator?.cancel()
        if (!isAdded) return
        binding.tvNextEpisodeProgress.gone()
        binding.tvNextEpisodePlayNextButton.background = nextEpisodeBackground
    }

    private fun setupCreditsButton() {
        binding.tvNextEpisodeCreditsButton.setOnKeyListener(TvCreditsKeyListener())
        binding.tvNextEpisodeCreditsButton.setOnClickListener { finishFragment() }
    }

    private fun finishFragment() {
        parentFragmentManager.commit { remove(this@TvNextEpisodeFragment) }
    }

    inner class TvNextEpisodeKeyListener : View.OnKeyListener {
        override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {

            if (keyCode == KeyEvent.KEYCODE_BACK
                    || keyCode == KeyEvent.KEYCODE_ESCAPE) {
                if (event?.action == KeyEvent.ACTION_UP) {
                    finishFragment()
                }
                return true
            }

            if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                if (event?.action == KeyEvent.ACTION_UP) {
                    cancelNextEpisodeProgress()
                    binding.tvNextEpisodeCreditsButton.requestFocus()
                }
                return true
            }

            if (keyCode == KeyEvent.KEYCODE_DPAD_UP
                    || keyCode == KeyEvent.KEYCODE_DPAD_DOWN
                    || keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                return true
            }

            return false
        }
    }

    inner class TvCreditsKeyListener : View.OnKeyListener {
        override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {

            if (keyCode == KeyEvent.KEYCODE_BACK
                    || keyCode == KeyEvent.KEYCODE_ESCAPE) {
                if (event?.action == KeyEvent.ACTION_UP) {
                    finishFragment()
                }
                return true
            }

            if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                if (event?.action == KeyEvent.ACTION_UP) {
                    binding.tvNextEpisodePlayNextButton.requestFocus()
                }
                return true
            }


            if (keyCode == KeyEvent.KEYCODE_DPAD_UP
                    || keyCode == KeyEvent.KEYCODE_DPAD_LEFT
                    || keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                return true
            }

            return false
        }
    }


}