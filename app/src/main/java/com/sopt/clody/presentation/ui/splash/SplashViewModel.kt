package com.sopt.clody.presentation.ui.splash

import androidx.lifecycle.ViewModel
import com.sopt.clody.domain.repository.TokenRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val tokenRepository: TokenRepository
) : ViewModel() {

    private val _isUserLoggedIn = MutableStateFlow<Boolean?>(null)
    val isUserLoggedIn: StateFlow<Boolean?> = _isUserLoggedIn

    init {
        attemptAutoLogin()
    }

    private fun attemptAutoLogin() {
        val accessToken = tokenRepository.getAccessToken()
        val refreshToken = tokenRepository.getRefreshToken()
        _isUserLoggedIn.value = accessToken.isNotBlank() && refreshToken.isNotBlank()
    }
}
