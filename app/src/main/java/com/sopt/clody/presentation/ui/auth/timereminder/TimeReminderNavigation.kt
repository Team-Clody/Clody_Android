package com.sopt.clody.presentation.ui.auth.timereminder

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.sopt.clody.presentation.utils.navigation.Route

fun NavGraphBuilder.timeReminderScreen(
    navigateToGuide: () -> Unit,
) {
    composable<Route.TimeReminder> {
        TimeReminderRoute(
            navigateToGuide = navigateToGuide,
        )
    }
}

fun NavHostController.navigateToTimeReminder(
    navOptions: NavOptionsBuilder.() -> Unit = {},
) {
    navigate(Route.TimeReminder, navOptions)
}
