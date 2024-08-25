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
    replyStatus: String,
    isToday: Boolean,
    isDeleted: Boolean,
    year: Int,
    month: Int,
    day: Int,
    onClickWriteDiary: (Int, Int, Int) -> Unit,
    onClickReplyDiary: () -> Unit,
) {
    val today = LocalDate.now()
    val isSelectedDateToday = year == today.year && month == today.monthValue && day == today.dayOfMonth

    when {

        isToday && diaryCount == 0 -> {
            ClodyButton(
                onClick = { onClickWriteDiary(year, month, day) },
                text = "일기쓰기",
                enabled = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
        }

        isDeleted && diaryCount != 0 -> {
            ClodyReplyButton(
                onClick = onClickReplyDiary,
                text = "답장확인",
                enabled = false,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
        }

        diaryCount != 0 && (replyStatus == "UNREADY" || replyStatus == "READY_NOT_READ" || replyStatus == "READY_READ") -> {
            ClodyReplyButton(
                onClick = onClickReplyDiary,
                text = "답장확인",
                enabled = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
        }

        isSelectedDateToday && diaryCount == 0 && replyStatus == "UNREADY" -> {
            ClodyButton(
                onClick = { onClickWriteDiary(year, month, day) },
                text = "일기쓰기",
                enabled = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
        }

        !isSelectedDateToday && diaryCount == 0 && replyStatus == "UNREADY" -> {
            ClodyButton(
                onClick = { onClickWriteDiary(year, month, day) },
                text = "일기쓰기",
                enabled = false,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
        }

        else -> {

        }
    }
}
