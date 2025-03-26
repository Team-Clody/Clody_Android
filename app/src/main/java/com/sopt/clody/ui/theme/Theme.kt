package com.sopt.clody.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable

@Composable
fun CLODYTheme(
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(content = content)
}

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
