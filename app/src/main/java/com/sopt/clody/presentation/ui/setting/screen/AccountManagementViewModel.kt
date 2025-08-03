package com.sopt.clody.presentation.ui.setting.screen

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sopt.clody.core.network.NetworkConnectivityObserver
import com.sopt.clody.core.network.NetworkStatus
import com.sopt.clody.data.datastore.TokenDataStore
import com.sopt.clody.data.remote.dto.request.ModifyNicknameRequestDto
import com.sopt.clody.domain.repository.AccountManagementRepository
import com.sopt.clody.presentation.ui.auth.signup.NicknameMessage
import com.sopt.clody.presentation.utils.language.LanguageProvider
import com.sopt.clody.presentation.utils.network.ErrorMessageProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountManagementViewModel @Inject constructor(
    private val accountManagementRepository: AccountManagementRepository,
    private val tokenDataStore: TokenDataStore,
    private val networkConnectivityObserver: NetworkConnectivityObserver,
    @ApplicationContext private val context: Context,
    private val languageProvider: LanguageProvider,
    private val errorMessageProvider: ErrorMessageProvider,
) : ViewModel() {
    private val _userInfoState = MutableStateFlow<UserInfoState>(UserInfoState.Idle)
    val userInfoState: StateFlow<UserInfoState> = _userInfoState

    private val _userNicknameState = MutableStateFlow<UserNicknameState>(UserNicknameState.Idle)
    val userNicknameState: StateFlow<UserNicknameState> = _userNicknameState

    private val _isValidNickname = MutableStateFlow(true)
    val isValidNickname: StateFlow<Boolean> = _isValidNickname

    private val _nicknameMessage = MutableStateFlow(NicknameMessage.DEFAULT)
    val nicknameMessage: StateFlow<NicknameMessage> = _nicknameMessage

    private val _logOutState = MutableStateFlow<LogOutState>(LogOutState.Idle)
    val logOutState: StateFlow<LogOutState> = _logOutState

    private val _revokeAccountState = MutableStateFlow<RevokeAccountState>(RevokeAccountState.Idle)
    val revokeAccountState: StateFlow<RevokeAccountState> = _revokeAccountState

    private val _showFailureDialog = MutableStateFlow(false)
    val showFailureDialog: StateFlow<Boolean> = _showFailureDialog

    private val _failureDialogMessage = MutableStateFlow("")
    val failureDialogMessage: StateFlow<String> = _failureDialogMessage

    private val _nicknameMaxLength = MutableStateFlow(languageProvider.getNicknameMaxLength())
    val nicknameMaxLength: StateFlow<Int> = _nicknameMaxLength

    private val maxRetryCount = 3
    private var retryCount = 0

    fun fetchUserInfo() {
        if (retryCount >= maxRetryCount) return
        _userInfoState.value = UserInfoState.Loading
        viewModelScope.launch {
            if (networkConnectivityObserver.networkStatus.first() == NetworkStatus.Unavailable) {
                _userInfoState.value = UserInfoState.Failure(errorMessageProvider.getNetworkError())
                return@launch
            }
            val result = accountManagementRepository.getUserInfo()
            _userInfoState.value = result.fold(
                onSuccess = {
                    val platformEnum = it.platform
                    retryCount = 0
                    UserInfoState.Success(it.copy(platform = platformEnum))
                },
                onFailure = {
                    retryCount++
                    if (retryCount >= maxRetryCount) {
                        UserInfoState.Failure(errorMessageProvider.getTemporaryError())
                    } else {
                        val errorMessage = if (it.message?.contains("200") == false) {
                            errorMessageProvider.getTemporaryError()
                        } else {
                            errorMessageProvider.getUnknownError()
                        }
                        UserInfoState.Failure(errorMessage)
                    }
                },
            )
        }
    }

    fun changeNickname(modifyNicknameRequestDto: ModifyNicknameRequestDto) {
        viewModelScope.launch {
            if (networkConnectivityObserver.networkStatus.first() == NetworkStatus.Unavailable) {
                _failureDialogMessage.value = errorMessageProvider.getNetworkError()
                _showFailureDialog.value = true
            }
            _userNicknameState.value = UserNicknameState.Loading
            val result = accountManagementRepository.modifyNickname(modifyNicknameRequestDto)
            _userNicknameState.value = result.fold(
                onSuccess = { UserNicknameState.Success(it) },
                onFailure = {
                    _failureDialogMessage.value = if (it.message?.contains("200") == false) {
                        errorMessageProvider.getTemporaryError()
                    } else {
                        errorMessageProvider.getUnknownError()
                    }
                    _showFailureDialog.value = true
                    UserNicknameState.Failure(_failureDialogMessage.value)
                },
            )
        }
    }

    fun validateNickname(nickname: String) {
        if (nickname.isNotEmpty()) {
            val isValid = nickname.matches(Regex("^[a-zA-Z가-힣0-9ㄱ-ㅎㅏ-ㅣ가-힣]{2,${_nicknameMaxLength.value}}$"))
            _isValidNickname.value = isValid
            _nicknameMessage.value = if (isValid) NicknameMessage.DEFAULT else NicknameMessage.INVALID
        } else {
            _isValidNickname.value = true
            _nicknameMessage.value = NicknameMessage.DEFAULT
        }
    }

    fun resetUserNicknameState() {
        _userNicknameState.value = UserNicknameState.Idle
    }

    fun logOutAccount() {
        viewModelScope.launch {
            runCatching {
                tokenDataStore.clearInfo()
            }.onSuccess {
                _logOutState.value = LogOutState.Success
            }.onFailure {
                _logOutState.value = LogOutState.Failure("로그아웃에 실패했습니다")
            }
        }
    }

    fun revokeAccount() {
        _revokeAccountState.value = RevokeAccountState.Loading
        viewModelScope.launch {
            if (networkConnectivityObserver.networkStatus.first() == NetworkStatus.Unavailable) {
                _failureDialogMessage.value = errorMessageProvider.getNetworkError()
                _showFailureDialog.value = true
            }
            val result = accountManagementRepository.revokeAccount()
            result.fold(
                onSuccess = {
                    tokenDataStore.clearInfo()
                    _revokeAccountState.value = RevokeAccountState.Success(it)
                },
                onFailure = {
                    _failureDialogMessage.value = if (it.message?.contains("200") == false) {
                        errorMessageProvider.getTemporaryError()
                    } else {
                        it.localizedMessage ?: errorMessageProvider.getUnknownError()
                    }
                    _showFailureDialog.value = true
                    RevokeAccountState.Failure(_failureDialogMessage.value)
                },
            )
        }
    }

    fun dismissFailureDialog() {
        _showFailureDialog.value = false
        _failureDialogMessage.value = ""
    }
}
