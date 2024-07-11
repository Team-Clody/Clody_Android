package com.sopt.clody.presentation.ui.navigatior

import androidx.navigation.NavHostController


class MainNavigator(
    val navController: NavHostController
) {
    val startDestination = "register_graph"
    fun navigateTermsOfService() {
        navController.navigate("terms_of_service")
    }
    fun navigateBack() {
        navController.popBackStack()
    }
}
