package com.sopt.clody.presentation.ui.setting.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sopt.clody.data.remote.dto.RequestModifyNicknameDto
import com.sopt.clody.data.remote.dto.ResponseModifyNicknameDto
import com.sopt.clody.data.repository.AccountManagementRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountManagementViewModel @Inject constructor(
    private val accountManagementRepository: AccountManagementRepository
) : ViewModel() {
    private val _userInfoState = MutableStateFlow<UserInfoState>(UserInfoState.Idle)
    val userInfoState: StateFlow<UserInfoState> = _userInfoState

    private val _userNicknameState = MutableStateFlow<UserNicknameState>(UserNicknameState.Idle)
    val userNicknameState: StateFlow<UserNicknameState> = _userNicknameState

    fun fetchUserInfo() {
        _userInfoState.value = UserInfoState.Loading
        viewModelScope.launch {
            val result = accountManagementRepository.getUserInfo()
            _userInfoState.value = result.fold(
                onSuccess = { UserInfoState.Success(it) },
                onFailure = { UserInfoState.Failure(it.message ?: "Unknown error")}
            )
        }
    }

    fun changeNickname(requestModifyNicknameDto: RequestModifyNicknameDto) {
        _userNicknameState.value = UserNicknameState.Loading
        viewModelScope.launch {
            val result = accountManagementRepository.modifyNickname(requestModifyNicknameDto)
            _userNicknameState.value = result.fold(
                onSuccess = { UserNicknameState.Success(it) },
                onFailure = { UserNicknameState.Failure(it.message ?: "Unknown error")}
            )
        }
    }
}
