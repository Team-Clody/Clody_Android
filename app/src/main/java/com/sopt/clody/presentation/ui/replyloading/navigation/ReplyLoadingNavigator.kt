package com.sopt.clody.presentation.ui.replyloading.navigation

import androidx.navigation.NavHostController
import java.time.LocalDate

class ReplyLoadingNavigator(
    private val navController: NavHostController
) {
    fun navigateHome(selectedYear: Int = LocalDate.now().year, selectedMonth: Int = LocalDate.now().monthValue) {
        navController.navigate("home/$selectedYear/$selectedMonth") {
            popUpTo(navController.graph.startDestinationId) {
                inclusive = true
            }
        }
    }

    fun navigateReplyDiary(year: Int, month: Int, day: Int) {
        navController.navigate("reply_diary/$year/$month/$day")
    }

    fun navigateBack(selectedYear: Int, selectedMonth: Int, from: String) {
        when (from) {
            "diary_list" -> {
                navController.popBackStack("diary_list", false)
            }

            "home/$selectedYear/$selectedMonth" -> {
                navController.popBackStack("home/$selectedYear/$selectedMonth", false)
            }

            "write_diary" -> {
                navController.popBackStack("home/$selectedYear/$selectedMonth", false) // 예외적으로 홈으로 이동
            }

            else -> {
                navController.navigateUp()
            }
        }
    }
}


