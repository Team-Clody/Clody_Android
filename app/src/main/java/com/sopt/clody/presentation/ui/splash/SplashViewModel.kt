package com.sopt.clody.presentation.ui.splash

import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.airbnb.mvrx.withState
import com.sopt.clody.BuildConfig
import com.sopt.clody.domain.appupdate.AppUpdateChecker
import com.sopt.clody.domain.model.AppUpdateState
import com.sopt.clody.domain.repository.TokenRepository
import com.sopt.clody.presentation.utils.amplitude.AmplitudeConstraints
import com.sopt.clody.presentation.utils.amplitude.AmplitudeUtils
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class SplashViewModel @AssistedInject constructor(
    @Assisted initialState: SplashContract.SplashState,
    private val tokenRepository: TokenRepository,
    private val appUpdateChecker: AppUpdateChecker,
) : MavericksViewModel<SplashContract.SplashState>(initialState) {

    private val _intents = Channel<SplashContract.SplashIntent>(BUFFERED)
    private val _sideEffects = Channel<SplashContract.SplashSideEffect>(BUFFERED)
    val sideEffects = _sideEffects.receiveAsFlow()

    init {
        _intents
            .receiveAsFlow()
            .onEach(::handleIntent)
            .launchIn(viewModelScope)
    }

    fun postIntent(intent: SplashContract.SplashIntent) {
        viewModelScope.launch { _intents.send(intent) }
    }

    private suspend fun handleIntent(intent: SplashContract.SplashIntent) {
        when (intent) {
            is SplashContract.SplashIntent.InitSplash -> {
                if (intent.startIntent.hasExtra("google.message_id")) {
                    AmplitudeUtils.trackEvent(AmplitudeConstraints.ALARM)
                }
                attemptAutoLogin()
                checkVersionAndNavigate()
            }

            SplashContract.SplashIntent.ClearUpdateState -> {
                setState { copy(updateState = AppUpdateState.Latest) }
            }
        }
    }

    private fun attemptAutoLogin() {
        val isLoggedIn = tokenRepository.getAccessToken().isNotBlank() &&
            tokenRepository.getRefreshToken().isNotBlank()
        setState { copy(isUserLoggedIn = isLoggedIn) }
    }

    private fun checkVersionAndNavigate() {
        viewModelScope.launch {
            val updateState = appUpdateChecker.getAppUpdateState(BuildConfig.VERSION_NAME)
            setState { copy(updateState = updateState) }

            if (updateState == AppUpdateState.Latest) {
                delay(1000)

                val isLoggedIn = withState(this@SplashViewModel) {
                    it.isUserLoggedIn
                }

                if (isLoggedIn == true) {
                    _sideEffects.send(SplashContract.SplashSideEffect.NavigateToHome)
                } else {
                    _sideEffects.send(SplashContract.SplashSideEffect.NavigateToLogin)
                }
            }
        }
    }

    @AssistedFactory
    interface Factory : AssistedViewModelFactory<SplashViewModel, SplashContract.SplashState> {
        override fun create(state: SplashContract.SplashState): SplashViewModel
    }

    companion object :
        MavericksViewModelFactory<SplashViewModel, SplashContract.SplashState> by hiltMavericksViewModelFactory()
}
