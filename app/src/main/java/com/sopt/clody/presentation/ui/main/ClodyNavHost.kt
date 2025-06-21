package com.sopt.clody.presentation.ui.main

import android.content.Intent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.sopt.clody.presentation.ui.auth.guide.navigation.guideScreen
import com.sopt.clody.presentation.ui.auth.guide.navigation.navigateToGuide
import com.sopt.clody.presentation.ui.auth.signup.navigation.navigateToSignUp
import com.sopt.clody.presentation.ui.auth.signup.navigation.signUpScreen
import com.sopt.clody.presentation.ui.auth.timereminder.navigateToTimeReminder
import com.sopt.clody.presentation.ui.auth.timereminder.timeReminderScreen
import com.sopt.clody.presentation.ui.diarylist.navigation.diaryListScreen
import com.sopt.clody.presentation.ui.diarylist.navigation.navigateToDiaryList
import com.sopt.clody.presentation.ui.home.navigation.homeScreen
import com.sopt.clody.presentation.ui.home.navigation.navigateToHome
import com.sopt.clody.presentation.ui.login.navigation.loginScreen
import com.sopt.clody.presentation.ui.login.navigation.navigateToLogin
import com.sopt.clody.presentation.ui.replydiary.navigation.navigateToReplyDiary
import com.sopt.clody.presentation.ui.replydiary.navigation.replyDiaryScreen
import com.sopt.clody.presentation.ui.replyloading.navigation.navigateToReplyLoading
import com.sopt.clody.presentation.ui.replyloading.navigation.replyLoadingScreen
import com.sopt.clody.presentation.ui.setting.navigation.accountManagementScreen
import com.sopt.clody.presentation.ui.setting.navigation.navigateToAccountManagement
import com.sopt.clody.presentation.ui.setting.navigation.navigateToNotificationSetting
import com.sopt.clody.presentation.ui.setting.navigation.navigateToSetting
import com.sopt.clody.presentation.ui.setting.navigation.navigateToWebView
import com.sopt.clody.presentation.ui.setting.navigation.notificationSettingScreen
import com.sopt.clody.presentation.ui.setting.navigation.settingScreen
import com.sopt.clody.presentation.ui.splash.navigation.splashScreen
import com.sopt.clody.presentation.ui.webview.webViewScreen
import com.sopt.clody.presentation.ui.writediary.navigation.navigateToWriteDiary
import com.sopt.clody.presentation.ui.writediary.navigation.writeDiaryScreen
import com.sopt.clody.presentation.utils.navigation.safePopBackStack

/**
 * 앱 전역의 Navigation Host.
 *
 * 각 기능? 화면?의 navigation graph를 이곳에 연결.
 * [NavHost]를 사용하여 화면 전환을 구성하고,
 * startIntent 등을 통해 초기 경로 조건 처리도 할 수 있음.
 *
 * @param appState 앱 상태를 포함한 네비게이션 컨트롤러
 * @param modifier Compose UI Modifier
 * @param startIntent 외부에서 전달된 인텐트 (푸시 처리 등)
 */
@Composable
fun ClodyNavHost(
    appState: ClodyAppState,
    modifier: Modifier = Modifier,
    startIntent: Intent,
) {
    val navController: NavHostController = appState.navController

    Box(modifier = modifier.fillMaxSize()) {
        NavHost(
            navController = navController,
            startDestination = appState.startDestination,
        ) {
            splashScreen(
                startIntent = startIntent,
                navigateToLogin = navController::navigateToLogin,
                navigateToHome = navController::navigateToHome,
            )

            loginScreen(
                navigateToSignUp = navController::navigateToSignUp,
                navigateToHome = navController::navigateToHome,
            )
            signUpScreen(
                navigateToHome = navController::navigateToTimeReminder,
                navigateToPrevious = navController::safePopBackStack,
                navigateToWebView = navController::navigateToWebView,
            )
            timeReminderScreen(
                navigateToGuide = navController::navigateToGuide,
            )
            guideScreen(
                navigateToHome = navController::navigateToHome,
            )
            homeScreen(
                navigateToDiaryList = navController::navigateToDiaryList,
                navigateToSetting = navController::navigateToSetting,
                navigateToWriteDiary = navController::navigateToWriteDiary,
                navigateToReplyLoading = navController::navigateToReplyLoading,
            )
            diaryListScreen(
                navigateToHome = navController::navigateToHome,
                navigateToReplyLoading = navController::navigateToReplyLoading,
            )
            writeDiaryScreen(
                navigateToReplyLoading = navController::navigateToReplyLoading,
                navigateToHome = navController::navigateToHome,
                navigateToPrevious = navController::safePopBackStack,
            )
            replyLoadingScreen(
                navigateToReplyDiary = navController::navigateToReplyDiary,
                navigateToHome = navController::navigateToHome,
                navigateToDiaryList = navController::navigateToDiaryList,
            )
            replyDiaryScreen(
                navigateToHome = navController::navigateToHome,
            )
            settingScreen(
                navigateToAccountManagement = navController::navigateToAccountManagement,
                navigateToNotification = navController::navigateToNotificationSetting,
                navigateToPrevious = navController::safePopBackStack,
                navigateToWebView = navController::navigateToWebView,
            )
            accountManagementScreen(
                navigateToPrevious = navController::safePopBackStack,
                navigateToLogin = navController::navigateToLogin,
            )
            notificationSettingScreen(
                navigateToPrevious = navController::safePopBackStack,
            )
            webViewScreen(
                navigateToPrevious = navController::safePopBackStack,
            )
        }
    }
}
