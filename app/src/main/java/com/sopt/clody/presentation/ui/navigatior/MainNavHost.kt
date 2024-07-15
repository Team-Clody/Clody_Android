package com.sopt.clody.presentation.ui.navigatior

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.sopt.clody.presentation.ui.auth.navigation.AuthNavigator
import com.sopt.clody.presentation.ui.auth.navigation.guidNavGraph
import com.sopt.clody.presentation.ui.auth.navigation.nicknameNavGraph
import com.sopt.clody.presentation.ui.auth.navigation.registerNavGraph
import com.sopt.clody.presentation.ui.auth.navigation.termsOfServiceNavGraph
import com.sopt.clody.presentation.ui.auth.navigation.timeReminderNavGraph
import com.sopt.clody.presentation.ui.diarylist.navigation.DiaryListNavigator
import com.sopt.clody.presentation.ui.diarylist.navigation.diaryListNavGraph
import com.sopt.clody.presentation.ui.home.navigation.HomeNavigator
import com.sopt.clody.presentation.ui.home.navigation.homeNavGraph
import com.sopt.clody.presentation.ui.setting.navigation.SettingNavigator
import com.sopt.clody.presentation.ui.setting.navigation.accountManagementNavGraph
import com.sopt.clody.presentation.ui.setting.navigation.notificationSettingNavGraph
import com.sopt.clody.presentation.ui.setting.navigation.settingNavGraph
import com.sopt.clody.presentation.ui.writediary.navigation.WriteDiaryNavigator
import com.sopt.clody.presentation.ui.writediary.navigation.writeDiaryNavGraph

@Composable
fun MainNavHost(
    modifier: Modifier = Modifier,
    authNavigator: AuthNavigator,
    homeNavigator: HomeNavigator,
    diaryListNavigator: DiaryListNavigator,
    writeDiaryNavigator: WriteDiaryNavigator,
    settingNavigator: SettingNavigator
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        NavHost(
            navController = authNavigator.navController,
            startDestination = authNavigator.startDestination,
        ) {
            registerNavGraph(authNavigator)
            termsOfServiceNavGraph(authNavigator)
            nicknameNavGraph(authNavigator)
            guidNavGraph(authNavigator)
            timeReminderNavGraph(authNavigator)
            homeNavGraph(homeNavigator)
            diaryListNavGraph(diaryListNavigator)
            writeDiaryNavGraph(writeDiaryNavigator)
            settingNavGraph(settingNavigator)
            accountManagementNavGraph(settingNavigator)
            notificationSettingNavGraph(settingNavigator)
        }
    }
}
