package com.sopt.clody.presentation.ui.main

import android.content.Intent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.sopt.clody.presentation.utils.amplitude.AmplitudeConstraints
import com.sopt.clody.presentation.utils.amplitude.AmplitudeUtils

/**
 * 루트 Composable 함수.
 * 앱 전반의 테마와 시스템 UI 설정, Scaffold 레이아웃을 관리하고
 * [ClodyNavHost]를 통해 실제 네비게이션 경로를 구성.
 */
@Composable
fun ClodyApp(
    appState: ClodyAppState,
    startIntent: Intent,
) {
    LaunchedEffect(startIntent) {
        if (startIntent.hasExtra("google.message_id")) {
            AmplitudeUtils.trackEvent(AmplitudeConstraints.ALARM)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        ClodyNavHost(
            appState = appState,
            startIntent = startIntent,
        )
    }
}
