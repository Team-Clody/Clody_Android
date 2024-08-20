package com.sopt.clody.presentation.ui.home.calendar.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.sopt.clody.data.remote.dto.response.MonthlyCalendarResponseDto
import com.sopt.clody.presentation.ui.home.model.CalendarDate
import kotlinx.datetime.DayOfWeek
import java.time.LocalDate

@Composable
fun MonthlyItem(
    dateList: List<CalendarDate>,
    selectedDate: LocalDate,
    onDayClick: (LocalDate) -> Unit,
    getDiaryDataForDate: (LocalDate) -> MonthlyCalendarResponseDto.Diary?
) {
    val itemWidth = (LocalConfiguration.current.screenWidthDp.dp - 40.dp) / 7

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
    ) {
        WeekHeader(itemWidth = itemWidth)
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                val firstDate = dateList.firstOrNull()?.let { LocalDate.of(it.year, it.month, it.date) }
                val firstDayOfWeek = firstDate?.dayOfWeek ?: DayOfWeek.SUNDAY
                val emptyDays = (firstDayOfWeek.value % 7)

                val paddedDateList = List(emptyDays) { null } + dateList

                paddedDateList.chunked(7).forEach { weekDates ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        weekDates.forEach { date ->
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(vertical = 2.dp),
                                contentAlignment = Alignment.Center,
                            ) {
                                if (date != null) {
                                    val localDate = LocalDate.of(date.year, date.month, date.date)
                                    val diaryData = getDiaryDataForDate(localDate)
                                    if (diaryData != null) {
                                        DayItem(
                                            date = localDate,
                                            dayOfWeek = localDate.dayOfWeek,
                                            onDayClick = { clickedDate ->
                                                onDayClick(clickedDate)
                                            },
                                            isSelected = localDate == selectedDate,
                                            diaryData = diaryData,
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    }
                                }
                            }
                        }
                        if (weekDates.size < 7) {
                            repeat(7 - weekDates.size) {
                                Box(
                                    modifier = Modifier
                                        .width(itemWidth)
                                        .padding(vertical = 2.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
