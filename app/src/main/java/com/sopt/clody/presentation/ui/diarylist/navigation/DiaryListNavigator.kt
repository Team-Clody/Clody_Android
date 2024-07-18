package com.sopt.clody.presentation.ui.diarylist.navigation

import androidx.navigation.NavController

class DiaryListNavigator(
    val navController: NavController
) {
    fun navigateCalendar() {
        navController.navigate("home")
    }

    fun navigateReplyLoading(year: Int, month: Int, day: Int) {
        navController.navigate("reply_loading/$year/$month/$day?from=diary_list")
    }

    fun navigateBack() {
        navController.popBackStack()
    }
}
