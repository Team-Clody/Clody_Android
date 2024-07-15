package com.sopt.clody.presentation.ui.diarylist.navigation

import androidx.navigation.NavController

class DiaryListNavigator(
    val navController: NavController
) {
    fun navigateCalendar() {
        navController.navigate("home")
    }

    fun navigateReplyDiary() {
        navController.navigate("reply_diary")
    }

    fun navigateBack() {
        navController.popBackStack()
    }
}
