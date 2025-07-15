package com.sopt.clody.presentation.ui.login

import android.content.Context
import com.airbnb.mvrx.MavericksState

class LoginContract {

    data class LoginState(
        val loginType: LoginType = LoginType.GOOGLE,
        val isLoading: Boolean = false,
        val errorMessage: String? = null,
    ) : MavericksState

    sealed class LoginIntent {
        data object SetLoginType : LoginIntent()
        data class LoginWithKakao(val context: Context) : LoginIntent()
        data class LoginWithGoogle(val context: Context) : LoginIntent()
        data object ClearError : LoginIntent()
    }

    sealed interface LoginSideEffect {
        data object NavigateToHome : LoginSideEffect
        data object NavigateToSignUp : LoginSideEffect
        data class ShowError(val message: String) : LoginSideEffect
    }
}
