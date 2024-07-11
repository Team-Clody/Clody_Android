package com.sopt.clody.presentation.ui.navigatior

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.sopt.clody.presentation.ui.auth.screen.RegisterRoute
import com.sopt.clody.presentation.ui.auth.screen.TermsOfServiceRoute


fun NavGraphBuilder.registerNavGraph(
    navigator: MainNavigator
) {
    navigation(startDestination = "register", route = "register_graph") {
        composable("register") {
            RegisterRoute(navigator)
        }
    }
}

fun NavGraphBuilder.termsOfServiceNavGraph(
    navigator: MainNavigator
) {
    composable("terms_of_service") {
        TermsOfServiceRoute(navigator)
    }
}
