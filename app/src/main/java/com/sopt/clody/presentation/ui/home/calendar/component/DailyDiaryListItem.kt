package com.sopt.clody.presentation.ui.home.calendar.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.sopt.clody.R
import com.sopt.clody.data.remote.dto.response.DailyDiariesResponseDto
import com.sopt.clody.ui.theme.ClodyTheme
import kotlinx.datetime.DayOfWeek
import java.time.LocalDate

@Composable
fun DailyDiaryListItem(
    date: LocalDate,
    dayOfWeek: DayOfWeek,
    dailyDiaries: List<DailyDiariesResponseDto.Diary>,
    onShowDiaryDeleteStateChange: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(
                text = "${date.month.value}.${date.dayOfMonth}",
                style = ClodyTheme.typography.body3Medium,
                color = ClodyTheme.colors.gray04,
                modifier = Modifier.padding(vertical = 3.dp)
            )
            Text(
                text = "${dayOfWeek.toKoreanShortLabel()}요일",
                style = ClodyTheme.typography.body2Medium,
                color = ClodyTheme.colors.gray02,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            Image(
                painter = painterResource(id = R.drawable.ic_home_kebab),
                contentDescription = "go to delete",
                modifier = Modifier.clickable(onClick = { onShowDiaryDeleteStateChange(true) })
            )
        }
        if (dailyDiaries.isEmpty()) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 44.dp)
            ) {
                Text(
                    text = "아직 감사의 일기가 없어요!",
                    style = ClodyTheme.typography.body3Regular,
                    color = ClodyTheme.colors.gray05,
                    textAlign = TextAlign.Center,
                )
            }
        } else {
            dailyDiaries.forEachIndexed { index, diary ->
                DiaryItem(
                    index = index + 1,
                    text = diary.content
                )
            }
        }
    }
}

@Composable
fun DiaryItem(
    index: Int,
    text: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(ClodyTheme.colors.gray08, shape = RoundedCornerShape(10.dp))
            .padding(18.dp)
    ) {
        Text(
            text = "$index. $text",
            style = ClodyTheme.typography.body2Medium,
            color = ClodyTheme.colors.gray01
        )
    }
}
