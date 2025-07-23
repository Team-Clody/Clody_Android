package com.sopt.clody.presentation.ui.auth.signup

import android.content.Context
import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.airbnb.mvrx.withState
import com.sopt.clody.core.fcm.FcmTokenProvider
import com.sopt.clody.core.login.LoginSdk
import com.sopt.clody.data.remote.dto.request.GoogleSignUpRequestDto
import com.sopt.clody.data.remote.dto.request.SignUpRequestDto
import com.sopt.clody.data.remote.dto.response.SignUpResponseDto
import com.sopt.clody.data.remote.util.NetworkUtil
import com.sopt.clody.domain.repository.AuthRepository
import com.sopt.clody.domain.repository.TokenRepository
import com.sopt.clody.presentation.ui.auth.signup.SignUpContract.Companion.DEFAULT_NICKNAME_MESSAGE
import com.sopt.clody.data.datastore.OAuthDataStore
import com.sopt.clody.data.datastore.OAuthProvider
import com.sopt.clody.presentation.ui.setting.screen.SettingOptionUrls
import com.sopt.clody.presentation.utils.language.LanguageProvider
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class SignUpViewModel @AssistedInject constructor(
    @Assisted initialState: SignUpContract.SignUpState,
    private val loginSdk: LoginSdk,
    private val authRepository: AuthRepository,
    private val tokenRepository: TokenRepository,
    private val fcmTokenProvider: FcmTokenProvider,
    private val oAuthDataStore: OAuthDataStore,
    private val networkUtil: NetworkUtil,
    private val languageProvider: LanguageProvider,
) : MavericksViewModel<SignUpContract.SignUpState>(initialState) {

    private val _intents = Channel<SignUpContract.SignUpIntent>(BUFFERED)
    private val _sideEffects = Channel<SignUpContract.SignUpSideEffect>(BUFFERED)
    val sideEffects = _sideEffects.receiveAsFlow()

    init {
        _intents
            .receiveAsFlow()
            .onEach(::handleIntent)
            .launchIn(viewModelScope)
        postIntent(SignUpContract.SignUpIntent.SetNicknameMaxLength)
        postIntent(SignUpContract.SignUpIntent.SetWebViewUrl)
    }

    fun postIntent(intent: SignUpContract.SignUpIntent) {
        viewModelScope.launch { _intents.send(intent) }
    }

    private suspend fun handleIntent(intent: SignUpContract.SignUpIntent) {
        when (intent) {
            is SignUpContract.SignUpIntent.SetNickname -> handleSetNickname(intent)
            is SignUpContract.SignUpIntent.SetNicknameFocus -> handleSetNicknameFocus(intent)
            is SignUpContract.SignUpIntent.SetNicknameMaxLength -> setNicknameMaxLength()
            is SignUpContract.SignUpIntent.ProceedTerms -> handleProceedTerms()
            is SignUpContract.SignUpIntent.CompleteSignUp -> signUp(intent.context)
            is SignUpContract.SignUpIntent.ClearError -> clearError()
            is SignUpContract.SignUpIntent.ToggleAllChecked -> handleToggleAllChecked(intent)
            is SignUpContract.SignUpIntent.ToggleServiceChecked -> handleToggleServiceChecked(intent)
            is SignUpContract.SignUpIntent.TogglePrivacyChecked -> handleTogglePrivacyChecked(intent)
            is SignUpContract.SignUpIntent.SetWebViewUrl -> setWebViewUrl()
            is SignUpContract.SignUpIntent.OpenWebView -> handleOpenWebView(intent.url)
            SignUpContract.SignUpIntent.BackToTerms -> handleBackToTerms()
        }
    }

    private fun handleSetNickname(intent: SignUpContract.SignUpIntent.SetNickname) {
        val isValid = validateNickname(intent.value)
        setState {
            copy(
                nickname = intent.value,
                isValidNickname = isValid,
                nicknameMessage = if (intent.value.isEmpty() || isValid) {
                    DEFAULT_NICKNAME_MESSAGE
                } else {
                    "사용할 수 없는 닉네임이에요"
                },
            )
        }
    }

    private fun handleSetNicknameFocus(intent: SignUpContract.SignUpIntent.SetNicknameFocus) {
        setState { copy(isNicknameFocused = intent.isFocused) }
    }

    private fun setNicknameMaxLength() {
        setState { copy(nicknameMaxLength = languageProvider.getNicknameMaxLength()) }
    }

    private fun handleProceedTerms() {
        setState { copy(currentStep = SignUpContract.SignUpState.Step.NICKNAME) }
    }

    private fun clearError() {
        setState { copy(errorMessage = null) }
    }

    private fun handleToggleAllChecked(intent: SignUpContract.SignUpIntent.ToggleAllChecked) {
        setState {
            copy(serviceChecked = intent.checked, privacyChecked = intent.checked)
        }
    }

    private fun handleToggleServiceChecked(intent: SignUpContract.SignUpIntent.ToggleServiceChecked) {
        setState { copy(serviceChecked = intent.checked) }
    }

    private fun handleTogglePrivacyChecked(intent: SignUpContract.SignUpIntent.TogglePrivacyChecked) {
        setState { copy(privacyChecked = intent.checked) }
    }

    private fun setWebViewUrl() {
        setState {
            copy(
                serviceUrl = languageProvider.getWebViewUrlFor(SettingOptionUrls.TERMS_OF_SERVICE_URL),
                privacyUrl = languageProvider.getWebViewUrlFor(SettingOptionUrls.PRIVACY_POLICY_URL),
            )
        }
    }

    private suspend fun handleOpenWebView(url: String) {
        _sideEffects.send(SignUpContract.SignUpSideEffect.NavigateToWebView(url))
    }

    private fun handleBackToTerms() {
        setState {
            copy(
                currentStep = SignUpContract.SignUpState.Step.TERMS,
                nickname = "",
                isNicknameFocused = false,
                isValidNickname = true,
                nicknameMessage = SignUpContract.DEFAULT_NICKNAME_MESSAGE,
            )
        }
    }

    private suspend fun signUp(context: Context) {
        val state = withState(this@SignUpViewModel) { it }

        if (!networkUtil.isNetworkAvailable()) {
            setState { copy(errorMessage = "네트워크 연결을 확인해주세요.") }
            return
        }

        setState { copy(isLoading = true) }

        val platform = oAuthDataStore.getPlatform()
        val fcmToken = fcmTokenProvider.getToken().orEmpty()

        if (platform == OAuthProvider.GOOGLE) {
            val idToken = oAuthDataStore.getIdToken()
            if (idToken.isNullOrBlank()) {
                setState { copy(errorMessage = "Google ID Token이 없습니다.", isLoading = false) }
                return
            }

            val request = GoogleSignUpRequestDto(
                idToken = idToken,
                platform = "Android",
                name = state.nickname,
                fcmToken = fcmToken,
            )

            val result = authRepository.signUpWithGoogle(request)
            handleSignUpResult(result, isGoogle = true)
        } else {
            loginSdk.login(context).fold(
                onSuccess = { token ->
                    val request = SignUpRequestDto(
                        platform = OAuthProvider.KAKAO.apiValue,
                        name = state.nickname,
                        fcmToken = fcmToken,
                    )
                    val result = authRepository.signUp("Bearer ${token.value}", request)
                    handleSignUpResult(result, isGoogle = false)
                },
                onFailure = {
                    setState { copy(errorMessage = "로그인에 실패했어요~", isLoading = false) }
                },
            )
        }
    }

    private suspend fun handleSignUpResult(
        result: Result<SignUpResponseDto>,
        isGoogle: Boolean,
    ) {
        result.fold(
            onSuccess = {
                tokenRepository.setTokens(it.accessToken, it.refreshToken)
                if (isGoogle) oAuthDataStore.clear()
                _sideEffects.send(SignUpContract.SignUpSideEffect.NavigateToTimeReminder)
            },
            onFailure = {
                setState { copy(errorMessage = "회원가입에 실패했어요~") }
            },
        )
        setState { copy(isLoading = false) }
    }
    private fun validateNickname(nickname: String): Boolean {
        val state = withState(this@SignUpViewModel) { it }
        setState { copy(nicknameMaxLength = languageProvider.getNicknameMaxLength()) }
        val regex = "^[a-zA-Z가-힣0-9ㄱ-ㅎㅏ-ㅣ가-힣]{2,${state.nicknameMaxLength}}$".toRegex()
        return nickname.matches(regex)
    }

    @AssistedFactory
    interface Factory : AssistedViewModelFactory<SignUpViewModel, SignUpContract.SignUpState> {
        override fun create(state: SignUpContract.SignUpState): SignUpViewModel
    }

    companion object :
        MavericksViewModelFactory<SignUpViewModel, SignUpContract.SignUpState> by hiltMavericksViewModelFactory()
}
