package pl.cyfrowypolsat.cpplayerexternal.presentation

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import pl.cyfrowypolsat.cpcommon.presentation.extensions.gone
import pl.cyfrowypolsat.cpcommon.presentation.extensions.visible
import pl.cyfrowypolsat.cpplayercore.configuration.PlayerConfig
import pl.cyfrowypolsat.cpplayercore.core.PlayerException
import pl.cyfrowypolsat.cpplayercore.events.player.PlayerListener
import pl.cyfrowypolsat.cpplayerexternal.BuildConfig
import pl.cyfrowypolsat.cpplayerexternal.databinding.CpplActivityCpplayerBinding
import pl.cyfrowypolsat.cpplayerexternal.presentation.CPPlayerViewModel.ScreenState
import pl.cyfrowypolsat.cpplayerexternal.presentation.error.ErrorMessageProvider
import pl.cyfrowypolsat.cpplayerexternal.presentation.model.CPPlayerExtra
import timber.log.Timber

class CPPlayerActivity : AppCompatActivity(), PlayerListener {

    companion object {
        private const val CPPLAYER_EXTRA_KEY = "CPPLAYER_EXTRA_KEY"

        fun startActivity(activity: Activity,
                          playerExtra: CPPlayerExtra) {
            val intent = buildIntent(activity, playerExtra)
            activity.startActivity(intent)
        }

        fun buildIntent(activity: Activity,
                        playerExtra: CPPlayerExtra): Intent {
            val intent = Intent(activity, CPPlayerActivity::class.java)
            intent.putExtra(CPPLAYER_EXTRA_KEY, playerExtra)
            return intent
        }
    }

    private val viewModel: CPPlayerViewModel by viewModels()
    private lateinit var binding: CpplActivityCpplayerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        plantTimber()
        super.onCreate(savedInstanceState)
        binding = CpplActivityCpplayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        hideSystemUI()
        initViewModel()
        setListeners()
    }

    private fun setListeners() {
        binding.cpplPlayerErrorButton.setOnClickListener { finish() }
    }

    private fun initViewModel() {
        val playerExtra = intent.getParcelableExtra<CPPlayerExtra>(CPPLAYER_EXTRA_KEY)
        if (playerExtra == null) {
            finish()
            return
        }
        viewModel.playerConfig.observe(this, this::setupPlayer)
        viewModel.screenState.observe(this, this::setScreenState)
        viewModel.error.observe(this, this::setErrorMessage)
        viewModel.init(playerExtra.serviceUrl)
    }

    private fun setupPlayer(playerConfig: PlayerConfig) {
        binding.cpplPlayerView.setup(playerConfig = playerConfig,
                activity = this,
                lifecycle = lifecycle,
                fragmentManager = supportFragmentManager,
                playerListener = this)
    }

    private fun setScreenState(screenState: ScreenState) {
        binding.cpplPlayerView.gone()
        binding.cpplPlayerProgress.gone()
        binding.cpplPlayerErrorContainer.gone()
        when(screenState) {
            ScreenState.LOADING -> binding.cpplPlayerProgress.visible()
            ScreenState.PLAYER -> binding.cpplPlayerView.visible()
            ScreenState.ERROR -> binding.cpplPlayerErrorContainer.visible()
        }
    }

    private fun setErrorMessage(throwable: Throwable) {
        val errorMessage = ErrorMessageProvider.getMessage(throwable, this)
        binding.cpplPlayerErrorText.text = errorMessage
    }

    override fun onPlayerError(e: PlayerException) {
        viewModel.playerError(e)
    }

    private fun plantTimber() {
        if (BuildConfig.DEBUG && Timber.treeCount == 0) {
            Timber.plant(Timber.DebugTree())
        }
    }

    private fun hideSystemUI() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, binding.root).apply {
            hide(WindowInsetsCompat.Type.systemBars())
            systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) hideSystemUI()
    }

}