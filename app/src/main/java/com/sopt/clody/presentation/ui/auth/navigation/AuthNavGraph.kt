package com.sopt.clody.presentation.ui.auth.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.sopt.clody.presentation.ui.auth.screen.GuideRoute
import com.sopt.clody.presentation.ui.auth.screen.NicknameRoute
import com.sopt.clody.presentation.ui.auth.screen.TermsOfServiceRoute
import com.sopt.clody.presentation.ui.auth.timereminder.TimeReminderRoute
import com.sopt.clody.presentation.ui.auth.signup.SignUpRoute


fun NavGraphBuilder.registerNavGraph(
    navigator: AuthNavigator
) {
    navigation(startDestination = "register", route = "register_graph") {
        composable("register") {
            SignUpRoute(navigator)
        }
    }
}

fun NavGraphBuilder.termsOfServiceNavGraph(
    navigator: AuthNavigator
) {
    composable("terms_of_service") {
        TermsOfServiceRoute(navigator)
    }
}

fun NavGraphBuilder.nicknameNavGraph(
    navigator: AuthNavigator
) {
    composable("nickname") {
        NicknameRoute(navigator)
    }
}

fun NavGraphBuilder.guidNavGraph(
    navigator: AuthNavigator
) {
    composable("guide") {
        GuideRoute(navigator)
    }
}

fun NavGraphBuilder.timeReminderNavGraph(
    navigator: AuthNavigator
) {
    composable("time_reminder") {
        TimeReminderRoute(navigator)
    }
}
