package com.sopt.clody.presentation.ui.home.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.sopt.clody.presentation.ui.home.screen.HomeRoute
import com.sopt.clody.presentation.ui.replyloading.navigation.ReplyLoadingNavigator
import com.sopt.clody.presentation.ui.replyloading.screen.ReplyLoadingRoute
import java.time.LocalDate

fun NavGraphBuilder.homeNavGraph(
    navigator: HomeNavigator,
    replyLoadingNavigator: ReplyLoadingNavigator
) {
    composable(
        route = "home/{selectedYear}/{selectedMonth}",
        arguments = listOf(
            navArgument("selectedYear") { type = NavType.IntType },
            navArgument("selectedMonth") { type = NavType.IntType }
        )
    ) { backStackEntry ->
        val selectedYear = backStackEntry.arguments?.getInt("selectedYear") ?: LocalDate.now().year
        val selectedMonth = backStackEntry.arguments?.getInt("selectedMonth") ?: LocalDate.now().monthValue
        HomeRoute(
            navigator = navigator,
            selectedYear = selectedYear,
            selectedMonth = selectedMonth
        )
    }

    composable(
        route = "reply_loading/{year}/{month}/{day}?from={from}",
        arguments = listOf(
            navArgument("year") { type = NavType.IntType },
            navArgument("month") { type = NavType.IntType },
            navArgument("day") { type = NavType.IntType },
            navArgument("from") { defaultValue = "home" } // 기본값 설정
        )
    ) { backStackEntry ->
        val year = backStackEntry.arguments?.getInt("year") ?: 0
        val month = backStackEntry.arguments?.getInt("month") ?: 0
        val day = backStackEntry.arguments?.getInt("day") ?: 0
        val from = backStackEntry.arguments?.getString("from") ?: "home"
        ReplyLoadingRoute(
            navigator = replyLoadingNavigator,
            year = year,
            month = month,
            day = day,
            from = from
        )
    }
}
