package com.sopt.clody.presentation.ui.diarylist.navigation

import androidx.navigation.NavController

class DiaryListNavigator(
    val navController: NavController
) {
    fun navigateHome(selectedYear: Int, selectedMonth: Int) {
        navController.navigate("home/$selectedYear/$selectedMonth")
    }

    fun navigateReplyLoading(year: Int, month: Int, day: Int, replyStatus: String) {
        navController.navigate("reply_loading/$year/$month/$day?from=diary_list&replyStatus=$replyStatus")
    }

    fun navigateBack() {
        navController.navigateUp()
    }
}
