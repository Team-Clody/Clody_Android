package com.sopt.clody.presentation.ui.auth.signup

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.sopt.clody.R
import com.sopt.clody.presentation.ui.auth.component.button.KaKaoButton
import com.sopt.clody.presentation.ui.auth.navigation.AuthNavigator
import com.sopt.clody.presentation.utils.base.UiState
import com.sopt.clody.presentation.utils.extension.showLongToast
import com.sopt.clody.ui.theme.ClodyTheme
import kotlinx.coroutines.launch

@Composable
fun SignUpRoute(
    authNavigator: AuthNavigator
) {
    val viewModel: SignUpViewModel = hiltViewModel()
    val signInState by viewModel.signInState.collectAsState()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(signInState) {
        when (val result = signInState.uiState) {
            is UiState.Success -> {
                authNavigator.navigateHome()
            }
            is UiState.Failure -> {
                if (result.msg.contains("404") || result.msg.contains("User not found")) {
                    authNavigator.navigateTermsOfService()
                } else {
                    coroutineScope.launch {
                        showLongToast(context, result.msg)
                    }
                }
            }
            else -> {}
        }
    }

    SignUpScreen(
        signInState = signInState,
        onSignInClick = { viewModel.signInWithKakao(context) },
    )
}
@Composable
fun SignUpScreen(
    signInState: SignInState,
    onSignInClick: () -> Unit
) {
    val systemUiController = rememberSystemUiController()
    val backgroundColor = ClodyTheme.colors.white

    LaunchedEffect(Unit) {
        systemUiController.setStatusBarColor(
            color = backgroundColor,
            darkIcons = true
        )
    }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(color = ClodyTheme.colors.white)
    ) {
        val (imgSignUpLogo, txtSignUpTitle, btnLoginKakao, imgSignUpLogoTitle, txtErrorMessage, progressBar) = createRefs()
        val guideline = createGuidelineFromTop(0.34f)

        Image(
            painter = painterResource(id = R.drawable.ic_signup_logo),
            contentDescription = null,
            modifier = Modifier
                .constrainAs(imgSignUpLogo) {
                    top.linkTo(guideline)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            contentScale = ContentScale.Crop
        )

        Image(
            painter = painterResource(id = R.drawable.ic__signup_title),
            contentDescription = null,
            modifier = Modifier.constrainAs(txtSignUpTitle) {
                top.linkTo(imgSignUpLogo.bottom, margin = 20.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
        )

        Image(
            painter = painterResource(id = R.drawable.ic_signup_logotitle),
            contentDescription = null,
            modifier = Modifier
                .constrainAs(imgSignUpLogoTitle) {
                    top.linkTo(txtSignUpTitle.bottom, margin = 10.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            contentScale = ContentScale.Crop
        )

        if (signInState.uiState is UiState.Loading) {
            CircularProgressIndicator(
                color = ClodyTheme.colors.mainYellow,
                modifier = Modifier.constrainAs(progressBar) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            )

        } else {
            KaKaoButton(
                text = stringResource(id = R.string.signup_btn_kakao),
                onClick = onSignInClick,
                modifier = Modifier.constrainAs(btnLoginKakao) {
                    bottom.linkTo(parent.bottom, margin = 40.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    SignUpScreen(
        signInState = SignInState(),
        onSignInClick = {}
    )
}
