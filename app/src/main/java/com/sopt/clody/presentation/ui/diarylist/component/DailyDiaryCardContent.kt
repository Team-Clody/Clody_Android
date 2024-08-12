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
fun DailyDiaryCardContent(
    diary: List<String>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        diary.forEachIndexed { index, content ->
            Row(
                modifier = Modifier.padding(bottom = 14.dp)
            ) {
                Text(
                    text = "${index + 1}.",
                    color = ClodyTheme.colors.gray01,
                    style = ClodyTheme.typography.body3SemiBold
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = content,
                    color = ClodyTheme.colors.gray03,
                    style = ClodyTheme.typography.body3Medium
                )
            }
        }
    }
}
