package com.sopt.clody.presentation.ui.writediary.navigator

import androidx.navigation.NavHostController


class WriteDiaryNavigator(
    val navController: NavHostController
) {

    fun navigateWriteDiary() {
        navController.navigate("write_diary")
    }

}
