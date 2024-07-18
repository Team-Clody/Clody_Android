package com.sopt.clody.presentation.ui.home.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.sopt.clody.presentation.ui.home.screen.HomeRoute
import com.sopt.clody.presentation.ui.replyloading.navigation.ReplyLoadingNavigator
import com.sopt.clody.presentation.ui.replyloading.screen.ReplyLoadingRoute

fun NavGraphBuilder.homeNavGraph(
    navigator: HomeNavigator,
    replyLoadingNavigator: ReplyLoadingNavigator
) {
    composable("home") {
        HomeRoute(navigator)
    }
    composable("reply_loading/{year}/{month}/{day}") { backStackEntry ->
        val year = backStackEntry.arguments?.getString("year")?.toIntOrNull() ?: 0
        val month = backStackEntry.arguments?.getString("month")?.toIntOrNull() ?: 0
        val day = backStackEntry.arguments?.getString("day")?.toIntOrNull() ?: 0
        ReplyLoadingRoute(replyLoadingNavigator, year, month, day)
    }
}
