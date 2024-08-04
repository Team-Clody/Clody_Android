package com.sopt.clody.presentation.ui.replydiary.navigation

import androidx.navigation.NavHostController

class ReplyDiaryNavigator(
    val navController: NavHostController
) {
    fun navigateHome(selectedYear: Int, selectedMonth: Int) {
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
