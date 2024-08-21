package com.sopt.clody.presentation.ui.writediary.navigation

import androidx.navigation.NavHostController


class WriteDiaryNavigator(
    val navController: NavHostController
) {
    fun navigateReplyLoading(year: Int, month: Int, day: Int, replyStatus: String = "READY_NOT_READ") {
        navController.navigate("reply_loading/$year/$month/$day?from=write_diary&replyStatus=$replyStatus")
    }

    fun navigateBack() {
        navController.navigateUp()
    }
}
