package com.sopt.clody.presentation.ui.home.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.sopt.clody.presentation.ui.home.screen.HomeRoute

fun NavGraphBuilder.homeNavGraph(
    navigator: HomeNavigator
) {
    composable("home") {
        HomeRoute(navigator)
    }
}
