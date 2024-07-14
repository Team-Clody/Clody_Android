package com.sopt.clody.presentation.ui.home.navigator

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

    fun navigateReplyDiary() {
        navController.navigate("reply_diary")
    }

    fun navigateBack() {
        navController.popBackStack()
    }
}
