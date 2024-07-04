package com.sopt.clody.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

object ClodyTheme {
    val colors: ClodyColors
        @Composable
        @ReadOnlyComposable
        get() = LocalClodyColors.current

    val typography: ClodyTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalClodyTypography.current
}

@Composable
fun provideClodyColorsAndTypography(
    colors: ClodyColors,
    typography: ClodyTypography,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalClodyColors provides colors,
        LocalClodyTypography provides typography,
        content = content,
    )
}

@Composable
fun CLODYTheme(content: @Composable () -> Unit) {
    val colors = defaultClodyColors
    val typography = defaultClodyTypography

    provideClodyColorsAndTypography(colors, typography) {
        val view = LocalView.current
        if (!view.isInEditMode) {
            // SideEffect를 사용하여 상태 표시줄의 색상을 설정
            SideEffect {
                val window = (view.context as Activity).window
                window.statusBarColor = Color.White.toArgb() // 상태 표시줄 색상을 흰색으로 설정
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true // 상태 표시줄 아이콘 색상을 항상 검정색으로 설정
            }
        }
        MaterialTheme(content = content)
    }
}
