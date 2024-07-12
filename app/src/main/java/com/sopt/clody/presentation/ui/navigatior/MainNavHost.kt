package com.sopt.clody.presentation.ui.navigatior

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.sopt.clody.presentation.ui.setting.navigation.SettingNavigator
import com.sopt.clody.presentation.ui.setting.navigation.accountManagementNavGraph
import com.sopt.clody.presentation.ui.setting.navigation.notificationSettingNavGraph
import com.sopt.clody.presentation.ui.setting.navigation.settingNavGraph

@Composable
fun MainNavHost(
    modifier: Modifier = Modifier,
    mainNavigator: MainNavigator,
    settingNavigator: SettingNavigator
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        NavHost(
            navController = mainNavigator.navController,
            startDestination = mainNavigator.startDestination,
        ) {
            registerNavGraph(mainNavigator)
            termsOfServiceNavGraph(mainNavigator)
            settingNavGraph(settingNavigator)
            accountManagementNavGraph(settingNavigator)
            notificationSettingNavGraph(settingNavigator)
        }
    }
}
