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
import com.sopt.clody.presentation.utils.extension.getDayOfWeek

@Composable
fun MonthlyDiaryList(
    paddingValues: PaddingValues,
    diaryListViewModel: DiaryListViewModel,
    diaries: List<ResponseMonthlyDiaryDto.DailyDiary>,
    showDiaryDeleteBottomSheet: () -> Unit,
    onClickReplyDiary: (Int, Int, Int) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(top = 18.dp)
    ) {
        itemsIndexed(items = diaries) { index, dailyDiary ->
            val date = dailyDiary.date.split("-")
            val year = date[0].toInt()
            val month = date[1].toInt()
            val day = date[2].toInt()
            val dayOfWeek = getDayOfWeek(year, month, day)
            DailyDiaryCard(
                index = index,
                diaryListViewModel = diaryListViewModel,
                dailyDiary = dailyDiary,
                year = year,
                month = month,
                day = day,
                dayOfWeek = dayOfWeek,
                showDiaryDeleteBottomSheet = showDiaryDeleteBottomSheet,
                onClickReplyDiary = onClickReplyDiary
            )
        }
    }
}
