package com.sopt.clody.presentation.ui.auth.signup

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import com.sopt.clody.data.ClodyFirebaseMessagingService
import com.sopt.clody.data.remote.dto.request.LoginRequestDto
import com.sopt.clody.data.remote.dto.request.SignUpRequestDto
import com.sopt.clody.data.repository.AuthRepository
import com.sopt.clody.data.repository.TokenRepository
import com.sopt.clody.presentation.utils.base.UiState
import com.sopt.clody.presentation.utils.network.ErrorMessages.FAILURE_NETWORK_MESSAGE
import com.sopt.clody.presentation.utils.network.ErrorMessages.FAILURE_TEMPORARY_MESSAGE
import com.sopt.clody.presentation.utils.network.ErrorMessages.UNKNOWN_ERROR
import com.sopt.clody.presentation.utils.network.NetworkUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@OptIn(FlowPreview::class)
@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val tokenRepository: TokenRepository,
    private val networkUtil: NetworkUtil
) : ViewModel() {

    private val _signInState = MutableStateFlow(SignInState())
    val signInState: StateFlow<SignInState> = _signInState

    private val _signUpState = MutableStateFlow(SignUpState())
    val signUpState: StateFlow<SignUpState> = _signUpState

    private var accessToken: String? = null

    private val _nickname = MutableStateFlow("")
    val nickname: StateFlow<String> = _nickname

    private val _isValidNickname = MutableStateFlow(true)
    val isValidNickname: StateFlow<Boolean> = _isValidNickname

    private val _nicknameMessage = MutableStateFlow(DEFAULT_NICKNAME_MESSAGE)
    val nicknameMessage: StateFlow<String> = _nicknameMessage

    init {
        attemptAutoLogin()
        debounceNicknameValidation()
    }

    // 자동 로그인
    private fun attemptAutoLogin() {
        val accessToken = tokenRepository.getAccessToken()
        val refreshToken = tokenRepository.getRefreshToken()

        if (accessToken.isNotBlank() && refreshToken.isNotBlank()) {
            _signInState.value = SignInState(UiState.Success(AUTO_LOGIN_SUCCESS))
        } else {
            _signInState.value = SignInState(UiState.Empty)
        }
    }

    fun signInWithKakao(context: Context) {
        viewModelScope.launch {
            _signInState.value = SignInState(UiState.Loading)
            val tokenResult = runCatching { loginWithKakao(context) }
            tokenResult.onSuccess { token ->
                accessToken = token.accessToken
                fetchKakaoUserInfo(context)
            }.onFailure {
                _signInState.value = SignInState(UiState.Failure(it.localizedMessage ?: UNKNOWN_ERROR))
            }
        }
    }

    fun proceedWithSignUp(context: Context) {
        viewModelScope.launch {
            if (!networkUtil.isNetworkAvailable()) {
                _signUpState.value = SignUpState(UiState.Failure(FAILURE_NETWORK_MESSAGE))
                return@launch
            }
            _signUpState.value = SignUpState(UiState.Loading)
            val tokenResult = runCatching { loginWithKakao(context) }
            tokenResult.onSuccess { token ->
                accessToken = token.accessToken
                performSignUp(context)
            }.onFailure {
                _signUpState.value = SignUpState(UiState.Failure(it.localizedMessage ?: UNKNOWN_ERROR))
            }
        }
    }

    private suspend fun loginWithKakao(context: Context): OAuthToken {
        return suspendCancellableCoroutine { continuation ->
            val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
                if (error != null) {
                    continuation.resumeWithException(error)
                } else if (token != null) {
                    continuation.resume(token)
                }
            }
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
                UserApiClient.instance.loginWithKakaoTalk(context, callback = callback)
            } else {
                UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
            }
        }
    }

    private fun fetchKakaoUserInfo(context: Context) {
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                _signInState.value = SignInState(UiState.Failure(error.localizedMessage))
            } else if (user != null) {
                val fcmToken = ClodyFirebaseMessagingService.getTokenFromPreferences(context) ?: ""
                val requestSignInDto = LoginRequestDto(platform = KAKAO_PLATFORM, fcmToken = fcmToken)
                validateUser("Bearer ${accessToken.orEmpty()}", requestSignInDto)
            }
        }
    }

    private fun validateUser(authorization: String, requestSignInDto: LoginRequestDto) {
        viewModelScope.launch {
            authRepository.signIn(authorization, requestSignInDto).fold(
                onSuccess = { response ->
                    storeTokens(response.accessToken, response.refreshToken)
                    _signInState.value = SignInState(UiState.Success(USER_EXISTS))
                },
                onFailure = {
                    val message = it.localizedMessage ?: UNKNOWN_ERROR
                    val uiState = if (message.contains("404")) {
                        UiState.Failure(USER_NOT_FOUND_ERROR)
                    } else {
                        UiState.Failure(message)
                    }
                    _signInState.value = SignInState(uiState)
                }
            )
        }
    }

    private fun storeTokens(accessToken: String, refreshToken: String) {
        viewModelScope.launch {
            tokenRepository.setTokens(accessToken, refreshToken)
        }
    }

    fun setNickname(nickname: String) {
        _nickname.value = nickname
    }

    private fun performSignUp(context: Context) {
        val authorization = "Bearer ${accessToken.orEmpty()}"
        val fcmToken = ClodyFirebaseMessagingService.getTokenFromPreferences(context) ?: ""
        viewModelScope.launch {
            authRepository.signUp(
                authorization,
                SignUpRequestDto(platform = KAKAO_PLATFORM, name = nickname.value, fcmToken = fcmToken)
            ).fold(
                onSuccess = { response ->
                    _signUpState.value = SignUpState(UiState.Success(SIGN_UP_SUCCESS))
                    storeTokens(response.accessToken, response.refreshToken)
                },
                onFailure = { error ->
                    val errorMessage = if (error.message?.contains("200") == false) {
                        FAILURE_TEMPORARY_MESSAGE
                    } else {
                        error.localizedMessage ?: UNKNOWN_ERROR
                    }
                    _signUpState.value = SignUpState(UiState.Failure(errorMessage))
                }
            )
        }
    }

    private fun debounceNicknameValidation() {
        viewModelScope.launch {
            _nickname
                .debounce(NICKNAME_VALIDATION_DELAY)
                .collectLatest { nickname ->
                    validateNickname(nickname)
                }
        }
    }

    private fun validateNickname(nickname: String) {
        if (nickname.isNotEmpty()) {
            val isValid = nickname.matches(Regex(NICKNAME_PATTERN))
            _isValidNickname.value = isValid
            _nicknameMessage.value = if (isValid) DEFAULT_NICKNAME_MESSAGE else FAILURE_NICKNAME_MESSAGE
        } else {
            _isValidNickname.value = true
            _nicknameMessage.value = DEFAULT_NICKNAME_MESSAGE
        }
    }

    fun resetSignUpState() {
        _signUpState.value = SignUpState()
    }

    companion object {
        private const val AUTO_LOGIN_SUCCESS = "자동 로그인"
        private const val USER_EXISTS = "유저가 이미 존재합니다"
        private const val SIGN_UP_SUCCESS = "회원가입 성공"
        private const val USER_NOT_FOUND_ERROR = "유저를 찾을 수 없습니다"
        private const val KAKAO_PLATFORM = "kakao"

        private const val NICKNAME_VALIDATION_DELAY = 300L
        private const val NICKNAME_PATTERN = "^[a-zA-Z가-힣0-9ㄱ-ㅎㅏ-ㅣ가-힣]{2,10}$"
        private const val DEFAULT_NICKNAME_MESSAGE = "특수문자, 띄어쓰기 없이 작성해주세요"
        private const val FAILURE_NICKNAME_MESSAGE = "사용할 수 없는 닉네임이에요"
    }
}
