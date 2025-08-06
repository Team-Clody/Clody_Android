package com.sopt.clody.presentation.ui.home.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sopt.clody.R
import com.sopt.clody.presentation.ui.component.button.ClodyButton
import com.sopt.clody.presentation.ui.component.button.ClodyReplyButton
import com.sopt.clody.ui.theme.ClodyTheme

@Composable
fun DiaryStateButton(
    hasDraft: Boolean,
    canWrite: Boolean,
    canReply: Boolean,
    isInvalidDraft: Boolean,
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
                text = stringResource(R.string.home_btn_continue_draft),
                enabled = true,
                modifier = modifier,
            )
        }

        isInvalidDraft -> {
            ClodyButton(
                onClick = { /* no-action */ },
                text = stringResource(R.string.home_btn_check_reply),
                enabled = false,
                modifier = modifier,
                disabledContainerColor = ClodyTheme.colors.gray05,
                disabledContentColor = ClodyTheme.colors.white,
            )
        }

        canReply -> {
            ClodyReplyButton(
                onClick = onClickReplyDiary,
                text = stringResource(R.string.home_btn_check_reply),
                enabled = true,
                modifier = modifier,
            )
        }

        canWrite -> {
            ClodyButton(
                onClick = { onClickWriteDiary(year, month, day) },
                text = stringResource(R.string.home_btn_write_diary),
                enabled = true,
                modifier = modifier,
            )
        }

        else -> {
            ClodyButton(
                onClick = { onClickWriteDiary(year, month, day) },
                text = stringResource(R.string.home_btn_write_diary),
                enabled = false,
                modifier = modifier,
            )
        }
    }
}
