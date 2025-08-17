package com.sopt.clody.presentation.ui.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.sopt.clody.R
import com.sopt.clody.data.remote.dto.response.MonthlyCalendarResponseDto
import com.sopt.clody.presentation.ui.type.DailyCloverType
import com.sopt.clody.domain.type.ReplyStatus
import com.sopt.clody.presentation.utils.amplitude.AmplitudeConstraints
import com.sopt.clody.presentation.utils.amplitude.AmplitudeUtils
import com.sopt.clody.ui.theme.ClodyTheme
import kotlinx.datetime.DayOfWeek
import java.time.LocalDate
import java.time.format.TextStyle

@Composable
fun MonthlyCalendar(
    dateList: List<CalendarDate>,
    selectedDate: LocalDate,
    onDayClick: (LocalDate) -> Unit,
    getDiaryDataForDate: (LocalDate) -> MonthlyCalendarResponseDto.Diary?,
) {
    val locale = LocalConfiguration.current.locales[0]
    val days = remember {
        List(7) { i -> DayOfWeek.SUNDAY.plus(i.toLong()) }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
    ) {
        // 요일 헤더 부분
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
        ) {
            days.forEach { week ->
                Box(
                    modifier = Modifier.width((LocalConfiguration.current.screenWidthDp.dp - 40.dp) / 7),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = week.getDisplayName(TextStyle.NARROW, locale),
                        color = ClodyTheme.colors.gray05,
                        style = ClodyTheme.typography.detail1Medium,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }

        // 월별 캘린더의 클로버
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth(),
            ) {
                val firstDate = dateList.firstOrNull()?.let { LocalDate.of(it.year, it.month, it.date) }
                val firstDayOfWeek = firstDate?.dayOfWeek ?: DayOfWeek.SUNDAY
                val emptyDays = (firstDayOfWeek.value % 7)

                val paddedDateList = List(emptyDays) { null } + dateList

                paddedDateList.chunked(7).forEach { weekDates ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
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
                                        DailyClover(
                                            date = localDate,
                                            onDayClick = { clickedDate ->
                                                AmplitudeUtils.trackEvent(AmplitudeConstraints.HOME_CALENDAR_CLOVER)
                                                onDayClick(clickedDate)
                                            },
                                            isSelected = localDate == selectedDate,
                                            diaryData = diaryData,
                                            modifier = Modifier.fillMaxWidth(),
                                        )
                                    }
                                }
                            }
                        }
                        if (weekDates.size < 7) {
                            repeat(7 - weekDates.size) {
                                Box(
                                    modifier = Modifier
                                        .width((LocalConfiguration.current.screenWidthDp.dp - 40.dp) / 7)
                                        .padding(vertical = 2.dp),
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DailyClover(
    date: LocalDate,
    onDayClick: (LocalDate) -> Unit,
    isSelected: Boolean,
    diaryData: MonthlyCalendarResponseDto.Diary,
    modifier: Modifier = Modifier,
) {
    val today = LocalDate.now()
    val isToday = date == today

    val iconRes = DailyCloverType.getCalendarCloverType(diaryData, isToday).iconRes

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .padding(8.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable { onDayClick(date) },
    ) {
        Box(
            modifier = Modifier
                .size(48.dp),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = "Diary clover icon",
            )
            if (diaryData.replyStatus == ReplyStatus.READY_NOT_READ && diaryData.diaryCount > 0) {
                Image(
                    painter = painterResource(id = R.drawable.ic_home_unread_reply),
                    contentDescription = "Unread replies icon",
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 0.dp, bottom = 8.dp)
                        .size(12.dp),
                )
            }
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .background(
                    if (isSelected) Color.Black else Color.Transparent,
                    shape = RoundedCornerShape(12.dp),
                )
                .padding(horizontal = 6.dp),
        ) {
            Text(
                text = date.dayOfMonth.toString(),
                style = ClodyTheme.typography.detail1SemiBold.copy(
                    color = when {
                        isSelected -> ClodyTheme.colors.white
                        isToday -> ClodyTheme.colors.gray02
                        else -> ClodyTheme.colors.gray05
                    },
                ),
                textAlign = TextAlign.Center,
            )
        }
    }
}
