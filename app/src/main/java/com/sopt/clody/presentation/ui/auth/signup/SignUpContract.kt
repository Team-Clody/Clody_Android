package com.sopt.clody.presentation.ui.auth.signup

import android.content.Context
import com.airbnb.mvrx.MavericksState

class SignUpContract {
    data class SignUpState(
        val currentStep: Step = Step.TERMS,
        val nickname: String = "",
        val isNicknameFocused: Boolean = false,
        val isValidNickname: Boolean = true,
        val nicknameMaxLength: Int = 15,
        val nicknameMessage: String = DEFAULT_NICKNAME_MESSAGE,
        val isLoading: Boolean = false,
        val errorMessage: String? = null,
        val serviceChecked: Boolean = false,
        val serviceUrl: String = "",
        val privacyChecked: Boolean = false,
        val privacyUrl: String = "",
    ) : MavericksState {
        val allChecked: Boolean
            get() = serviceChecked && privacyChecked

        enum class Step {
            TERMS, NICKNAME
        }
    }

    sealed class SignUpIntent {
        data class SetNickname(val value: String) : SignUpIntent()
        data class SetNicknameFocus(val isFocused: Boolean) : SignUpIntent()
        data object SetNicknameMaxLength : SignUpIntent()
        data object ProceedTerms : SignUpIntent()
        data class CompleteSignUp(val context: Context) : SignUpIntent()
        data object ClearError : SignUpIntent()

        data class ToggleAllChecked(val checked: Boolean) : SignUpIntent()
        data class ToggleServiceChecked(val checked: Boolean) : SignUpIntent()
        data class TogglePrivacyChecked(val checked: Boolean) : SignUpIntent()
        data object SetWebViewUrl : SignUpIntent()
        data class OpenWebView(val url: String) : SignUpIntent()
        data object BackToTerms : SignUpIntent()
    }

    sealed interface SignUpSideEffect {
        data object NavigateToTimeReminder : SignUpSideEffect
        data class NavigateToWebView(val url: String) : SignUpSideEffect
        data class ShowMessage(val message: String) : SignUpSideEffect
    }

    companion object {
        const val DEFAULT_NICKNAME_MESSAGE = "특수문자, 띄어쓰기 없이 작성해주세요"
    }
}
