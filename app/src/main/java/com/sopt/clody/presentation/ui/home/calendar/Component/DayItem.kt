package com.sopt.clody.presentation.ui.home.calendar.Component

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sopt.clody.R
import com.sopt.clody.domain.model.DiaryData
import com.sopt.clody.ui.theme.ClodyTheme
import kotlinx.datetime.DayOfWeek
import java.time.LocalDate

@Composable
fun DayItem(
    date: LocalDate,
    dayOfWeek: DayOfWeek,
    onDayClick: (LocalDate) -> Unit,
    isSelected: Boolean,
    diaryData: DiaryData,
    modifier: Modifier = Modifier,
) {
    val today = LocalDate.now()
    val isToday = date == today

    val iconRes = when {
        isToday && diaryData.diaryCount == 0 && diaryData.replyStatus == "UNREADY" -> R.drawable.ic_home_today_unwritten_clover
        diaryData.replyStatus == "READY_NOT_READ" && diaryData.diaryCount > 0 -> R.drawable.ic_home_ungiven_clover
        diaryData.replyStatus == "UNREADY" && diaryData.diaryCount > 0 -> R.drawable.ic_home_ungiven_clover
        diaryData.diaryCount == 0 -> R.drawable.ic_home_ungiven_clover
        diaryData.diaryCount == 1 -> R.drawable.ic_home_bottom_clover
        diaryData.diaryCount in 2..3 -> R.drawable.ic_home_mid_clover
        diaryData.diaryCount in 4..5 -> R.drawable.ic_home_top_clover
        else -> R.drawable.ic_home_ungiven_clover
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .padding(8.dp)
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
                        .padding(end = 6.dp, bottom = 8.dp)
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

@Composable
@Preview(showBackground = true)
fun DayItemPreview() {
    DayItem(
        date = LocalDate.now(),
        dayOfWeek = DayOfWeek.SUNDAY,
        onDayClick = {},
        isSelected = false,
        diaryData = DiaryData(diaryCount = 2, replyStatus = "READY_NOT_READ")
    )
}


