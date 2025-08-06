package com.sopt.clody.presentation.ui.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.sopt.clody.presentation.utils.navigation.Route

/**
 * 앱의 전역 상태를 관리하는 클래스.
 * 현재는 네비게이션 컨트롤러만 포함되어 있으며,
 * 추후 다른 상태(예: 사용자 인증 상태, 테마 설정 등) 추가 가능.
 */
@Stable
class ClodyAppState(
    val navController: NavHostController,
) {
    val startDestination = Route.Splash
}

/**
 * [ClodyAppState]를 기억하고 유지하는 Composable 함수.
 * [rememberNavController]를 활용해 NavController를 생성.
 */
@Composable
fun rememberClodyAppState(
    navController: NavHostController = rememberNavController(),
): ClodyAppState = remember { ClodyAppState(navController) }
