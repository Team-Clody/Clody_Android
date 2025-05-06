package com.sopt.clody.presentation.ui.auth.guide.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.sopt.clody.presentation.ui.auth.guide.GuideRoute
import com.sopt.clody.presentation.utils.navigation.Route

fun NavGraphBuilder.guideScreen(
    navigateToHome: () -> Unit,
) {
    composable<Route.Guide> {
        GuideRoute(
            navigateToHome = navigateToHome,
        )
    }
}

fun NavController.navigateToGuide(
    navOptions: NavOptionsBuilder.() -> Unit = {},
) {
    navigate(Route.Guide, navOptions)
}
