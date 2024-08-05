package com.sopt.clody.presentation.ui.writediary.navigation

import androidx.navigation.NavHostController


class WriteDiaryNavigator(
    val navController: NavHostController
) {
    fun navigateReplyLoading(year: Int, month: Int, day: Int) {
        navController.navigate("reply_loading/$year/$month/$day?from=write_diary")
    }

    fun navigateBack() {
        navController.navigateUp()
    }
}
