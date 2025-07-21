package com.sopt.clody.presentation.utils.extension

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import java.time.LocalDate
import java.time.format.TextStyle

@Composable
fun getDayOfWeek(year: Int, month: Int, day: Int): String {
    val date = LocalDate.of(year, month, day)
    return date.dayOfWeek.getDisplayName(TextStyle.FULL, LocalConfiguration.current.locales[0])
}
