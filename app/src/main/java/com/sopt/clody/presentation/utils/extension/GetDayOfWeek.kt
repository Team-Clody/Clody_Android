package com.sopt.clody.presentation.utils.extension

import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

fun getDayOfWeek(year: Int, month: Int, day: Int): String {
    val date = LocalDate.of(year, month, day)
    return date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.KOREAN)
}
