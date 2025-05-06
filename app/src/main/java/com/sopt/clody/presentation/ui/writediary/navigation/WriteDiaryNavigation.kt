package com.sopt.clody.presentation.ui.writediary.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.sopt.clody.presentation.ui.writediary.screen.WriteDiaryRoute
import com.sopt.clody.presentation.utils.navigation.Route

fun NavGraphBuilder.writeDiaryScreen(
    navigateToReplyLoading: (year: Int, month: Int, day: Int) -> Unit,
    navigateToHome: (year: Int, month: Int) -> Unit,
    navigateToPrevious: () -> Unit,
) {
    composable<Route.WriteDiary> { backStackEntry ->
        backStackEntry.toRoute<Route.WriteDiary>().apply {
            WriteDiaryRoute(
                year = year,
                month = month,
                date = date,
                navigateToReplyLoading = navigateToReplyLoading,
                navigateToHome = navigateToHome,
                navigateToPrevious = navigateToPrevious,
            )
        }
    }
}

fun NavController.navigateToWriteDiary(
    year: Int,
    month: Int,
    day: Int,
    navOptions: NavOptionsBuilder.() -> Unit = {},
) {
    navigate(Route.WriteDiary(year, month, day), navOptions)
}
