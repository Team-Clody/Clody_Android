package com.sopt.clody.presentation.ui.auth.login.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.sopt.clody.presentation.ui.auth.login.LoginRoute
import com.sopt.clody.presentation.utils.navigation.Route

fun NavGraphBuilder.loginScreen(
    navigateToTermsOfService: () -> Unit,
    navigateToHome: () -> Unit,
) {
    composable<Route.Login> {
        LoginRoute(
            navigateToTermsOfService = navigateToTermsOfService,
            navigateToHome = navigateToHome,
        )
    }
}

fun NavController.navigateToLogin(
    navOptions: NavOptionsBuilder.() -> Unit = {},
) {
    navigate(Route.Login, navOptions)
}
