package com.sopt.clody.presentation.ui.home.calendar.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.sopt.clody.R
import com.sopt.clody.data.remote.dto.response.MonthlyCalendarResponseDto
import com.sopt.clody.domain.model.DiaryCloverType
import com.sopt.clody.ui.theme.ClodyTheme
import kotlinx.datetime.DayOfWeek
import java.time.LocalDate

@Composable
fun DayItem(
    date: LocalDate,
    dayOfWeek: DayOfWeek,
    onDayClick: (LocalDate) -> Unit,
    isSelected: Boolean,
    diaryData: MonthlyCalendarResponseDto.Diary,
    modifier: Modifier = Modifier,
) {
    val today = LocalDate.now()
    val isToday = date == today

    val iconRes = DiaryCloverType.getCalendarCloverType(diaryData, isToday).iconRes

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .padding(8.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable { onDayClick(date) }
    ) {
        Box(
            modifier = Modifier
                .size(48.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = "Diary clover icon"
            )
            if (diaryData.replyStatus == "READY_NOT_READ" && diaryData.diaryCount > 0) {
                Image(
                    painter = painterResource(id = R.drawable.ic_home_unread_reply),
                    contentDescription = "Unread replies icon",
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 0.dp, bottom = 8.dp)
                        .size(12.dp)
                )
            }
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .background(
                    if (isSelected) Color.Black else Color.Transparent,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(horizontal = 6.dp)
        ) {
            Text(
                text = date.dayOfMonth.toString(),
                style = ClodyTheme.typography.detail1SemiBold.copy
                    (
                    color = when {
                        isSelected -> ClodyTheme.colors.white
                        isToday -> ClodyTheme.colors.gray02
                        else -> ClodyTheme.colors.gray05
                    }
                ),
                textAlign = TextAlign.Center
            )
        }
    }
}


