package com.sopt.clody.presentation.ui.home.navigation

import androidx.navigation.NavController

class HomeNavigator(
    val navController: NavController
) {
    fun navigateDiaryList() {
        navController.navigate("diary_list")
    }

    fun navigateSetting() {
        navController.navigate("setting")
    }

    fun navigateWriteDiary(year: Int, month: Int, day: Int) {
        navController.navigate("write_diary/$year/$month/$day")
    }


    fun navigateReplyDiary() {
        navController.navigate("reply_diary")
    }

    fun navigateBack() {
        navController.popBackStack()
    }
}
