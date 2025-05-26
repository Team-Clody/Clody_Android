package com.sopt.clody.presentation.ui.auth.signup

import android.content.Context
import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.airbnb.mvrx.withState
import com.sopt.clody.core.fcm.FcmTokenProvider
import com.sopt.clody.core.login.LoginSdk
import com.sopt.clody.data.remote.dto.request.SignUpRequestDto
import com.sopt.clody.data.remote.util.NetworkUtil
import com.sopt.clody.domain.repository.AuthRepository
import com.sopt.clody.domain.repository.TokenRepository
import com.sopt.clody.presentation.ui.auth.signup.SignUpContract.Companion.DEFAULT_NICKNAME_MESSAGE
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
    private val networkUtil: NetworkUtil,
) : MavericksViewModel<SignUpContract.SignUpState>(initialState) {

    private val _intents = Channel<SignUpContract.SignUpIntent>(BUFFERED)
    private val _sideEffects = Channel<SignUpContract.SignUpSideEffect>(BUFFERED)
    val sideEffects = _sideEffects.receiveAsFlow()

    init {
        _intents
            .receiveAsFlow()
            .onEach(::handleIntent)
            .launchIn(viewModelScope)
    }

    fun postIntent(intent: SignUpContract.SignUpIntent) {
        viewModelScope.launch { _intents.send(intent) }
    }

    private suspend fun handleIntent(intent: SignUpContract.SignUpIntent) {
        when (intent) {
            is SignUpContract.SignUpIntent.SetNickname -> {
                val isValid = validateNickname(intent.value)
                setState {
                    copy(
                        nickname = intent.value,
                        isValidNickname = isValid,
                        nicknameMessage = if (intent.value.isEmpty()) {
                            DEFAULT_NICKNAME_MESSAGE
                        } else if (isValid) {
                            DEFAULT_NICKNAME_MESSAGE
                        } else {
                            "사용할 수 없는 닉네임이에요"
                        },
                    )
                }
            }

            is SignUpContract.SignUpIntent.SetNicknameFocus -> {
                setState { copy(isNicknameFocused = intent.isFocused) }
            }

            SignUpContract.SignUpIntent.ProceedTerms -> {
                setState { copy(currentStep = SignUpContract.SignUpState.Step.NICKNAME) }
            }

            is SignUpContract.SignUpIntent.CompleteSignUp -> {
                signUp(intent.context)
            }

            SignUpContract.SignUpIntent.ProceedTerms -> {
                setState { copy(currentStep = SignUpContract.SignUpState.Step.NICKNAME) }
            }

            SignUpContract.SignUpIntent.ClearError -> {
                setState { copy(errorMessage = null) }
            }

            is SignUpContract.SignUpIntent.ToggleAllChecked -> {
                setState {
                    copy(
                        allChecked = intent.checked,
                        serviceChecked = intent.checked,
                        privacyChecked = intent.checked,
                    )
                }
            }

            is SignUpContract.SignUpIntent.ToggleServiceChecked -> {
                setState {
                    val all = intent.checked && this.privacyChecked
                    copy(
                        serviceChecked = intent.checked,
                        allChecked = all,
                    )
                }
            }

            is SignUpContract.SignUpIntent.TogglePrivacyChecked -> {
                setState {
                    val all = this.serviceChecked && intent.checked
                    copy(
                        privacyChecked = intent.checked,
                        allChecked = all,
                    )
                }
            }

            SignUpContract.SignUpIntent.BackToTerms -> {
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
        }
    }

    fun signUp(context: Context) {
        viewModelScope.launch {
            val state = withState(this@SignUpViewModel) { it }
            if (!networkUtil.isNetworkAvailable()) {
                setState { copy(errorMessage = "네트워크 연결을 확인해주세요.") }
                return@launch
            }
            setState { copy(isLoading = true) }
            loginSdk.login(context).fold(
                onSuccess = { accessToken ->
                    launch {
                        val fcm = fcmTokenProvider.getToken().orEmpty()
                        val req = SignUpRequestDto("kakao", state.nickname, fcm)
                        authRepository.signUp("Bearer ${accessToken.value}", req).fold(
                            onSuccess = {
                                tokenRepository.setTokens(it.accessToken, it.refreshToken)
                                _sideEffects.send(SignUpContract.SignUpSideEffect.NavigateToTimeReminder)
                            },
                            onFailure = {
                                setState { copy(errorMessage = it.message ?: "알 수 없는 오류") }
                            },
                        )

                        setState { copy(isLoading = false) }
                    }
                },
                onFailure = {
                    setState { copy(errorMessage = it.message ?: "로그인 실패", isLoading = false) }
                },
            )
        }
    }

    private fun validateNickname(nickname: String): Boolean {
        val regex = "^[a-zA-Z가-힣0-9ㄱ-ㅎㅏ-ㅣ가-힣]{2,10}$".toRegex()
        return nickname.matches(regex)
    }

    @AssistedFactory
    interface Factory : AssistedViewModelFactory<SignUpViewModel, SignUpContract.SignUpState> {
        override fun create(state: SignUpContract.SignUpState): SignUpViewModel
    }

    companion object :
        MavericksViewModelFactory<SignUpViewModel, SignUpContract.SignUpState> by hiltMavericksViewModelFactory()
}
