package com.sopt.clody.presentation.ui.diarylist.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sopt.clody.data.remote.dto.response.ResponseMonthlyDiaryDto

@Composable
fun MonthlyDiaryList(
    paddingValues: PaddingValues,
    onClickReplyDiary: (Int, Int, Int) -> Unit,
    diaries: List<ResponseMonthlyDiaryDto.DailyDiary>
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(top = 18.dp)
    ) {
        itemsIndexed(diaries) { index, dailyDiary ->
            DailyDiaryCard(
                index = index,
                dailyDiary = dailyDiary,
                onClickReplyDiary = onClickReplyDiary
            )
        }
    }
}
