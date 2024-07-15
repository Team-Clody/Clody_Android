package com.sopt.clody.presentation.ui.diarylist.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.sopt.clody.presentation.ui.diarylist.screen.DiaryListRoute

fun NavGraphBuilder.diaryListNavGraph(
    navigator: DiaryListNavigator
) {
    composable("diary_list") {
        DiaryListRoute(navigator)
    }
}
