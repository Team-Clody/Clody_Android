package com.sopt.clody.presentation.ui.setting.navigation

import androidx.navigation.NavHostController
import java.net.URLEncoder

class SettingNavigator(
    val navController: NavHostController
) {
    fun navigateAccountManagement() {
        navController.navigate("account_management")
    }

    fun navigateNotificationSetting() {
        navController.navigate("notification_setting")
    }

    fun navigateWebView(url: String) {
        val encodedUrl = URLEncoder.encode(url, "UTF-8")
        navController.navigate("web_view/$encodedUrl")
    }

    fun navigateBack() {
        navController.navigateUp()
    }
}
