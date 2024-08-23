package com.sopt.clody.presentation.ui.home.model

import java.time.YearMonth

data class CalendarDate(
    val date: Int,
    val month: Int,
    val year: Int
)

fun daysInMonth(month: Int, year: Int): Int {
    return YearMonth.of(year, month).lengthOfMonth()
}

fun generateCalendarDates(year: Int, month: Int): List<CalendarDate> {
    val daysInCurrentMonth = daysInMonth(month, year)

    return (1..daysInCurrentMonth).map { day ->
        CalendarDate(day, month, year)
    }
}
