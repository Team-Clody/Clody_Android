package com.sopt.clody.presentation.ui.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sopt.clody.R
import com.sopt.clody.presentation.ui.auth.navigation.AuthNavigator
import com.sopt.clody.ui.theme.ClodyTheme
import kotlinx.coroutines.delay
import java.time.LocalDate

@Composable
fun SplashRoute(
    navigator: AuthNavigator,
    viewModel: SplashViewModel = hiltViewModel(),
) {
    val isUserLoggedIn by viewModel.isUserLoggedIn.collectAsStateWithLifecycle()

    LaunchedEffect(isUserLoggedIn) {
        if (isUserLoggedIn != null) {
            delay(1000)
            navigator.navController.navigate(
                if (isUserLoggedIn == true) {
                    "home/${LocalDate.now().year}/${LocalDate.now().monthValue}"
                } else {
                    "register_graph"
                },
            ) {
                popUpTo("splash") { inclusive = true }
            }
        }
    }

    SplashScreen()
}

@Composable
fun SplashScreen() {
    val backgroundColor = ClodyTheme.colors.mainYellow
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(id = R.drawable.img_splash_logo),
            contentDescription = "App Logo",
            modifier = Modifier.size(160.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    SplashScreen()
}
