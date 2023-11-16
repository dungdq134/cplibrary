package pl.cyfrowypolsat.cpplayerexternal.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import pl.cyfrowypolsat.cpplayercore.configuration.PlayerConfig
import pl.cyfrowypolsat.cpplayercore.core.PlayerException
import pl.cyfrowypolsat.cpplayerexternal.domain.GetPlayerConfigUseCase

class CPPlayerViewModel : ViewModel() {

    enum class ScreenState {
        ERROR, LOADING, PLAYER
    }

    protected var disposables = CompositeDisposable()
    private val getPlayerConfigUseCase = GetPlayerConfigUseCase()

    val playerConfig = MutableLiveData<PlayerConfig>()
    val screenState = MutableLiveData<ScreenState>()
    val error = MutableLiveData<Throwable>()

    fun init(url: String) {
        disposables.add(getPlayerConfigUseCase.getPlayerConfig(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { screenState.value = ScreenState.LOADING }
                .subscribe(this::handleGetPlayerConfig, this::handleGetPlayerConfigError))
    }

    private fun handleGetPlayerConfig(playerConfig: PlayerConfig) {
        this.playerConfig.value = playerConfig
        screenState.value = ScreenState.PLAYER
    }

    private fun handleGetPlayerConfigError(throwable: Throwable) {
        error.value = throwable
        screenState.value = ScreenState.ERROR
    }

    fun playerError(exception: PlayerException) {
        error.value = exception
        screenState.value = ScreenState.ERROR
    }

    override fun onCleared() {
        disposables.clear()
    }

}