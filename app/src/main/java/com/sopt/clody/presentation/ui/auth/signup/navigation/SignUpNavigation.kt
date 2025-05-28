package com.sopt.clody.presentation.ui.auth.signup.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.sopt.clody.presentation.ui.auth.signup.NicknameRoute
import com.sopt.clody.presentation.ui.auth.signup.TermsOfServiceRoute
import com.sopt.clody.presentation.utils.navigation.Route

fun NavGraphBuilder.termsOfServiceScreen(
    navigateToNickname: () -> Unit,
    navigateToLogin: () -> Unit,
) {
    composable<Route.TermsOfService> {
        TermsOfServiceRoute(
            navigateToNickname = navigateToNickname,
            navigateToLogin = navigateToLogin,
        )
    }
}

fun NavGraphBuilder.nicknameScreen(
    navigateToReminder: () -> Unit,
    navigateToPrevious: () -> Unit,
) {
    composable<Route.Nickname> {
        NicknameRoute(
            navigateToReminder = navigateToReminder,
            navigateToPrevious = navigateToPrevious,
        )
    }
}

fun NavController.navigateToNickname(
    navOptions: NavOptionsBuilder.() -> Unit = {},
) {
    navigate(Route.Nickname, navOptions)
}

fun NavController.navigateToTermsOfService(
    navOptions: NavOptionsBuilder.() -> Unit = {},
) {
    navigate(Route.TermsOfService, navOptions)
}
