package com.sopt.clody.presentation.ui.navigatior

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.sopt.clody.presentation.ui.auth.navigation.AuthNavigator
import com.sopt.clody.presentation.ui.auth.navigation.guidNavGraph
import com.sopt.clody.presentation.ui.auth.navigation.nicknameNavGraph
import com.sopt.clody.presentation.ui.auth.navigation.registerNavGraph
import com.sopt.clody.presentation.ui.auth.navigation.termsOfServiceNavGraph
import com.sopt.clody.presentation.ui.auth.navigation.timeReminderNavGraph
import com.sopt.clody.presentation.ui.auth.screen.SignUpScreen
import com.sopt.clody.presentation.ui.diarylist.navigator.DiaryListNavigator
import com.sopt.clody.presentation.ui.diarylist.navigator.diaryListNavGraph
import com.sopt.clody.presentation.ui.setting.navigation.SettingNavigator
import com.sopt.clody.presentation.ui.setting.navigation.accountManagementNavGraph
import com.sopt.clody.presentation.ui.setting.navigation.notificationSettingNavGraph
import com.sopt.clody.presentation.ui.setting.navigation.settingNavGraph
import com.sopt.clody.presentation.ui.splash.SplashScreen

@Composable
fun MainNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    authNavigator: AuthNavigator,
    diaryListNavigator: DiaryListNavigator,
    settingNavigator: SettingNavigator
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        NavHost(
            navController = navController,
            startDestination = "splash",
        ) {
            composable("splash") { SplashScreen(navController = authNavigator.navController) }
            registerNavGraph(authNavigator)
            termsOfServiceNavGraph(authNavigator)
            nicknameNavGraph(authNavigator)
            guidNavGraph(authNavigator)
            timeReminderNavGraph(authNavigator)
            diaryListNavGraph(diaryListNavigator)
            settingNavGraph(settingNavigator)
            accountManagementNavGraph(settingNavigator)
            notificationSettingNavGraph(settingNavigator)
        }
    }
}
