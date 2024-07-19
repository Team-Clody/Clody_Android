package com.sopt.clody.presentation.ui.auth.navigation

import androidx.navigation.NavHostController


class AuthNavigator(
    val navController: NavHostController
) {
    val startDestination = "register_graph"
    fun navigateTermsOfService() {
        navController.navigate("terms_of_service")
    }
    fun navigateNickname() {
        navController.navigate("nickname")
    }
    fun navigateGuide() {
        navController.navigate("guide")
    }

    fun navigateTimeReminder() {
        navController.navigate("time_reminder")
    }

    fun navigateHome() {
        navController.navigate("home")
    }

    fun navigateBack() {
        navController.popBackStack()
    }
    fun navigateToSignupScreen() {
        navController.navigate("register") {
            popUpTo(navController.graph.startDestinationId) { inclusive = true }
        }
    }
}
