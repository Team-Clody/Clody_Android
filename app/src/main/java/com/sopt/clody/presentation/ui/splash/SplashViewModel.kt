package com.sopt.clody.presentation.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sopt.clody.BuildConfig
import com.sopt.clody.domain.appupdate.AppUpdateChecker
import com.sopt.clody.domain.model.AppUpdateState
import com.sopt.clody.domain.repository.TokenRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val tokenRepository: TokenRepository,
    private val appUpdateChecker: AppUpdateChecker,
) : ViewModel() {

    private val _isUserLoggedIn = MutableStateFlow<Boolean?>(null)
    val isUserLoggedIn: StateFlow<Boolean?> = _isUserLoggedIn

    private val _updateState = MutableStateFlow<AppUpdateState?>(null)
    val updateState: StateFlow<AppUpdateState?> = _updateState

    init {
        attemptAutoLogin()
        checkVersion()
    }

    private fun attemptAutoLogin() {
        val accessToken = tokenRepository.getAccessToken()
        val refreshToken = tokenRepository.getRefreshToken()
        _isUserLoggedIn.value = accessToken.isNotBlank() && refreshToken.isNotBlank()
    }

    private fun checkVersion() {
        viewModelScope.launch {
            val state = appUpdateChecker.getAppUpdateState(BuildConfig.VERSION_NAME)
            _updateState.value = state
        }
    }

    fun clearUpdateState() {
        _updateState.value = AppUpdateState.Latest
    }
}
