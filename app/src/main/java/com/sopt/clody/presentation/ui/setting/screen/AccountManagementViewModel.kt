package com.sopt.clody.presentation.ui.setting.screen

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sopt.clody.data.datastore.TokenDataStore
import com.sopt.clody.data.remote.dto.RequestModifyNicknameDto
import com.sopt.clody.data.repository.AccountManagementRepository
import com.sopt.clody.presentation.utils.network.ErrorMessages.FAILURE_NETWORK_MESSAGE
import com.sopt.clody.presentation.utils.network.ErrorMessages.FAILURE_TEMPORARY_MESSAGE
import com.sopt.clody.presentation.utils.network.ErrorMessages.UNKNOWN_ERROR
import com.sopt.clody.data.remote.util.NetworkUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountManagementViewModel @Inject constructor(
    private val accountManagementRepository: AccountManagementRepository,
    private val tokenDataStore: TokenDataStore,
    private val networkUtil: NetworkUtil,
    @ApplicationContext private val context: Context
) : ViewModel() {
    private val _userInfoState = MutableStateFlow<UserInfoState>(UserInfoState.Idle)
    val userInfoState: StateFlow<UserInfoState> = _userInfoState

    private val _userNicknameState = MutableStateFlow<UserNicknameState>(UserNicknameState.Idle)
    val userNicknameState: StateFlow<UserNicknameState> = _userNicknameState

    private val _isValidNickname = MutableStateFlow(true)
    val isValidNickname: StateFlow<Boolean> = _isValidNickname

    private val _nicknameMessage = MutableStateFlow(DEFAULT_NICKNAME_MESSAGE)
    val nicknameMessage: StateFlow<String> = _nicknameMessage

    private val _logOutState = MutableStateFlow<LogOutState>(LogOutState.Idle)
    val logOutState: StateFlow<LogOutState> = _logOutState

    private val _revokeAccountState = MutableStateFlow<RevokeAccountState>(RevokeAccountState.Idle)
    val revokeAccountState: StateFlow<RevokeAccountState> = _revokeAccountState

    private val _showFailureDialog = MutableStateFlow(false)
    val showFailureDialog: StateFlow<Boolean> = _showFailureDialog

    private val _failureDialogMessage = MutableStateFlow("")
    val failureDialogMessage: StateFlow<String> = _failureDialogMessage

    private val maxRetryCount = 3
    private var retryCount = 0

    fun fetchUserInfo() {
        if (retryCount >= maxRetryCount) return
        _userInfoState.value = UserInfoState.Loading
        viewModelScope.launch {
            if (!networkUtil.isNetworkAvailable()) {
                _userInfoState.value = UserInfoState.Failure(FAILURE_NETWORK_MESSAGE)
                return@launch
            }
            val result = accountManagementRepository.getUserInfo()
            _userInfoState.value = result.fold(
                onSuccess = {
                    retryCount = 0
                    UserInfoState.Success(it)
                },
                onFailure = {
                    retryCount++
                    if(retryCount >= maxRetryCount) {
                        UserInfoState.Failure(FAILURE_TEMPORARY_MESSAGE)
                    } else {
                        val errorMessage = if (it.message?.contains("200") == false) {
                            FAILURE_TEMPORARY_MESSAGE
                        } else {
                            UNKNOWN_ERROR
                        }
                        UserInfoState.Failure(errorMessage)
                    }
                }
            )
        }
    }

    fun changeNickname(requestModifyNicknameDto: RequestModifyNicknameDto) {
        viewModelScope.launch {
            if (!networkUtil.isNetworkAvailable()) {
                _failureDialogMessage.value = FAILURE_NETWORK_MESSAGE
                _showFailureDialog.value = true
            }
            _userNicknameState.value = UserNicknameState.Loading
            val result = accountManagementRepository.modifyNickname(requestModifyNicknameDto)
            _userNicknameState.value = result.fold(
                onSuccess = { UserNicknameState.Success(it) },
                onFailure = {
                    _failureDialogMessage.value = if (it.message?.contains("200") == false) {
                        FAILURE_TEMPORARY_MESSAGE
                    } else {
                        UNKNOWN_ERROR
                    }
                    _showFailureDialog.value = true
                    UserNicknameState.Failure(_failureDialogMessage.value)
                }
            )
        }
    }

    fun validateNickname(nickname: String) {
        if (nickname.isNotEmpty()) {
            val isValid = nickname.matches(Regex(NICKNAME_PATTERN))
            _isValidNickname.value = isValid
            _nicknameMessage.value = if (isValid) DEFAULT_NICKNAME_MESSAGE else FAILURE_NICKNAME_MESSAGE
        } else {
            _isValidNickname.value = true
            _nicknameMessage.value = DEFAULT_NICKNAME_MESSAGE
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
            if (!networkUtil.isNetworkAvailable()) {
                _failureDialogMessage.value = FAILURE_NETWORK_MESSAGE
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
                        FAILURE_TEMPORARY_MESSAGE
                    } else {
                        it.localizedMessage ?: UNKNOWN_ERROR
                    }
                    _showFailureDialog.value = true
                    RevokeAccountState.Failure(_failureDialogMessage.value)
                }
            )
        }
    }

    fun dismissFailureDialog() {
        _showFailureDialog.value = false
        _failureDialogMessage.value = ""
    }

    companion object {
        private const val NICKNAME_PATTERN = "^[a-zA-Z가-힣0-9ㄱ-ㅎㅏ-ㅣ가-힣]{2,10}$"
        private const val DEFAULT_NICKNAME_MESSAGE = "특수문자, 띄어쓰기 없이 작성해주세요"
        private const val FAILURE_NICKNAME_MESSAGE = "사용할 수 없는 닉네임이에요"
    }
}
