package com.sopt.clody.presentation.ui.diarylist.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.sopt.clody.presentation.ui.diarylist.screen.DiaryListRoute
import com.sopt.clody.presentation.utils.navigation.Route

fun NavGraphBuilder.diaryListScreen(
    navigateToHome: (year: Int, month: Int) -> Unit,
    navigateToReplyLoading: (year: Int, month: Int, date: Int, replyStatus: Route.ReplyLoading.ReplyLoadingFrom) -> Unit,
) {
    composable<Route.DiaryList> { backStackEntry ->
        backStackEntry.toRoute<Route.DiaryList>().apply {
            DiaryListRoute(
                selectedYearFromHome = selectedYearFromHome,
                selectedMonthFromHome = selectedMonthFromHome,
                navigateToHome = navigateToHome,
                navigateToReplyLoading = navigateToReplyLoading,
            )
        }
    }
}

fun NavController.navigateToDiaryList(
    selectedYearFromHome: Int,
    selectedMonthFromHome: Int,
) {
    navigate(Route.DiaryList(selectedYearFromHome, selectedMonthFromHome))
}
