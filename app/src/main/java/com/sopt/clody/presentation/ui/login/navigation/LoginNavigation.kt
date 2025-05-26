package com.sopt.clody.presentation.ui.login.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.sopt.clody.presentation.ui.login.LoginRoute
import com.sopt.clody.presentation.utils.navigation.Route

fun NavGraphBuilder.loginScreen(
    navigateToSignUp: () -> Unit,
    navigateToHome: () -> Unit,
) {
    composable<Route.Login> {
        LoginRoute(
            navigateToSignUp = navigateToSignUp,
            navigateToHome = navigateToHome,
        )
    }
}

fun NavController.navigateToLogin(
    navOptions: NavOptionsBuilder.() -> Unit = {},
) {
    navigate(Route.Login, navOptions)
}
