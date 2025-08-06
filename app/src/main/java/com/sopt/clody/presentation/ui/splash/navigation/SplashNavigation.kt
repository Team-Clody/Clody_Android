package com.sopt.clody.presentation.ui.splash.navigation

import android.content.Intent
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.sopt.clody.presentation.ui.splash.SplashRoute
import com.sopt.clody.presentation.utils.navigation.Route

fun NavGraphBuilder.splashScreen(
    startIntent: Intent,
    navigateToLogin: () -> Unit,
    navigateToHome: () -> Unit,
) {
    composable<Route.Splash> {
        SplashRoute(
            startIntent = startIntent,
            onLoginRequired = navigateToLogin,
            onAlreadyLoggedIn = navigateToHome,
        )
    }
}
