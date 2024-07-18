package com.sopt.clody.presentation.ui.auth.signup

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import com.sopt.clody.data.remote.dto.request.LoginRequestDto
import com.sopt.clody.data.remote.dto.request.SignUpRequestDto
import com.sopt.clody.data.repository.AuthRepository
import com.sopt.clody.data.repository.TokenRepository
import com.sopt.clody.presentation.utils.base.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val tokenRepository: TokenRepository
) : ViewModel() {

    private val _signInState = MutableStateFlow(SignInState())
    val signInState: StateFlow<SignInState> = _signInState

    private val _signUpState = MutableStateFlow(SignUpState())
    val signUpState: StateFlow<SignUpState> = _signUpState

    private val _nickname = MutableStateFlow("")
    val nickname: StateFlow<String> = _nickname

    private var accessToken: String? = null

    init {
        attemptAutoLogin()
    }

    // 자동 로그인
    private fun attemptAutoLogin() {
        val accessToken = tokenRepository.getAccessToken()
        val refreshToken = tokenRepository.getRefreshToken()

        if (accessToken.isNotBlank() && refreshToken.isNotBlank()) {
            _signInState.value = SignInState(UiState.Success("Auto login successful"))
        } else {
            _signInState.value = SignInState(UiState.Empty)
        }
    }

    // 카카오 로그인
    fun signInWithKakao(context: Context) {
        _signInState.value = SignInState(UiState.Loading)
        val callback = createKakaoLoginCallback { fetchKakaoUserInfo() }
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
            UserApiClient.instance.loginWithKakaoTalk(context, callback = callback)
        } else {
            UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
        }
    }

    // 회원가입 진행
    fun proceedWithSignUp(context: Context) {
        _signUpState.value = SignUpState(UiState.Loading)
        val callback = createKakaoLoginCallback { performSignUp() }
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
            UserApiClient.instance.loginWithKakaoTalk(context, callback = callback)
        } else {
            UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
        }
    }

    // 카카오 로그인 콜백 생성
    private fun createKakaoLoginCallback(onSuccess: () -> Unit): (OAuthToken?, Throwable?) -> Unit {
        return { token, error ->
            if (error != null) {
                _signInState.value = SignInState(UiState.Failure(error.localizedMessage))
            } else if (token != null) {
                accessToken = token.accessToken
                onSuccess()
            }
        }
    }

    // 사용자 정보 가져오기
    private fun fetchKakaoUserInfo() {
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                _signInState.value = SignInState(UiState.Failure(error.localizedMessage))
            } else if (user != null) {
                _signInState.value = SignInState(UiState.Success("Kakao login successful"))
                val requestSignInDto = LoginRequestDto(platform = "kakao")
                validateUser("Bearer ${accessToken.orEmpty()}", requestSignInDto)
            }
        }
    }

    // 사용자 검증
    private fun validateUser(authorization: String, requestSignInDto: LoginRequestDto) {
        viewModelScope.launch {
            authRepository.signIn(authorization, requestSignInDto).onSuccess { response ->
                _signInState.value = SignInState(UiState.Success("User exists"))
                storeTokens(response.accessToken, response.refreshToken)
            }.onFailure {
                val message = it.localizedMessage ?: "Unknown error"
                val uiState = if (message.contains("404") || message.contains("User not found")) {
                    UiState.Failure("404 Error or User not found")
                } else {
                    UiState.Failure(message)
                }
                _signInState.value = SignInState(uiState)
            }
        }
    }

    // 토큰 저장
    private fun storeTokens(accessToken: String, refreshToken: String) {
        viewModelScope.launch {
            tokenRepository.setTokens(accessToken, refreshToken)
        }
    }

    // 닉네임 설정
    fun setNickname(nickname: String) {
        _nickname.value = nickname
    }

    // 회원가입 수행
    private fun performSignUp() {
        val authorization = "Bearer ${accessToken.orEmpty()}"
        _signUpState.value = SignUpState(UiState.Loading)
        viewModelScope.launch {
            authRepository.signUp(
                authorization,
                SignUpRequestDto(
                    platform = "kakao",
                    name = nickname.value
                )
            ).onSuccess { response ->
                _signUpState.value = SignUpState(UiState.Success("Sign up successful"))
                storeTokens(response.accessToken, response.refreshToken)
            }.onFailure {
                _signUpState.value = SignUpState(UiState.Failure(it.localizedMessage))
            }
        }
    }
}
