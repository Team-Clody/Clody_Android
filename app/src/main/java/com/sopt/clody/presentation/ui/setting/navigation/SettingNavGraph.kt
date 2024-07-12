package com.sopt.clody.presentation.ui.setting.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.sopt.clody.presentation.ui.setting.screen.AccountManagementRoute
import com.sopt.clody.presentation.ui.setting.screen.NotificationSettingRoute
import com.sopt.clody.presentation.ui.setting.screen.SettingRoute

fun NavGraphBuilder.settingNavGraph(
    navigator: SettingNavigator
) {
    composable("setting") {
        SettingRoute(navigator)
    }
}

fun NavGraphBuilder.accountManagementNavGraph(
    navigator: SettingNavigator
) {
    composable("account_management") {
        AccountManagementRoute(navigator)
    }
}

fun NavGraphBuilder.notificationSettingNavGraph(
    navigator: SettingNavigator
) {
    composable("notification_setting") {
        NotificationSettingRoute(navigator)
    }
}
