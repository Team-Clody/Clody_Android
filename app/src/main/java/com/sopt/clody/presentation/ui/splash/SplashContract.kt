package com.sopt.clody.presentation.ui.splash

import android.content.Intent
import com.airbnb.mvrx.MavericksState
import com.sopt.clody.domain.model.AppUpdateState

class SplashContract {

    data class SplashState(
        val isUserLoggedIn: Boolean? = null,
        val updateState: AppUpdateState? = null,
    ) : MavericksState

    sealed class SplashIntent {
        data class InitSplash(val startIntent: Intent) : SplashIntent()
        data object ClearUpdateState : SplashIntent()
    }

    sealed interface SplashSideEffect {
        data object NavigateToLogin : SplashSideEffect
        data object NavigateToHome : SplashSideEffect
    }
}
