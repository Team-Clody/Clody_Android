package com.sopt.clody.presentation.ui.setting.screen

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sopt.clody.data.datastore.TokenDataStore
import com.sopt.clody.data.remote.dto.RequestModifyNicknameDto
import com.sopt.clody.data.repository.AccountManagementRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountManagementViewModel @Inject constructor(
    private val accountManagementRepository: AccountManagementRepository, private val tokenDataStore: TokenDataStore, @ApplicationContext private val context: Context
) : ViewModel() {
    private val _userInfoState = MutableStateFlow<UserInfoState>(UserInfoState.Idle)
    val userInfoState: StateFlow<UserInfoState> = _userInfoState

    private val _userNicknameState = MutableStateFlow<UserNicknameState>(UserNicknameState.Idle)
    val userNicknameState: StateFlow<UserNicknameState> = _userNicknameState

    private val _isValidNickname = MutableStateFlow(true)
    val isValidNickname: StateFlow<Boolean> = _isValidNickname

    private val _nicknameMessage = MutableStateFlow(DEFAULT_NICKNAME_MESSAGE)
    val nicknameMessage: StateFlow<String> = _nicknameMessage

    private val _revokeAccountState = MutableStateFlow<RevokeAccountState>(RevokeAccountState.Idle)
    val revokeAccountState: StateFlow<RevokeAccountState> = _revokeAccountState

    private val _logOutState = MutableStateFlow<LogOutState>(LogOutState.Idle)
    val logOutState: StateFlow<LogOutState> = _logOutState

    fun fetchUserInfo() {
        _userInfoState.value = UserInfoState.Loading
        viewModelScope.launch {
            val result = accountManagementRepository.getUserInfo()
            _userInfoState.value = result.fold(onSuccess = { UserInfoState.Success(it) }, onFailure = { UserInfoState.Failure(it.message ?: "Unknown error") })
        }
    }

    fun changeNickname(requestModifyNicknameDto: RequestModifyNicknameDto) {
        _userNicknameState.value = UserNicknameState.Loading
        viewModelScope.launch {
            val result = accountManagementRepository.modifyNickname(requestModifyNicknameDto)
            _userNicknameState.value = result.fold(onSuccess = { UserNicknameState.Success(it) }, onFailure = { UserNicknameState.Failure(it.message ?: "Unknown error") })
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
        viewModelScope.launch {
            val result = accountManagementRepository.revokeAccount()
            result.fold(
                onSuccess = {
                    tokenDataStore.clearInfo()
                    _revokeAccountState.value = RevokeAccountState.Success(it)
                },
                onFailure = {
                    _revokeAccountState.value = RevokeAccountState.Failure(it.localizedMessage ?: "에러 발생")
                }
            )
        }
    }

    companion object {
        private const val NICKNAME_PATTERN = "^[a-zA-Z가-힣0-9ㄱ-ㅎㅏ-ㅣ가-힣]{2,10}$"
        private const val DEFAULT_NICKNAME_MESSAGE = "특수문자, 띄어쓰기 없이 작성해주세요"
        private const val FAILURE_NICKNAME_MESSAGE = "사용할 수 없는 닉네임이에요"
    }
}
