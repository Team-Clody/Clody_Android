package com.sopt.clody.presentation.ui.webview

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.sopt.clody.presentation.utils.navigation.Route

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
