package com.sopt.clody.presentation.ui.home.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sopt.clody.presentation.ui.component.button.ClodyButton
import com.sopt.clody.presentation.ui.component.button.ClodyReplyButton
import java.time.LocalDate

@Composable
fun DiaryStateButton(
    diaryCount: Int,
    isDeleted: Boolean,
    year: Int,
    month: Int,
    day: Int,
    onClickWriteDiary: (Int, Int, Int) -> Unit,
    onClickReplyDiary: () -> Unit,
) {
    val today = LocalDate.now()
    val isAvailableDay = year == today.year && month == today.monthValue && (day == today.dayOfMonth || day == today.dayOfMonth - 1)

    val writeDiaryEnabled = diaryCount == 0 && isAvailableDay
    val writeDiaryDisabled = diaryCount == 0 && !isAvailableDay
    val checkReplyEnabled = diaryCount != 0 && !isDeleted
    val checkReplyDisabled = diaryCount != 0 && isDeleted

    when {
        writeDiaryEnabled -> {
            ClodyButton(
                onClick = { onClickWriteDiary(year, month, day) },
                text = "일기쓰기",
                enabled = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            )
        }

        writeDiaryDisabled -> {
            ClodyButton(
                onClick = { onClickWriteDiary(year, month, day) },
                text = "일기쓰기",
                enabled = false,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            )
        }

        checkReplyEnabled -> {
            ClodyReplyButton(
                onClick = onClickReplyDiary,
                text = "답장확인",
                enabled = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            )
        }

        checkReplyDisabled -> {
            ClodyReplyButton(
                onClick = onClickReplyDiary,
                text = "답장확인",
                enabled = false,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            )
        }
    }
}
