package com.sopt.clody.presentation.ui.login

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.sopt.clody.R
import com.sopt.clody.data.datastore.OAuthProvider
import com.sopt.clody.presentation.ui.auth.component.button.GoogleButton
import com.sopt.clody.presentation.ui.auth.component.button.KaKaoButton
import com.sopt.clody.presentation.ui.component.dialog.FailureDialog
import com.sopt.clody.presentation.ui.login.LoginContract.LoginIntent
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

    val googleSignInHelper = remember { GoogleSignInHelper(context) }

    val googleSignInLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult(),
    ) { result ->

        if (result.resultCode == Activity.RESULT_OK) {
            val idToken = googleSignInHelper.extractIdToken(result.data)
            viewModel.postIntent(LoginIntent.LoginOAuth(OAuthProvider.GOOGLE, idToken = idToken))
        }
    }

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
        state = state,
        onKaKaoLoginClick = {
            viewModel.postIntent(LoginIntent.LoginOAuth(OAuthProvider.KAKAO, context = context))
        },
        onGoogleLoginClick = {
            googleSignInHelper.requestSignIn(
                onSuccess = { intentSenderRequest -> googleSignInLauncher.launch(intentSenderRequest) },
                onFailure = {
                    viewModel.postIntent(LoginIntent.ClearError)
                },
            )
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
    state: LoginContract.LoginState,
    onKaKaoLoginClick: () -> Unit,
    onGoogleLoginClick: () -> Unit,
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
            if (state.loginType == OAuthProvider.KAKAO) {
                KaKaoButton(
                    text = stringResource(id = R.string.signup_btn_kakao),
                    onClick = onKaKaoLoginClick,
                    modifier = Modifier
                        .navigationBarsPadding()
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .padding(bottom = 40.dp),
                )
            } else {
                GoogleButton(
                    text = stringResource(id = R.string.signup_btn_google),
                    onClick = onGoogleLoginClick,
                    modifier = Modifier
                        .navigationBarsPadding()
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .padding(bottom = 40.dp),
                )
            }
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = backgroundColor)
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.heightForScreenPercentage(0.36f))
            Image(
                painter = painterResource(id = R.drawable.img_splash_logo),
                contentDescription = "App Logo",
                modifier = Modifier.size(160.dp),
            )
        }
    }
}

@ClodyPreview
@Composable
fun LoginScreenPreview() {
    BasePreview {
        LoginScreen(
            state = LoginContract.LoginState(
                isLoading = false,
                loginType = OAuthProvider.KAKAO,
                errorMessage = null,
            ),
            onKaKaoLoginClick = {},
            onGoogleLoginClick = {},
        )
    }
}
