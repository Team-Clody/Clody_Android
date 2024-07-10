package com.sopt.clody.presentation.Home.Calendar.Component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sopt.clody.R
import com.sopt.clody.ui.theme.ClodyTheme

@Composable
fun DailyDiaryListItem(
    diaryTexts: List<String>
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "6월 26일",
                    style = ClodyTheme.typography.body3Medium,
                    color = ClodyTheme.colors.gray04,
                    modifier = Modifier.padding(vertical = 3.dp)
                )
                Text(
                    text = "목요일",
                    style = ClodyTheme.typography.body2Medium,
                    color = ClodyTheme.colors.gray02,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
                Image(
                    painter = painterResource(id = R.drawable.ic_home_kebab),
                    contentDescription = "go to delete",
                    modifier = Modifier
                )
            }
        }

        itemsIndexed(diaryTexts) { index, text ->
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
            .background(ClodyTheme.colors.gray08, shape = RoundedCornerShape(10.dp)) // 배경과 모서리 둥글게
            .padding(18.dp)
    ) {
        Text(
            text = "$index. $text",
            style = ClodyTheme.typography.body2Medium,
            color = ClodyTheme.colors.gray01
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DailyDiaryListItemPreview() {
    val sampleTexts = listOf(
        "첫 번째 다이어리 내용",
        "두 번째 다이어리 내용",
        "세 번째 다이어리 내용"
    )
    DailyDiaryListItem(diaryTexts = sampleTexts)
}


