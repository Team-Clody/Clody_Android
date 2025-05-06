package com.sopt.clody.presentation.ui.replyloading.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.sopt.clody.presentation.ui.replyloading.screen.ReplyLoadingRoute
import com.sopt.clody.presentation.utils.navigation.ReplyStatus
import com.sopt.clody.presentation.utils.navigation.Route

fun NavGraphBuilder.replyLoadingScreen(
    navigateToReplyDiary: (Int, Int, Int, ReplyStatus) -> Unit,
    navigateToHome: (year: Int, month: Int, day: Int) -> Unit,
    navigateToDiaryList: (Int, Int) -> Unit,
) {
    composable<Route.ReplyLoading> { backStackEntry ->
        backStackEntry.toRoute<Route.ReplyLoading>().apply {
            ReplyLoadingRoute(
                year = year,
                month = month,
                date = date,
                from = from,
                replyStatus = replyStatus,
                navigateToReplyDiary = navigateToReplyDiary,
                navigateToHome = navigateToHome,
                navigateToDiaryList = navigateToDiaryList,
            )
        }
    }
}

fun NavController.navigateToReplyLoading(
    year: Int,
    month: Int,
    day: Int,
    from: Route.ReplyLoading.ReplyLoadingFrom = Route.ReplyLoading.ReplyLoadingFrom.HOME,
    replyStatus: ReplyStatus = ReplyStatus.UNREADY,
    navOptions: NavOptionsBuilder.() -> Unit = {},
) {
    navigate(
        Route.ReplyLoading(year, month, day, from, replyStatus),
        navOptions,
    )
}
