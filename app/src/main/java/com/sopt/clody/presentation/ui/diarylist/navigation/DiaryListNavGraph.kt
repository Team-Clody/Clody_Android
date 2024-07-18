package com.sopt.clody.presentation.ui.diarylist.navigation

import DiaryListRoute
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.sopt.clody.presentation.ui.replyloading.navigation.ReplyLoadingNavigator
import com.sopt.clody.presentation.ui.replyloading.screen.ReplyLoadingRoute

fun NavGraphBuilder.diaryListNavGraph(
    navigator: DiaryListNavigator,
    replyLoadingNavigator: ReplyLoadingNavigator
) {
    composable("diary_list") {
        DiaryListRoute(navigator)
    }
    composable("reply_loading/{year}/{month}/{day}?from={from}",
        arguments = listOf(
            navArgument("year") { type = NavType.IntType },
            navArgument("month") { type = NavType.IntType },
            navArgument("day") { type = NavType.IntType },
            navArgument("from") { defaultValue = "diary_list" } // 기본값 설정
        )
    ) { backStackEntry ->
        val year = backStackEntry.arguments?.getInt("year") ?: 0
        val month = backStackEntry.arguments?.getInt("month") ?: 0
        val day = backStackEntry.arguments?.getInt("day") ?: 0
        val from = backStackEntry.arguments?.getString("from") ?: "diary_list" // from 파라미터 가져오기
        ReplyLoadingRoute(replyLoadingNavigator, year, month, day, from)
    }
}
