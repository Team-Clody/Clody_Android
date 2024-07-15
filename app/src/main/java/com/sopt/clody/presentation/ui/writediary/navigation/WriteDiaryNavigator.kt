package com.sopt.clody.presentation.ui.writediary.navigation

import androidx.navigation.NavHostController


class WriteDiaryNavigator(
    val navController: NavHostController
) {
    fun navigateWriteDiary() {
        navController.navigate("write_diary")
    }

    fun navigateBack() {
        navController.popBackStack()
    }
}
