package com.sopt.clody.presentation.ui.login

import android.content.Context
import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.sopt.clody.core.fcm.FcmTokenProvider
import com.sopt.clody.core.login.LoginSdk
import com.sopt.clody.data.remote.dto.request.LoginRequestDto
import com.sopt.clody.domain.repository.AuthRepository
import com.sopt.clody.domain.repository.TokenRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class LoginViewModel @AssistedInject constructor(
    @Assisted initialState: LoginContract.LoginState,
    private val loginSdk: LoginSdk,
    private val authRepository: AuthRepository,
    private val tokenRepository: TokenRepository,
    private val fcmTokenProvider: FcmTokenProvider,
) : MavericksViewModel<LoginContract.LoginState>(initialState) {

    private val _intents = Channel<LoginContract.LoginIntent>(BUFFERED)
    private val _sideEffects = Channel<LoginContract.LoginSideEffect>(BUFFERED)
    val sideEffects = _sideEffects.receiveAsFlow()

    init {
        _intents
            .receiveAsFlow()
            .onEach(::handleIntent)
            .launchIn(viewModelScope)
    }

    fun postIntent(intent: LoginContract.LoginIntent) {
        viewModelScope.launch { _intents.send(intent) }
    }

    private suspend fun handleIntent(intent: LoginContract.LoginIntent) {
        when (intent) {
            is LoginContract.LoginIntent.LoginWithKakao -> loginWithKakao(intent.context)
            LoginContract.LoginIntent.ClearError -> setState { copy(errorMessage = null) }
        }
    }

    private suspend fun loginWithKakao(context: Context) {
        setState { copy(isLoading = true, errorMessage = null) }

        loginSdk.login(context).fold(
            onSuccess = { accessToken ->
                validateUser("Bearer ${accessToken.value}")
            },
            onFailure = { error ->
                setState { copy(isLoading = false) }
                _sideEffects.send(
                    LoginContract.LoginSideEffect.ShowError(
                        error.message ?: "로그인에 실패했습니다.",
                    ),
                )
            },
        )
    }

    private suspend fun validateUser(token: String) {
        val fcmToken = fcmTokenProvider.getToken().orEmpty()
        val request = LoginRequestDto(platform = "kakao", fcmToken = fcmToken)

        authRepository.signIn(token, request).fold(
            onSuccess = {
                tokenRepository.setTokens(it.accessToken, it.refreshToken)
                setState { copy(isLoading = false) }
                _sideEffects.send(LoginContract.LoginSideEffect.NavigateToHome)
            },
            onFailure = { error ->
                val message = error.message.orEmpty()
                setState { copy(isLoading = false) }

                if (message.contains("404") || message.contains("유저가 없습니다")) {
                    _sideEffects.send(LoginContract.LoginSideEffect.NavigateToSignUp)
                } else {
                    _sideEffects.send(LoginContract.LoginSideEffect.ShowError("로그인 실패"))
                }
            },
        )
    }

    @AssistedFactory
    interface Factory : AssistedViewModelFactory<LoginViewModel, LoginContract.LoginState> {
        override fun create(state: LoginContract.LoginState): LoginViewModel
    }

    companion object :
        MavericksViewModelFactory<LoginViewModel, LoginContract.LoginState> by hiltMavericksViewModelFactory()
}
