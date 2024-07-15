package com.sopt.clody.presentation.ui.home.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sopt.clody.domain.model.ButtonStateData
import com.sopt.clody.presentation.ui.component.button.ClodyButton

@Composable
fun DiaryStateButton(
    diaryCount: Int,
    replyStatus: String,
    onClickWriteDiary: () -> Unit,
    onClickReplyDiary: () -> Unit,
    ) {
    val buttonState = remember(diaryCount, replyStatus) {
        when {
            diaryCount == 0 && replyStatus == "UNREADY" -> {
                ButtonStateData("일기쓰기", true)
            }
            diaryCount != 0 && replyStatus == "UNREADY" -> {
                ButtonStateData("일기쓰기", false)
            }
            diaryCount != 0 && (replyStatus == "READY_NOT_READ" || replyStatus == "READY_READ") -> {
                ButtonStateData("답장확인", true)
            }
            else -> {
                ButtonStateData("답장확인", false)
            }
        }
    }

    ClodyButton(
        onClick = { onClickWriteDiary() },
        text = buttonState.text,
        enabled = buttonState.enabled,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    )
}
