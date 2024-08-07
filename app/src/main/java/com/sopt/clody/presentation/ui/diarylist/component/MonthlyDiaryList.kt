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
import com.sopt.clody.presentation.ui.diarylist.screen.DiaryListViewModel

@Composable
fun MonthlyDiaryList(
    paddingValues: PaddingValues,
    diaryListViewModel: DiaryListViewModel,
    diaries: List<ResponseMonthlyDiaryDto.DailyDiary>,
    showDiaryDeleteBottomSheet: () -> Unit,
    onClickReplyDiary: () -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(top = 18.dp)
    ) {
        itemsIndexed(items = diaries) { index, dailyDiary ->
            DailyDiaryCard(
                index = index,
                diaryListViewModel = diaryListViewModel,
                dailyDiary = dailyDiary,
                showDiaryDeleteBottomSheet = showDiaryDeleteBottomSheet,
                onClickReplyDiary = onClickReplyDiary
            )
        }
    }
}
