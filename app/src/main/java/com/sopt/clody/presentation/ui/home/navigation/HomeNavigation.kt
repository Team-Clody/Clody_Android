package com.sopt.clody.presentation.ui.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.sopt.clody.presentation.ui.type.ReplyStatus
import com.sopt.clody.presentation.ui.home.screen.HomeRoute
import com.sopt.clody.presentation.utils.navigation.Route
import java.time.LocalDate

fun NavGraphBuilder.homeScreen(
    navigateToDiaryList: (year: Int, month: Int) -> Unit,
    navigateToSetting: () -> Unit,
    navigateToWriteDiary: (year: Int, month: Int, date: Int) -> Unit,
    navigateToReplyLoading: (
        year: Int,
        month: Int,
        date: Int,
        from: Route.ReplyLoading.ReplyLoadingFrom,
        replyStatus: ReplyStatus,
    ) -> Unit,
) {
    composable<Route.Home> { backStackEntry ->
        backStackEntry.toRoute<Route.Home>().apply {
            HomeRoute(
                isFromReplyDiary = isFromReplyDiary,
                navigateToDiaryList = navigateToDiaryList,
                navigateToSetting = navigateToSetting,
                navigateToWriteDiary = navigateToWriteDiary,
                navigateToReplyLoading = navigateToReplyLoading,
            )
        }
    }
}

fun NavController.navigateToHome(
    selectedYear: Int = LocalDate.now().year,
    selectedMonth: Int = LocalDate.now().monthValue,
    selectedDay: Int? = LocalDate.now().dayOfMonth,
    isFromReplyDiary: Boolean = false,
    navOptions: NavOptionsBuilder.() -> Unit = {},
) {
    navigate(Route.Home(selectedYear, selectedMonth, selectedDay, isFromReplyDiary), navOptions)
}
