package com.sopt.clody.presentation.ui.setting.screen

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.sopt.clody.presentation.ui.navigatior.MainNavigator
import com.sopt.clody.presentation.ui.setting.navigation.SettingNavigator

@Composable
fun NotificationSettingRoute(
    navigator: SettingNavigator
) {
    NotificationSettingScreen(
        onBackClick = { navigator.navigateToBack() }
    )
}

@Composable
fun NotificationSettingScreen(onBackClick: () -> Unit) {
    Text("알림 설정 페이지")
}
