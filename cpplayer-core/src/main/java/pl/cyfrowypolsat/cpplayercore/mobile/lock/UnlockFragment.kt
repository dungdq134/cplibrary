package pl.cyfrowypolsat.cpplayercore.mobile.lock

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import pl.cyfrowypolsat.cpcommon.presentation.extensions.fadeIn
import pl.cyfrowypolsat.cpcommon.presentation.extensions.fadeOut
import pl.cyfrowypolsat.cpcommon.presentation.extensions.viewBinding
import pl.cyfrowypolsat.cpplayercore.R
import pl.cyfrowypolsat.cpplayercore.databinding.CpplCrMobileFragmentUnlockBinding

class UnlockFragment : Fragment() {

    companion object {
        const val DEFAULT_SHOW_TIMEOUT_MS = 5000L
    }

    var onUnlock: () -> Unit = { }
    var showTimeoutMs: Int? = null

    private val binding by viewBinding(CpplCrMobileFragmentUnlockBinding::bind)
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.cppl_cr_mobile_fragment_unlock, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.unlockButton.onUnlock = {
            onUnlock()
            finishFragment()
        }
        binding.unlockButton.onSlidingCancel = { finishAfterDelay() }
        binding.unlockButton.onSlidingStart = {
            handler.removeCallbacksAndMessages(null)
            binding.unlock.fadeOut(500)
        }
        binding.unlockButton.onProgressReset = {
            binding.unlock.fadeIn(500)
        }
        finishAfterDelay()

    }

    private fun finishAfterDelay() {
        val showTimeoutMs = showTimeoutMs?.toLong() ?: DEFAULT_SHOW_TIMEOUT_MS
        handler.postDelayed({
            if (isAdded) finishFragment()
        }, showTimeoutMs)
    }

    private fun finishFragment() {
        parentFragmentManager.commit { remove(this@UnlockFragment) }
    }

}