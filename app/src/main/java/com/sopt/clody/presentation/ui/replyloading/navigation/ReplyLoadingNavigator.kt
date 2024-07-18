package com.sopt.clody.presentation.ui.replyloading.navigation

import androidx.navigation.NavHostController

class ReplyLoadingNavigator(
    val navController: NavHostController
) {
    fun navigateReplyDiary(year: Int, month: Int, day: Int) {
        navController.navigate("reply_diary/$year/$month/$day")
    }

    fun navigateBack() {
        navController.popBackStack()
    }
}
