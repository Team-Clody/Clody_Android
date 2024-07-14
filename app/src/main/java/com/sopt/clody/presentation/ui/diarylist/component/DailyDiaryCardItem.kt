package com.sopt.clody.presentation.ui.diarylist.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sopt.clody.ui.theme.ClodyTheme

@Composable
fun DailyDiaryCardItem(index: Int) {
    val diaryList = (1..(index % 5 + 1)).map { i -> "마지막 세미나에 참석할 수 있어 감사해,마지막 세미나에 참석할 수 있어 감사해0000000$i" }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        diaryList.forEachIndexed { idx, entry ->
            Row(
                modifier = Modifier.padding(bottom = 14.dp)
            ) {
                Text(
                    text = "${idx + 1}.",
                    color = ClodyTheme.colors.gray01,
                    style = ClodyTheme.typography.body3SemiBold
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = entry,
                    color = ClodyTheme.colors.gray03,
                    style = ClodyTheme.typography.body3Medium
                )
            }
        }
    }
}
