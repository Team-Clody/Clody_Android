package com.sopt.clody.presentation.ui.writediary.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.sopt.clody.presentation.ui.writediary.screen.WriteDiaryRoute
import java.time.LocalDate


fun NavGraphBuilder.writeDiaryNavGraph(
    writeDiaryNavigator: WriteDiaryNavigator,
) {
    composable(
        "write_diary/{year}/{month}/{day}",
        arguments = listOf(
            navArgument("year") { type = NavType.IntType },
            navArgument("month") { type = NavType.IntType },
            navArgument("day") { type = NavType.IntType }
        )
    ) { backStackEntry ->
        val currentDate = LocalDate.now()
        val year = backStackEntry.arguments?.getInt("year") ?: currentDate.year
        val month = backStackEntry.arguments?.getInt("month") ?: currentDate.monthValue
        val day = backStackEntry.arguments?.getInt("day") ?: currentDate.dayOfMonth
        WriteDiaryRoute(writeDiaryNavigator, year, month, day)
    }
}
