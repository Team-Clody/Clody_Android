package com.sopt.clody.presentation.utils.navigation

import androidx.navigation.NavController

/**
 * 현재 backStack 상단에서 popBackStack 동작을 수행하는 helper 함수.
 */
fun NavController.safePopBackStack() {
    if (currentBackStackEntry?.lifecycle?.currentState == androidx.lifecycle.Lifecycle.State.RESUMED) {
        popBackStack()
    }
}
