package com.sopt.clody.presentation.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.remember
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
}
