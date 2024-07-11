package com.sopt.clody.domain.model

import java.time.LocalDate
import java.time.YearMonth

data class CalendarDate(
    val date: Int,
    val month: Int,
    val year: Int
)

data class DiaryData(
    val diaryCount: Int,
    val replyStatus: String,
)

fun daysInMonth(month: Int, year: Int): Int {
    return YearMonth.of(year, month).lengthOfMonth()
}

fun generateCalendarDates(year: Int, month: Int): List<CalendarDate> {
    val firstDayOfMonth = LocalDate.of(year, month, 1)
    val daysInCurrentMonth = daysInMonth(month, year)

    return (1..daysInCurrentMonth).map { day ->
        CalendarDate(day, month, year)
    }
}
