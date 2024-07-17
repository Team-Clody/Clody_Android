package com.sopt.clody.presentation.ui.home.component

import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sopt.clody.domain.model.ButtonStateData
import com.sopt.clody.presentation.ui.component.button.ClodyButton
import com.sopt.clody.presentation.ui.component.button.ClodyReplyButton

@Composable
fun DiaryStateButton(
    diaryCount: Int,
    replyStatus: String,
    isToday: Boolean,
    onClickWriteDiary: () -> Unit,
    onClickReplyDiary: () -> Unit,
) {

    when {
        !isToday && diaryCount == 0 && replyStatus == "UNREADY" -> {
            ClodyButton(
                onClick = onClickWriteDiary,
                text = "일기쓰기",
                enabled = false,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
        }
        isToday && diaryCount == 0 && replyStatus == "UNREADY" -> {
            ClodyButton(
                onClick = onClickWriteDiary,
                text = "일기쓰기",
                enabled = true,
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

        else -> {

        }
    }

}
