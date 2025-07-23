package com.sopt.clody.presentation.utils.extension

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import java.time.LocalDate
import java.time.format.TextStyle

@Composable
fun getDayOfWeek(year: Int, month: Int, day: Int): String {
    val date = LocalDate.of(year, month, day)
    val locale = LocalConfiguration.current.locales.let { if (it.isEmpty) java.util.Locale.getDefault() else it[0] }
    return date.dayOfWeek.getDisplayName(TextStyle.FULL, locale)
}
