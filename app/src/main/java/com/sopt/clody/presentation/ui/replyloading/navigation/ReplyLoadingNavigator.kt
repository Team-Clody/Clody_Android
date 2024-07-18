package com.sopt.clody.presentation.ui.replyloading.navigation

import androidx.navigation.NavHostController

class ReplyLoadingNavigator(
    private val navController: NavHostController
) {
    fun navigateHome() {
        navController.navigate("home") {
            popUpTo(navController.graph.startDestinationId) {
                inclusive = true
            }
        }
    }

    fun navigateReplyDiary(year: Int, month: Int, day: Int) {
        navController.navigate("reply_diary/$year/$month/$day")
    }

    fun navigateBack(from: String) {
        when (from) {
            "diary_list" -> {
                navController.popBackStack("diary_list", false)
            }

            "home" -> {
                navController.popBackStack("home", false)
            }

            "write_diary" -> {
                navController.popBackStack("home", false) // 예외적으로 홈으로 이동
            }

            else -> {
                navController.popBackStack()
            }
        }
    }
}


