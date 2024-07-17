package com.sopt.clody.presentation.ui.replyloading.navigation

import androidx.navigation.NavHostController

class ReplyLoadingNavigator(
    val navController: NavHostController
) {
    fun navigateBack() {
        navController.popBackStack()
    }
}
