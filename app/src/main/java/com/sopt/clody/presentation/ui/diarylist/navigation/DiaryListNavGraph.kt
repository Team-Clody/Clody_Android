package com.sopt.clody.presentation.ui.diarylist.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.sopt.clody.presentation.ui.diarylist.screen.DiaryListRoute
import java.time.LocalDate

fun NavGraphBuilder.diaryListNavGraph(
    diaryListNavigator: DiaryListNavigator,
) {
    composable(
        route = "diary_list/{selectedYearFromHome}/{selectedMonthFromHome}",
        arguments = listOf (
            navArgument("selectedYearFromHome") { type = NavType.IntType },
            navArgument("selectedMonthFromHome") { type = NavType.IntType }
        )
    ) { backStackEntry ->
        val currentDate = LocalDate.now()
        val selectedYearFromHome = backStackEntry.arguments?.getInt("selectedYearFromHome") ?: currentDate.year
        val selectedMonthFromHome = backStackEntry.arguments?.getInt("selectedMonthFromHome") ?: currentDate.monthValue
        DiaryListRoute(
            navigator = diaryListNavigator,
            selectedYearFromHome = selectedYearFromHome,
            selectedMonthFromHome = selectedMonthFromHome
        )
    }
}
