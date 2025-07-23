package com.sopt.clody.presentation.ui.login

import android.content.Context
import com.airbnb.mvrx.MavericksState
import com.sopt.clody.data.datastore.OAuthProvider

class LoginContract {

    data class LoginState(
        val isLoading: Boolean = false,
        val errorMessage: String? = null,
    ) : MavericksState

    sealed class LoginIntent {
        data class LoginOAuth(val provider: OAuthProvider, val context: Context? = null, val idToken: String? = null) : LoginIntent()
        data object ClearError : LoginIntent()
    }

    sealed interface LoginSideEffect {
        data object NavigateToHome : LoginSideEffect
        data object NavigateToSignUp : LoginSideEffect
        data class ShowError(val message: String) : LoginSideEffect
    }
}
