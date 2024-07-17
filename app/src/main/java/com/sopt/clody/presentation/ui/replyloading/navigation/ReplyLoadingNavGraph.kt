package com.sopt.clody.presentation.ui.replyloading.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.sopt.clody.presentation.ui.replyloading.screen.ReplyLoadingRoute

fun NavGraphBuilder.replyLoadingNavGraph(
    navigator: ReplyLoadingNavigator
) {
    composable("reply_loading") {
        ReplyLoadingRoute(navigator)
    }
}
