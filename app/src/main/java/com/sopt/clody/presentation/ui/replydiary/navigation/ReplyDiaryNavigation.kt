package com.sopt.clody.presentation.ui.replydiary.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.sopt.clody.domain.type.ReplyStatus
import com.sopt.clody.presentation.ui.replydiary.ReplyDiaryRoute
import com.sopt.clody.presentation.utils.navigation.Route

fun NavGraphBuilder.replyDiaryScreen(
    navigateToHome: (year: Int, month: Int, date: Int, isFromReplyDiary: Boolean) -> Unit,
) {
    composable<Route.ReplyDiary> { backStackEntry ->
        backStackEntry.toRoute<Route.ReplyDiary>().apply {
            ReplyDiaryRoute(
                year = year,
                month = month,
                date = date,
                replyStatus = replyStatus,
                navigateToHome = navigateToHome,
            )
        }
    }
}

fun NavController.navigateToReplyDiary(
    year: Int,
    month: Int,
    day: Int,
    replyStatus: ReplyStatus = ReplyStatus.UNREADY,
    navOptions: NavOptionsBuilder.() -> Unit = {},
) {
    navigate(Route.ReplyDiary(year, month, day, replyStatus), navOptions)
}
