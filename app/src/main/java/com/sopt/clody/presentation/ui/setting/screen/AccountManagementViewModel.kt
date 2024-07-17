package com.sopt.clody.presentation.ui.setting.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
}
