package com.sopt.clody.presentation.ui.auth.signup.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.sopt.clody.presentation.ui.auth.signup.SignUpRoute
import com.sopt.clody.presentation.utils.navigation.Route

fun NavGraphBuilder.signUpScreen(
    navigateToHome: () -> Unit,
    navigateToPrevious: () -> Unit,
) {
    composable<Route.SignUp> {
        SignUpRoute(
            navigateToHome = navigateToHome,
            navigateToPrevious = navigateToPrevious,
        )
    }
}

fun NavController.navigateToSignUp(
    navOptions: NavOptionsBuilder.() -> Unit = {},
) {
    navigate(Route.SignUp, navOptions)
}
