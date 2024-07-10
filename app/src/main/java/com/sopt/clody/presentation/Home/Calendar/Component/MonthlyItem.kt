package com.sopt.clody.presentation.Home.Calendar.Component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sopt.clody.domain.model.CalendarDate
import com.sopt.clody.domain.model.DiaryData
import com.sopt.clody.domain.model.daysInMonth
import com.sopt.clody.domain.model.generateCalendarDates
import java.time.LocalDate
import java.time.YearMonth
import kotlin.random.Random
import com.sopt.clody.R
import kotlin.random.Random.Default.nextInt

@Composable
fun MonthlyItem(
    dateList: List<CalendarDate>,
    selectedDate: LocalDate,
    onDayClick: (LocalDate) -> Unit,
    getDiaryDataForDate: (LocalDate) -> DiaryData?
) {
    val itemWidth = (LocalConfiguration.current.screenWidthDp.dp - 40.dp) / 7
    val replyStatuses = listOf("UNREADY", "READY_NOT_READ", "READY_READ")
    Box(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center,
    ) {
        Column {
            dateList.chunked(7).forEach { weekDates ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    weekDates.forEach { date ->
                        val localDate = LocalDate.of(date.year, date.month, date.date)
                        val diaryData = getDiaryDataForDate(localDate)
                        Box(
                            modifier = Modifier
                                .width(itemWidth)
                                .padding(vertical = 2.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            if (diaryData != null) {
                                DayItem(
                                    date = localDate,
                                    onDayClick = { clickedDate ->
                                        onDayClick(clickedDate)
                                    },
                                    isSelected = localDate == selectedDate,
                                    diaryData = diaryData
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MonthlyItemPreview() {
    val currentMonth = YearMonth.now()
    val selectedDate = LocalDate.now()
    val fakeDiaryData = generateFakeDiaryData(currentMonth.year, currentMonth.monthValue)
    val dateList = generateCalendarDates(currentMonth.year, currentMonth.monthValue)

    MonthlyItem(
        dateList = dateList,
        selectedDate = selectedDate,
        onDayClick = { selectedDate ->

        }
    ) { date ->
        val dayIndex = date.dayOfMonth - 1
        val diaryData = if (dayIndex < fakeDiaryData.size) {
            fakeDiaryData[dayIndex]
        } else {
            null
        }
        diaryData
    }
}

fun generateFakeDiaryData(year: Int, month: Int): List<DiaryData> {
    val daysInMonth = daysInMonth(month, year)
    val random = Random.Default
    val today = LocalDate.now()

    val replyStatuses = listOf("UNREADY", "READY_NOT_READ", "READY_READ")

    return (1..daysInMonth).map { day ->
        val date = LocalDate.of(year, month, day)
        if (date == today) {
            DiaryData(
                diaryCount = 0,
                replyStatus = "UNREADY"
            )
        } else {
            DiaryData(
                diaryCount = random.nextInt(0, 5),
                replyStatus = replyStatuses[random.nextInt(replyStatuses.size)]
            )
        }
    }
}




