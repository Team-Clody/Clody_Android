package com.sopt.clody.presentation.ui.replyloading.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.sopt.clody.presentation.ui.replydiary.ReplyDiaryRoute
import com.sopt.clody.presentation.ui.replydiary.navigation.ReplyDiaryNavigator
import com.sopt.clody.presentation.ui.replyloading.screen.ReplyLoadingRoute

fun NavGraphBuilder.replyLoadingNavGraph(
    replyLoadingNavigator: ReplyLoadingNavigator,
    replyDiaryNavigator: ReplyDiaryNavigator
) {
    composable(
        "reply_loading/{year}/{month}/{day}?from={from}",
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
        val from = backStackEntry.arguments?.getString("from") ?: "home" // from 파라미터 가져오기
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
        val year = backStackEntry.arguments?.getInt("year") ?: 0
        val month = backStackEntry.arguments?.getInt("month") ?: 0
        val day = backStackEntry.arguments?.getInt("day") ?: 0
        ReplyDiaryRoute(replyDiaryNavigator, year, month, day)
    }
}
