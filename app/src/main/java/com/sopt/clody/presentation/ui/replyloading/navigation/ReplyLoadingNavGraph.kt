package com.sopt.clody.presentation.ui.replyloading.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.sopt.clody.presentation.ui.replydiary.ReplyDiaryRoute
import com.sopt.clody.presentation.ui.replydiary.navigation.ReplyDiaryNavigator
import com.sopt.clody.presentation.ui.replyloading.screen.ReplyLoadingRoute
import java.time.LocalDate

fun NavGraphBuilder.replyLoadingNavGraph(
    replyLoadingNavigator: ReplyLoadingNavigator,
    replyDiaryNavigator: ReplyDiaryNavigator
) {
    val currentDate = LocalDate.now()
    composable(
        "reply_loading/{year}/{month}/{day}?from={from}",
        arguments = listOf(
            navArgument("year") { type = NavType.IntType },
            navArgument("month") { type = NavType.IntType },
            navArgument("day") { type = NavType.IntType },
            navArgument("from") { defaultValue = "home" }
        )
    ) { backStackEntry ->
        val year = backStackEntry.arguments?.getInt("year") ?: currentDate.year
        val month = backStackEntry.arguments?.getInt("month") ?: currentDate.monthValue
        val day = backStackEntry.arguments?.getInt("day") ?: currentDate.dayOfMonth
        val from = backStackEntry.arguments?.getString("from") ?: "home"
        ReplyLoadingRoute(replyLoadingNavigator, year, month, day, from)
    }
    composable(
        "reply_diary/{year}/{month}/{day}",
        arguments = listOf(
            navArgument("year") { type = NavType.IntType },
            navArgument("month") { type = NavType.IntType },
            navArgument("day") { type = NavType.IntType }
        )
    ) { backStackEntry ->
        val year = backStackEntry.arguments?.getInt("year") ?: currentDate.year
        val month = backStackEntry.arguments?.getInt("month") ?: currentDate.monthValue
        val day = backStackEntry.arguments?.getInt("day") ?: currentDate.monthValue
        ReplyDiaryRoute(replyDiaryNavigator, year, month, day)
    }
}
