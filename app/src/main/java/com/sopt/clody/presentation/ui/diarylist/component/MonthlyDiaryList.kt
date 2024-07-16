package com.sopt.clody.presentation.ui.diarylist.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MonthlyDiaryList(
    paddingValues: PaddingValues,
    onClickReplyDiary: () -> Unit,
    selectedYear: Int,
    selectedMonth: Int,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(top = 18.dp)
    ) {
        itemsIndexed(
            listOf(100, 200, 300, 400, 500, 600, 700, 800, 900, 1000)
        ) { index, item ->
            DailyDiaryCard(index = index, order = item, onClickReplyDiary = onClickReplyDiary)
        }
    }
}
