package com.sopt.clody.presentation.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.sopt.clody.presentation.ui.auth.navigation.AuthNavigator
import com.sopt.clody.presentation.ui.diarylist.navigator.DiaryListNavigator
import com.sopt.clody.presentation.ui.home.navigator.HomeNavigator
import com.sopt.clody.presentation.ui.navigatior.MainNavHost
import com.sopt.clody.presentation.ui.setting.navigation.SettingNavigator
import com.sopt.clody.ui.theme.CLODYTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CLODYTheme {
                val navController = rememberNavController()
                val authNavigator = remember(navController) { AuthNavigator(navController) }
                val homeNavigator = remember(navController) { HomeNavigator(navController) }
                val diaryListNavigator = remember(navController) { DiaryListNavigator(navController) }
                val settingNavigator = remember(navController) { SettingNavigator(navController) }

                Scaffold(
                    containerColor = MaterialTheme.colorScheme.background,
                    content = { paddingValues ->
                        MainNavHost(
                            modifier = Modifier.padding(paddingValues),
                            authNavigator = authNavigator,
                            homeNavigator = homeNavigator,
                            diaryListNavigator = diaryListNavigator,
                            settingNavigator = settingNavigator
                        )
                    }
                )
            }
        }
    }
}
