package com.sopt.clody.presentation.ui.navigatior

import androidx.compose.foundation.layout.Box
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
import com.sopt.clody.presentation.ui.diarylist.navigation.DiaryListNavigator
import com.sopt.clody.presentation.ui.diarylist.navigation.diaryListNavGraph
import com.sopt.clody.presentation.ui.home.navigation.HomeNavigator
import com.sopt.clody.presentation.ui.home.navigation.homeNavGraph
import com.sopt.clody.presentation.ui.replydiary.navigation.ReplyDiaryNavigator
import com.sopt.clody.presentation.ui.replyloading.navigation.ReplyLoadingNavigator
import com.sopt.clody.presentation.ui.replyloading.navigation.replyLoadingNavGraph
import com.sopt.clody.presentation.ui.setting.navigation.SettingNavigator
import com.sopt.clody.presentation.ui.setting.navigation.accountManagementNavGraph
import com.sopt.clody.presentation.ui.setting.navigation.notificationSettingNavGraph
import com.sopt.clody.presentation.ui.setting.navigation.settingNavGraph
import com.sopt.clody.presentation.ui.setting.navigation.webViewNavGraph
import com.sopt.clody.presentation.ui.splash.SplashRoute
import com.sopt.clody.presentation.ui.writediary.navigation.WriteDiaryNavigator
import com.sopt.clody.presentation.ui.writediary.navigation.writeDiaryNavGraph

@Composable
fun MainNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    authNavigator: AuthNavigator,
    homeNavigator: HomeNavigator,
    diaryListNavigator: DiaryListNavigator,
    writeDiaryNavigator: WriteDiaryNavigator,
    settingNavigator: SettingNavigator,
    replyLoadingNavigator: ReplyLoadingNavigator,
    replyDiaryNavigator: ReplyDiaryNavigator,
) {
    Box(
        modifier = modifier,
    ) {
        NavHost(
            navController = navController,
            startDestination = "splash",
        ) {
            composable("splash") { SplashRoute(navigator = authNavigator) }
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
            webViewNavGraph(settingNavigator)
            replyLoadingNavGraph(replyLoadingNavigator, replyDiaryNavigator)
        }
    }
}
