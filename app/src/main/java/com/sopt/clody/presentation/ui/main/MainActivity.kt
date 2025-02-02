package com.sopt.clody.presentation.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.compose.rememberNavController
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
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
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var appUpdateManager: AppUpdateManager
    private val appUpdateViewModel: AppUpdateViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        checkForAppUpdate()
        setContent {
            CLODYTheme {
                val navController = rememberNavController()

                if (intent.getBooleanExtra("NAVIGATE_TO_LOGIN", false)) {
                    navController.navigate("register_graph") {
                        popUpTo(0) { inclusive = true }
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

    private fun checkForAppUpdate() {
        appUpdateViewModel.checkForAppUpdate()

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                appUpdateViewModel.appUpdateInfo.collect { appUpdateInfo ->
                    appUpdateInfo?.let {
                        if (it.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE &&
                            it.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
                        ) {

                            val appUpdateOptions = AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build()
                            appUpdateManager.startUpdateFlowForResult(
                                appUpdateInfo,
                                this@MainActivity,
                                appUpdateOptions,
                                REQUEST_CODE_UPDATE
                            )
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        appUpdateManager.appUpdateInfo.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                val appUpdateOptions = AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build()
                appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    this,
                    appUpdateOptions,
                    REQUEST_CODE_UPDATE
                )
            }
        }
    }
    companion object {
        const val REQUEST_CODE_UPDATE = 100
    }
}
