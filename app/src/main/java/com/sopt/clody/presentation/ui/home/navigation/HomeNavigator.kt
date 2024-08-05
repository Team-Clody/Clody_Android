package com.sopt.clody.presentation.ui.home.navigation

import androidx.navigation.NavController

class HomeNavigator(
    val navController: NavController
) {
    fun navigateDiaryList(selectedYear: Int, selectedMonth: Int) {
        navController.navigate("diary_list/$selectedYear/$selectedMonth")
    }

    fun navigateSetting() {
        navController.navigate("setting")
    }

    fun navigateWriteDiary(year: Int, month: Int, day: Int) {
        navController.navigate("write_diary/$year/$month/$day")
    }

    fun navigateReplyLoading(year: Int, month: Int, day: Int) {
        navController.navigate("reply_loading/$year/$month/$day?from=home")
    }

    fun navigateBack() {
        navController.navigateUp()
    }
}

