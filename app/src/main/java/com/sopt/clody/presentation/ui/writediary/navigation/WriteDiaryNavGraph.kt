package com.sopt.clody.presentation.ui.writediary.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.sopt.clody.presentation.ui.writediary.screen.WriteDiaryRoute


fun NavGraphBuilder.writeDiaryNavGraph(
    navigator: WriteDiaryNavigator
) {
    composable("write_diary") {
        WriteDiaryRoute(navigator)
    }
}
