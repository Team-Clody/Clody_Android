package com.sopt.clody.presentation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.sopt.clody.presentation.auth.RegisterRoute
import com.sopt.clody.presentation.auth.TermsOfServiceRoute
import com.sopt.clody.presentation.main.MainNavigator


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
