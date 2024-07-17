package com.sopt.clody.presentation.ui.writediary.navigation

import androidx.navigation.NavHostController


class WriteDiaryNavigator(
    val navController: NavHostController
) {
    fun navigateReplyLoading() {
        navController.navigate("reply_loading")
    }

    fun navigateBack() {
        navController.popBackStack()
    }
}
