package com.sopt.clody.presentation.ui.setting.screen

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.sopt.clody.presentation.ui.setting.navigation.SettingNavigator

@Composable
fun NotificationSettingRoute(
    navigator: SettingNavigator
) {
    NotificationSettingScreen(
        onBackClick = { navigator.navigateBack() }
    )
}

@Composable
fun NotificationSettingScreen(onBackClick: () -> Unit) {
    Text("알림 설정 페이지")
}
