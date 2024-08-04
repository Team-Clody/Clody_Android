package com.sopt.clody.presentation.ui.replydiary.navigation

import androidx.navigation.NavHostController

class ReplyDiaryNavigator(
    val navController: NavHostController
) {
    fun navigateHome() {
        navController.navigate("home") {
            popUpTo(navController.graph.startDestinationId) {
                inclusive = true
            }
        }
    }

    fun navigateBack() {
        navController.navigateUp()
    }
}
