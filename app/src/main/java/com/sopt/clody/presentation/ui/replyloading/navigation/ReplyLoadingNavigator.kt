package com.sopt.clody.presentation.ui.replyloading.navigation

import androidx.navigation.NavHostController

class ReplyLoadingNavigator(
    private val navController: NavHostController
) {
    fun navigateHome(selectedYear: Int, selectedMonth: Int) {
        navController.navigate("home/$selectedYear/$selectedMonth") {
            popUpTo(navController.graph.startDestinationId) {
                inclusive = true
            }
        }
    }

    fun navigateReplyDiary(year: Int, month: Int, day: Int, replyStatus: String) {
        navController.navigate("reply_diary/$year/$month/$day?replyStatus=$replyStatus")
    }

    private fun navigateWithPopUp(route: String, inclusive: Boolean = false) {
        navController.navigate(route) {
            popUpTo(navController.graph.startDestinationId) {
                this.inclusive = inclusive
            }
        }
    }

    fun navigateBack(selectedYear: Int, selectedMonth: Int, from: String) {

        when (from) {
            "diary_list" -> {
                navigateWithPopUp("diary_list/$selectedYear/$selectedMonth")
            }

            "home" -> {
                navigateWithPopUp("home/$selectedYear/$selectedMonth")
            }

            "write_diary" -> {
                navigateWithPopUp("home/$selectedYear/$selectedMonth") // 예외적으로 홈으로
            }

            else -> {
                navController.navigateUp()
            }
        }
    }
}
