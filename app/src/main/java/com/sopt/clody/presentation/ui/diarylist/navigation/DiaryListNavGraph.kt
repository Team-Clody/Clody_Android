package com.sopt.clody.presentation.ui.diarylist.navigation

import DiaryListRoute
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

fun NavGraphBuilder.diaryListNavGraph(
    navigator: DiaryListNavigator
) {
    composable("diary_list") {
        DiaryListRoute(navigator)
    }
}
