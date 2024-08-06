package com.sopt.clody.presentation.ui.setting.navigation

import androidx.navigation.NavHostController

class SettingNavigator(
    val navController: NavHostController
) {
    fun navigateAccountManagement() {
        navController.navigate("account_management")
    }

    fun navigateNotificationSetting() {
        navController.navigate("notification_setting")
    }

    fun navigateBack() {
        navController.navigateUp()
    }
}
