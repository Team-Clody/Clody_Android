package com.sopt.clody.presentation.ui.home.calendar.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.sopt.clody.R
import com.sopt.clody.ui.theme.ClodyTheme
import kotlinx.datetime.DayOfWeek

@Composable
fun DailyDiaryListItem(
    date: java.time.LocalDate,
    dayOfWeek: DayOfWeek,
    diaryTexts: List<String>,
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
                modifier = Modifier
                    .clickable(onClick = {onShowDiaryDeleteStateChange(true)} )
            )
        }
        diaryTexts.forEachIndexed { index, text ->
            DiaryItem(
                index = index + 1,
                text = text
            )
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


