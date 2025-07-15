package com.sopt.clody.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.platform.LocalConfiguration

@Composable
fun provideTypographyByLocale(): ClodyTypography {
    val locale = LocalConfiguration.current.locales[0]
    return if (locale.language == "ko") clodyKoreanTypography else clodyEnglishTypography
}

@Composable
fun ClodyTheme(
    content: @Composable () -> Unit,
) {
    val colors = defaultClodyColors
    val typography = provideTypographyByLocale()

    CompositionLocalProvider(
        LocalClodyColors provides colors,
        LocalClodyTypography provides typography,
        content = content,
    )
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
