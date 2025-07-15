package com.sopt.clody.presentation.ui.auth.signup

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.sopt.clody.presentation.ui.auth.signup.page.NickNamePage
import com.sopt.clody.presentation.ui.auth.signup.page.TermsOfServicePage
import com.sopt.clody.presentation.ui.component.dialog.FailureDialog
import com.sopt.clody.presentation.utils.extension.repeatOnStarted

@Composable
fun SignUpRoute(
    viewModel: SignUpViewModel = mavericksViewModel(),
    navigateToHome: () -> Unit,
    navigateToPrevious: () -> Unit,
    navigateToWebView: (String) -> Unit,
) {
    val state by viewModel.collectAsState()
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(viewModel) {
        lifecycleOwner.repeatOnStarted {
            viewModel.sideEffects.collect { effect ->
                when (effect) {
                    is SignUpContract.SignUpSideEffect.NavigateToTimeReminder -> navigateToHome()
                    is SignUpContract.SignUpSideEffect.ShowMessage -> {
                        // TODO: Snackbar나 Dialog로 에러 메시지 처리
                    }

                    is SignUpContract.SignUpSideEffect.NavigateToWebView -> {
                        navigateToWebView(effect.url) // ✅ WebView 이동 처리
                    }
                }
            }
        }
    }

    SignUpScreen(
        state = state,
        onIntent = { viewModel.postIntent(it) },
        context = context,
        navigateToPrevious = navigateToPrevious,
    )

    state.errorMessage?.let {
        FailureDialog(message = it) {
            viewModel.postIntent(SignUpContract.SignUpIntent.ClearError)
        }
    }
}

@Composable
fun SignUpScreen(
    state: SignUpContract.SignUpState,
    onIntent: (SignUpContract.SignUpIntent) -> Unit,
    context: Context,
    navigateToPrevious: () -> Unit,
) {
    when (state.currentStep) {
        SignUpContract.SignUpState.Step.TERMS -> {
            TermsOfServicePage(
                allChecked = state.allChecked,
                serviceChecked = state.serviceChecked,
                privacyChecked = state.privacyChecked,
                serviceUrl = state.serviceUrl,
                privacyUrl = state.privacyUrl,
                onToggleAll = { onIntent(SignUpContract.SignUpIntent.ToggleAllChecked(it)) },
                onToggleService = { onIntent(SignUpContract.SignUpIntent.ToggleServiceChecked(it)) },
                onTogglePrivacy = { onIntent(SignUpContract.SignUpIntent.TogglePrivacyChecked(it)) },
                onAgreeClick = { onIntent(SignUpContract.SignUpIntent.ProceedTerms) },
                navigateToPrevious = navigateToPrevious,
                navigateToWebView = { url -> onIntent(SignUpContract.SignUpIntent.OpenWebView(url)) },
            )
        }

        SignUpContract.SignUpState.Step.NICKNAME -> {
            NickNamePage(
                nickname = state.nickname,
                onNicknameChange = { onIntent(SignUpContract.SignUpIntent.SetNickname(it)) },
                onCompleteClick = { onIntent(SignUpContract.SignUpIntent.CompleteSignUp(context)) },
                onBackClick = { onIntent(SignUpContract.SignUpIntent.BackToTerms) },
                isLoading = state.isLoading,
                isValidNickname = state.isValidNickname,
                nicknameMaxLength = state.nicknameMaxLength,
                nicknameMessage = state.nicknameMessage,
                isFocused = state.isNicknameFocused,
                onFocusChanged = { onIntent(SignUpContract.SignUpIntent.SetNicknameFocus(it)) },
            )
        }
    }
}
