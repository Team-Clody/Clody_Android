package com.sopt.clody.presentation.ui.login

import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.sopt.clody.core.fcm.FcmTokenProvider
import com.sopt.clody.core.login.LoginSdk
import com.sopt.clody.data.datastore.OAuthDataStore
import com.sopt.clody.data.datastore.OAuthProvider
import com.sopt.clody.data.remote.dto.request.GoogleSignUpRequestDto
import com.sopt.clody.data.remote.dto.request.LoginRequestDto
import com.sopt.clody.domain.repository.AuthRepository
import com.sopt.clody.domain.repository.TokenRepository
import com.sopt.clody.presentation.ui.login.LoginContract.LoginIntent
import com.sopt.clody.presentation.utils.language.LanguageProvider
import com.sopt.clody.presentation.utils.network.ErrorMessageProvider
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
    private val oauthDataStore: OAuthDataStore,
    private val languageProvider: LanguageProvider,
    private val errorMessageProvider: ErrorMessageProvider,
) : MavericksViewModel<LoginContract.LoginState>(initialState) {

    private val _intents = Channel<LoginIntent>(BUFFERED)
    private val _sideEffects = Channel<LoginContract.LoginSideEffect>(BUFFERED)
    val sideEffects = _sideEffects.receiveAsFlow()

    init {
        setState { copy(loginType = languageProvider.getLoginType()) }
        _intents
            .receiveAsFlow()
            .onEach(::handleIntent)
            .launchIn(viewModelScope)
    }

    fun postIntent(intent: LoginIntent) {
        viewModelScope.launch { _intents.send(intent) }
    }

    private suspend fun handleIntent(intent: LoginIntent) {
        when (intent) {
            is LoginIntent.ClearError -> setState { copy(errorMessage = null) }
            is LoginIntent.LoginOAuth -> handleLoginOAuth(intent)
        }
    }

    private suspend fun handleLoginOAuth(intent: LoginIntent.LoginOAuth) {
        setState { copy(isLoading = true, errorMessage = null) }

        when (intent.provider) {
            OAuthProvider.KAKAO -> {
                loginSdk.login(intent.context!!).fold(
                    onSuccess = { token ->
                        validateKakaoUser(token.value)
                    },
                    onFailure = { error ->
                        setState { copy(isLoading = false) }
                        _sideEffects.send(LoginContract.LoginSideEffect.ShowError(errorMessageProvider.getLoginFailedError()))
                    },
                )
            }

            OAuthProvider.GOOGLE -> {
                val idToken = intent.idToken
                if (idToken.isNullOrBlank()) {
                    _sideEffects.send(LoginContract.LoginSideEffect.ShowError(errorMessageProvider.getLoginFailedError()))
                    return
                }

                validateGoogleUser(idToken)
            }
        }
    }

    private suspend fun validateKakaoUser(kakaoToken: String) {
        val fcmToken = fcmTokenProvider.getToken().orEmpty()
        val request = LoginRequestDto(platform = OAuthProvider.KAKAO.apiValue, fcmToken = fcmToken)

        authRepository.signIn("Bearer $kakaoToken", request).fold(
            onSuccess = {
                tokenRepository.setTokens(it.accessToken, it.refreshToken)
                setState { copy(isLoading = false) }
                _sideEffects.send(LoginContract.LoginSideEffect.NavigateToHome)
            },
            onFailure = { error ->
                setState { copy(isLoading = false) }
                val msg = error.message.orEmpty()
                if (msg.contains("404") || msg.contains("유저가 없습니다")) {
                    _sideEffects.send(LoginContract.LoginSideEffect.NavigateToSignUp)
                } else {
                    _sideEffects.send(LoginContract.LoginSideEffect.ShowError(msg))
                }
            },
        )
    }

    private suspend fun validateGoogleUser(idToken: String) {
        val fcmToken = fcmTokenProvider.getToken().orEmpty()
        val request = GoogleSignUpRequestDto(
            idToken = idToken,
            fcmToken = fcmToken,
        )

        authRepository.signUpWithGoogle(request).fold(
            onSuccess = {
                tokenRepository.setTokens(it.accessToken, it.refreshToken)
                setState { copy(isLoading = false) }
                _sideEffects.send(LoginContract.LoginSideEffect.NavigateToHome)
            },
            onFailure = { error ->
                setState { copy(isLoading = false) }

                val msg = error.message.orEmpty()
                if (msg.contains("500") || msg.contains("유저가 없습니다")) {
                    oauthDataStore.saveIdToken(idToken)
                    oauthDataStore.savePlatform(OAuthProvider.GOOGLE)
                    _sideEffects.send(LoginContract.LoginSideEffect.NavigateToSignUp)
                } else {
                    _sideEffects.send(LoginContract.LoginSideEffect.ShowError(msg))
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
