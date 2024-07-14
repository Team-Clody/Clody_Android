package com.sopt.clody.presentation.ui.writediary.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.sopt.clody.ui.theme.ClodyTheme

@Composable
fun DiaryTitleText(date: String, separator: String, day: String, modifier: Modifier = Modifier) {


    Text(
        text = "6월 26일 목요일",
        modifier = modifier.fillMaxWidth(),
        style = ClodyTheme.typography.head2,
        color = ClodyTheme.colors.gray01
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewDiaryTitleText() {
    DiaryTitleText(date = "6월 26일", separator = " / ", day = "목요일")
}


