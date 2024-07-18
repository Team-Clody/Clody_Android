package com.sopt.clody.presentation.ui.writediary.navigation

import androidx.navigation.NavHostController


class WriteDiaryNavigator(
    val navController: NavHostController
) {
    fun navigateWriteDiary() {
        navController.navigate("write_diary")
    }

    fun navigateReplyLoading(year: Int, month: Int, day: Int) {
        navController.navigate("reply_loading/$year/$month/$day")
    }

    fun navigateBack() {
        navController.popBackStack()
    }
}
