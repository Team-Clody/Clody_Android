package com.sopt.clody.presentation.ui.auth.login

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.sopt.clody.R
import com.sopt.clody.presentation.ui.auth.component.button.KaKaoButton
import com.sopt.clody.presentation.ui.auth.signup.SignUpViewModel
import com.sopt.clody.presentation.ui.component.LoadingScreen
import com.sopt.clody.presentation.utils.base.UiState
import com.sopt.clody.presentation.utils.extension.heightForScreenPercentage
import com.sopt.clody.ui.theme.ClodyTheme

@Composable
fun LoginRoute(
    navigateToTerms: () -> Unit,
    navigateToHome: () -> Unit,
) {
    val viewModel: SignUpViewModel = hiltViewModel()
    val signInState by viewModel.signInState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(signInState.uiState) {
        when (signInState.uiState) {
            is UiState.Success -> navigateToHome()
            is UiState.Failure -> navigateToTerms()
            else -> Unit
        }
    }

    LoginScreen(
        isLoading = signInState.uiState is UiState.Loading,
        onSignInClick = { viewModel.signInWithKakao(context) },
    )
}

@Composable
fun LoginScreen(
    isLoading: Boolean,
    onSignInClick: () -> Unit,
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
                onClick = onSignInClick,
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

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(
        isLoading = false,
        onSignInClick = {},
    )
}
