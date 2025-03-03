package com.sopt.clody.presentation.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.sopt.clody.presentation.ui.auth.navigation.AuthNavigator
import com.sopt.clody.presentation.ui.diarylist.navigation.DiaryListNavigator
import com.sopt.clody.presentation.ui.home.navigation.HomeNavigator
import com.sopt.clody.presentation.ui.navigatior.MainNavHost
import com.sopt.clody.presentation.ui.replydiary.navigation.ReplyDiaryNavigator
import com.sopt.clody.presentation.ui.replyloading.navigation.ReplyLoadingNavigator
import com.sopt.clody.presentation.ui.setting.navigation.SettingNavigator
import com.sopt.clody.presentation.ui.writediary.navigation.WriteDiaryNavigator
import com.sopt.clody.presentation.utils.amplitude.AmplitudeConstraints
import com.sopt.clody.presentation.utils.amplitude.AmplitudeUtils
import com.sopt.clody.ui.theme.CLODYTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContent {
            CLODYTheme {
                val navController = rememberNavController()
                val currentIntent by rememberUpdatedState(newValue = intent)
                var processedIntent by remember { mutableStateOf<Intent?>(null) }

                LaunchedEffect(key1 = currentIntent) {
                    if (processedIntent != currentIntent) {
                        handleIntent(currentIntent)
                        processedIntent = currentIntent
                    }
                }

                SideEffect {
                    if (processedIntent != intent) {
                        handleIntent(intent)
                        processedIntent = intent
                    }
                }

                LaunchedEffect(key1 = intent.getBooleanExtra("NAVIGATE_TO_LOGIN", false)) {
                    if (intent.getBooleanExtra("NAVIGATE_TO_LOGIN", false)) {
                        navController.navigate("register_graph") {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                }

                val authNavigator = remember(navController) { AuthNavigator(navController) }
                val homeNavigator = remember(navController) { HomeNavigator(navController) }
                val diaryListNavigator = remember(navController) { DiaryListNavigator(navController) }
                val writeDiaryNavigator = remember(navController) { WriteDiaryNavigator(navController) }
                val settingNavigator = remember(navController) { SettingNavigator(navController) }
                val replyLoadingNavigator = remember(navController) { ReplyLoadingNavigator(navController) }
                val replyDiaryNavigator = remember(navController) { ReplyDiaryNavigator(navController) }

                Scaffold(
                    containerColor = MaterialTheme.colorScheme.background,
                    content = { paddingValues ->
                        MainNavHost(
                            modifier = Modifier.padding(paddingValues),
                            navController = navController,
                            authNavigator = authNavigator,
                            homeNavigator = homeNavigator,
                            diaryListNavigator = diaryListNavigator,
                            writeDiaryNavigator = writeDiaryNavigator,
                            settingNavigator = settingNavigator,
                            replyLoadingNavigator = replyLoadingNavigator,
                            replyDiaryNavigator = replyDiaryNavigator
                        )
                    }
                )
            }
        }
    }

    private fun handleIntent(intent: Intent?) {
        if (intent?.extras?.containsKey("google.message_id") == true) {
            logPushClickEvent()
        }
    }

    private fun logPushClickEvent() {
        AmplitudeUtils.trackEvent(eventName = AmplitudeConstraints.ALARM)
    }
}
