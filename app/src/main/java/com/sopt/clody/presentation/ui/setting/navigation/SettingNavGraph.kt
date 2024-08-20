package com.sopt.clody.presentation.ui.setting.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.sopt.clody.presentation.ui.setting.screen.AccountManagementRoute
import com.sopt.clody.presentation.ui.setting.screen.NotificationSettingRoute
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
        route = "web_view/{encodeUrl}",
        arguments = listOf(navArgument("encodeUrl") { type = NavType.StringType })
    ) { backStackEntry ->
        val encodeUrl = backStackEntry.arguments?.getString("encodeUrl")
        encodeUrl?.let {
            WebViewRoute(navigator, it)
        }
    }
}
