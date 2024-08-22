package com.sopt.clody.presentation.ui.setting.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.sopt.clody.presentation.ui.setting.notificationsetting.screen.NotificationSettingRoute
import com.sopt.clody.presentation.ui.setting.screen.AccountManagementRoute
import com.sopt.clody.presentation.ui.setting.screen.SettingRoute
import com.sopt.clody.presentation.ui.setting.screen.WebViewRoute

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

fun NavGraphBuilder.webViewNavGraph(
    navigator: SettingNavigator
) {
    composable(
        route = "web_view/{encodedUrl}",
        arguments = listOf(navArgument("encodedUrl") { type = NavType.StringType })
    ) { backStackEntry ->
        val encodedUrl = backStackEntry.arguments?.getString("encodedUrl")
        encodedUrl?.let {
            WebViewRoute(navigator, it)
        }
    }
}
