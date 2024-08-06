package com.sopt.clody.presentation.ui.replydiary.navigation

import androidx.navigation.NavHostController
import java.time.LocalDate

class ReplyDiaryNavigator(
    val navController: NavHostController
) {
    fun navigateHome(selectedYear: Int = LocalDate.now().year, selectedMonth: Int = LocalDate.now().monthValue) {
        navController.navigate("home/$selectedYear/$selectedMonth") {
            popUpTo(navController.graph.startDestinationId) {
                inclusive = true
            }
        }
    }

    fun navigateBack() {
        navController.navigateUp()
    }
}
