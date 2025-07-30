package com.sopt.clody.presentation.ui.splash

import android.content.Intent
import com.airbnb.mvrx.MavericksState
import com.sopt.clody.domain.model.AppUpdateState

class SplashContract {
    data class SplashState(
        val updateState: AppUpdateState? = null,
        val showInspectionDialog: Boolean = false,
        val inspectionTimeText: String? = null,
    ) : MavericksState

    sealed class SplashIntent {
        data class InitSplash(val startIntent: Intent) : SplashIntent()
        data class HandleHardUpdate(val isConfirm: Boolean) : SplashIntent()
        data object HandleSoftUpdateConfirm : SplashIntent()
        data object ClearUpdateState : SplashIntent()
        data object DismissInspectionDialog : SplashIntent()
    }

    sealed interface SplashSideEffect {
        data object NavigateToLogin : SplashSideEffect
        data object NavigateToHome : SplashSideEffect
        data object NavigateToMarket : SplashSideEffect
        data object NavigateToMarketAndFinish : SplashSideEffect
        data object FinishApp : SplashSideEffect
    }
}
