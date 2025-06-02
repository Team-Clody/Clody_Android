package com.sopt.clody.presentation.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.sopt.clody.R
import com.sopt.clody.presentation.ui.auth.component.button.KaKaoButton
import com.sopt.clody.presentation.ui.component.LoadingScreen
import com.sopt.clody.presentation.ui.component.dialog.FailureDialog
import com.sopt.clody.presentation.utils.base.BasePreview
import com.sopt.clody.presentation.utils.base.ClodyPreview
import com.sopt.clody.presentation.utils.extension.heightForScreenPercentage
import com.sopt.clody.presentation.utils.extension.repeatOnStarted
import com.sopt.clody.ui.theme.ClodyTheme

@Composable
fun LoginRoute(
    navigateToSignUp: () -> Unit,
    navigateToHome: () -> Unit,
    viewModel: LoginViewModel = mavericksViewModel(),
) {
    val state by viewModel.collectAsState()
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(viewModel) {
        lifecycleOwner.repeatOnStarted {
            viewModel.sideEffects.collect { sideEffect ->
                when (sideEffect) {
                    is LoginContract.LoginSideEffect.NavigateToHome -> navigateToHome()
                    is LoginContract.LoginSideEffect.NavigateToSignUp -> navigateToSignUp()
                    is LoginContract.LoginSideEffect.ShowError -> {
                        // 삐용삐용 여기 뭘로 처리할까?
                    }
                }
            }
        }
    }

    LoginScreen(
        isLoading = state.isLoading,
        onLoginClick = {
            viewModel.postIntent(LoginContract.LoginIntent.LoginWithKakao(context))
        },
    )

    // 에러 메시지 추가로 다이얼로그로 처리하고 싶다면?
    state.errorMessage?.let { message ->
        FailureDialog(message = message) {
            viewModel.postIntent(LoginContract.LoginIntent.ClearError)
        }
    }
}

@Composable
fun LoginScreen(
    isLoading: Boolean,
    onLoginClick: () -> Unit,
) {
    val systemUiController = rememberSystemUiController()
    val backgroundColor = ClodyTheme.colors.white

    LaunchedEffect(Unit) {
        systemUiController.setStatusBarColor(
            color = backgroundColor,
            darkIcons = true,
        )
    }

    Scaffold(
        bottomBar = {
            KaKaoButton(
                text = stringResource(id = R.string.signup_btn_kakao),
                onClick = onLoginClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(bottom = 40.dp),
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = backgroundColor)
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.heightForScreenPercentage(0.38f))
            Image(
                painter = painterResource(id = R.drawable.ic_signup_logo),
                contentDescription = null,
                contentScale = ContentScale.Crop,
            )
            Spacer(modifier = Modifier.heightForScreenPercentage(0.02f))
            Image(
                painter = painterResource(id = R.drawable.ic__signup_title),
                contentDescription = null,
            )
            Spacer(modifier = Modifier.heightForScreenPercentage(0.01f))
            Image(
                painter = painterResource(id = R.drawable.ic_signup_logotitle),
                contentDescription = null,
                contentScale = ContentScale.Crop,
            )
        }
    }

    if (isLoading) {
        LoadingScreen()
    }
}

@ClodyPreview
@Composable
fun LoginScreenPreview() {
    BasePreview {
        LoginScreen(
            isLoading = false,
            onLoginClick = {},
        )
    }
}
