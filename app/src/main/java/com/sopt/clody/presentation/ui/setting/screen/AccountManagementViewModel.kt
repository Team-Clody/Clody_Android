package com.sopt.clody.presentation.ui.setting.screen

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jakewharton.processphoenix.ProcessPhoenix
import com.sopt.clody.data.datastore.TokenDataStore
import com.sopt.clody.data.repository.AccountManagementRepository
import com.sopt.clody.presentation.ui.main.MainActivity
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
    @ApplicationContext private val context: Context
) : ViewModel() {
    private val _userInfoState = MutableStateFlow<UserInfoState>(UserInfoState.Idle)
    val userInfoState: StateFlow<UserInfoState> = _userInfoState

    private val _revokeState = MutableStateFlow<RevokeState>(RevokeState.Idle)
    val revokeState: StateFlow<RevokeState> = _revokeState

    fun fetchUserInfo() {
        _userInfoState.value = UserInfoState.Loading
        viewModelScope.launch {
            val result = accountManagementRepository.getUserInfo()
            _userInfoState.value = result.fold(
                onSuccess = { UserInfoState.Success(it) },
                onFailure = { UserInfoState.Failure(it.message ?: "Unknown error") }
            )
        }
    }

    fun revoke() {
        viewModelScope.launch {
            val result = accountManagementRepository.revoke()
            _revokeState.value = result.fold(
                onSuccess = {
                    tokenDataStore.clearInfo()
                    Handler(Looper.getMainLooper()).post {
                        ProcessPhoenix.triggerRebirth(context, Intent(context, MainActivity::class.java))
                    }
                    RevokeState.Success(it)
                },
                onFailure = { RevokeState.Failure(it.message ?: "Unknown error") }
            )
        }
    }
}
