package com.sopt.clody.presentation.ui.setting.navigation

import androidx.navigation.NavHostController

class SettingNavigator(
    val navController: NavHostController
) {
    fun navigateToSetting() {
        navController.navigate("setting")
    }

    fun navigateToAccountManagement() {
        navController.navigate("account_management")
    }

    fun navigateToNotificationSetting() {
        navController.navigate("notification_setting")
    }

    fun navigateToBack() {
        navController.popBackStack()
    }
}
