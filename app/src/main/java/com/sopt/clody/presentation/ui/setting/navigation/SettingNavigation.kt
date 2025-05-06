package com.sopt.clody.presentation.ui.setting.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.sopt.clody.presentation.ui.setting.notificationsetting.screen.NotificationSettingRoute
import com.sopt.clody.presentation.ui.setting.screen.AccountManagementRoute
import com.sopt.clody.presentation.ui.setting.screen.SettingRoute
import com.sopt.clody.presentation.ui.setting.screen.WebViewRoute
import com.sopt.clody.presentation.utils.navigation.Route

fun NavGraphBuilder.settingScreen(
    navigateToAccountManagement: () -> Unit,
    navigateToNotification: () -> Unit,
    navigateToPrevious: () -> Unit,
    navigateToWebView: (String) -> Unit,
) {
    composable<Route.Setting> {
        SettingRoute(
            navigateToAccountManagement = navigateToAccountManagement,
            navigateToNotification = navigateToNotification,
            navigateToPrevious = navigateToPrevious,
            navigateToWebView = navigateToWebView,
        )
    }
}

fun NavGraphBuilder.accountManagementScreen(
    navigateToPrevious: () -> Unit,
    navigateToLogin: () -> Unit,
) {
    composable<Route.AccountManagement> {
        AccountManagementRoute(
            navigateToPrevious = navigateToPrevious,
            navigateToLogin = navigateToLogin,
        )
    }
}

fun NavGraphBuilder.notificationSettingScreen(
    navigateToPrevious: () -> Unit,
) {
    composable<Route.NotificationSetting> {
        NotificationSettingRoute(navigateToPrevious = navigateToPrevious)
    }
}

fun NavGraphBuilder.webViewScreen(
    navigateToPrevious: () -> Unit,
) {
    composable<Route.WebView> { backStackEntry ->
        backStackEntry.toRoute<Route.WebView>().apply {
            WebViewRoute(
                encodedUrl = encodedUrl,
                navigateToPrevious = navigateToPrevious,
            )
        }
    }
}

fun NavController.navigateToSetting(
    navOptions: NavOptionsBuilder.() -> Unit = {},
) {
    navigate(Route.Setting, navOptions)
}

fun NavController.navigateToAccountManagement(
    navOptions: NavOptionsBuilder.() -> Unit = {},
) {
    navigate(Route.AccountManagement, navOptions)
}

fun NavController.navigateToNotificationSetting(
    navOptions: NavOptionsBuilder.() -> Unit = {},
) {
    navigate(Route.NotificationSetting, navOptions)
}

fun NavController.navigateToWebView(
    encodedUrl: String,
    navOptions: NavOptionsBuilder.() -> Unit = {},
) {
    navigate(Route.WebView(encodedUrl), navOptions)
}
