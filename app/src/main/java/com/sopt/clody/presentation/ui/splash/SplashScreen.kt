package com.sopt.clody.presentation.ui.splash

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sopt.clody.R
import com.sopt.clody.domain.model.AppUpdateState
import com.sopt.clody.presentation.utils.amplitude.AmplitudeConstraints
import com.sopt.clody.presentation.utils.amplitude.AmplitudeUtils
import com.sopt.clody.presentation.utils.appupdate.AppUpdateUtils
import com.sopt.clody.ui.theme.ClodyTheme
import kotlinx.coroutines.delay

@Composable
fun SplashRoute(
    startIntent: Intent,
    onLoginRequired: () -> Unit,
    onAlreadyLoggedIn: () -> Unit,
    viewModel: SplashViewModel = hiltViewModel(),
) {
    val isUserLoggedIn by viewModel.isUserLoggedIn.collectAsStateWithLifecycle()
    val updateState by viewModel.updateState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val activity = context as Activity

    // Push 클릭 추적
    LaunchedEffect(startIntent) {
        if (startIntent.hasExtra("google.message_id")) {
            AmplitudeUtils.trackEvent(AmplitudeConstraints.ALARM)
        }
    }

    LaunchedEffect(isUserLoggedIn, updateState) {
        if (isUserLoggedIn != null && updateState == AppUpdateState.Latest) {
            delay(1000)
            if (isUserLoggedIn == true) {
                onAlreadyLoggedIn()
            } else {
                onLoginRequired()
            }
        }
    }

    when (val state = updateState) {
        is AppUpdateState.SoftUpdate -> {
            SoftUpdateDialog(
                latestVersion = state.latestVersion,
                onDismiss = { viewModel.clearUpdateState() },
                onConfirm = { AppUpdateUtils.navigateToMarket(context) },
            )
        }

        is AppUpdateState.HardUpdate -> {
            HardUpdateDialog(
                latestVersion = state.latestVersion,
                onConfirm = { AppUpdateUtils.navigateToMarketAndFinish(activity) },
                onExit = { activity.finishAffinity() },
            )
        }

        else -> {}
    }

    SplashScreen()
}

@Composable
fun SplashScreen() {
    val backgroundColor = ClodyTheme.colors.mainYellow
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(id = R.drawable.img_splash_logo),
            contentDescription = "App Logo",
            modifier = Modifier.size(160.dp),
        )
    }
}

@Composable
fun SoftUpdateDialog(
    latestVersion: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("업데이트 필요") },
        text = {
            Text(
                text = "새로운 버전 ${latestVersion}을 사용할 수 있습니다.\n지금 업데이트하시겠습니까?",
                textAlign = TextAlign.Center,
            )
        },
        confirmButton = { TextButton(onClick = onConfirm) { Text("업데이트") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("나중에") } },
    )
}

@Composable
fun HardUpdateDialog(
    latestVersion: String,
    onConfirm: () -> Unit,
    onExit: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = {},
        title = { Text("필수 업데이트") },
        text = { Text("버전 ${latestVersion}으로 업데이트가 필요합니다.") },
        confirmButton = { TextButton(onClick = onConfirm) { Text("업데이트") } },
        dismissButton = { TextButton(onClick = onExit) { Text("앱 종료") } },
    )
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    SplashScreen()
}
