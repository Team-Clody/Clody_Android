package com.sopt.clody.presentation.ui.home.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.sopt.clody.presentation.ui.home.screen.HomeRoute
import java.time.LocalDate

fun NavGraphBuilder.homeNavGraph(
    navigator: HomeNavigator,
) {
    composable(
        route = "home/{selectedYear}/{selectedMonth}",
        arguments = listOf(
            navArgument("selectedYear") { type = NavType.IntType },
            navArgument("selectedMonth") { type = NavType.IntType }
        )
    ) { backStackEntry ->
        val currentDate = LocalDate.now()
        val selectedYear = backStackEntry.arguments?.getInt("selectedYear") ?: currentDate.year
        val selectedMonth = backStackEntry.arguments?.getInt("selectedMonth") ?: currentDate.monthValue
        HomeRoute(
            navigator = navigator,
            selectedYear = selectedYear,
            selectedMonth = selectedMonth
        )
    }
}
