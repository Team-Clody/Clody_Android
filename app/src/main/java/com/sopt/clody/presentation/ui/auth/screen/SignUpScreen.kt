package com.sopt.clody.presentation.ui.auth.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.sopt.clody.R
import com.sopt.clody.presentation.ui.auth.component.button.KaKaoButton
import com.sopt.clody.presentation.ui.auth.navigation.AuthNavigator
import com.sopt.clody.ui.theme.ClodyTheme

@Composable
fun SignUpRoute(
    navigator: AuthNavigator
) {
    SignUpScreen(
        onTermsOfServiceClick = { navigator.navigateTermsOfService() }
    )
}

@Composable
fun SignUpScreen(
    onTermsOfServiceClick: () -> Unit
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(color = ClodyTheme.colors.white)
    ) {
        val (imgSignUpLogo, txtSignUpTitle, btnLoginKakao, imgSignUpLogoTitle) = createRefs()
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

        KaKaoButton(
            text = stringResource(id = R.string.signup_btn_kakao),
            onClick = { onTermsOfServiceClick() },
            modifier = Modifier.constrainAs(btnLoginKakao) {
                bottom.linkTo(parent.bottom, margin = 40.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    SignUpScreen(onTermsOfServiceClick = {})
}

