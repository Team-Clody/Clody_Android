package com.sopt.clody.presentation.ui.home.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sopt.clody.presentation.ui.component.button.ClodyButton
import com.sopt.clody.presentation.ui.component.button.ClodyReplyButton

@Composable
fun DiaryStateButton(
    hasDraft: Boolean,
    canWrite: Boolean,
    canReply: Boolean,
    year: Int,
    month: Int,
    day: Int,
    onClickWriteDiary: (Int, Int, Int) -> Unit,
    onClickReplyDiary: () -> Unit,
) {
    val modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp)

    when {
        hasDraft -> {
            ClodyButton(
                onClick = { onClickWriteDiary(year, month, day) },
                text = "이어쓰기",
                enabled = true,
                modifier = modifier,
            )
        }

        canReply -> {
            ClodyReplyButton(
                onClick = onClickReplyDiary,
                text = "답장확인",
                enabled = true,
                modifier = modifier,
            )
        }

        canWrite -> {
            ClodyButton(
                onClick = { onClickWriteDiary(year, month, day) },
                text = "일기쓰기",
                enabled = true,
                modifier = modifier,
            )
        }

        else -> {
            ClodyButton(
                onClick = { onClickWriteDiary(year, month, day) },
                text = "일기쓰기",
                enabled = false,
                modifier = modifier,
            )
        }
    }
}
