package com.sopt.clody.presentation.ui.home.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.sopt.clody.R
import com.sopt.clody.domain.model.CalendarMonthlyInfo
import com.sopt.clody.domain.model.DailyDiaryInfo
import com.sopt.clody.presentation.ui.component.button.ClodyButton
import com.sopt.clody.presentation.ui.component.button.ClodyReplyButton
import com.sopt.clody.presentation.ui.type.DailyStateButtonType
import com.sopt.clody.ui.theme.ClodyTheme
import timber.log.Timber
import java.time.LocalDate

@Composable
fun DailyStateButton(
    calendarDailyInfo: CalendarMonthlyInfo.CalendarDailyInfo,
    selectedDailyInfo: DailyDiaryInfo,
    onClickWriteDiary: () -> Unit,
    onClickContinueDraft: () -> Unit,
    onClickReplyDiary: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val type = DailyStateButtonType.getType(calendarDailyInfo, selectedDailyInfo)

    when (type) {
        DailyStateButtonType.DRAFT_ENABLED -> {
            ClodyButton(
                onClick = onClickContinueDraft,
                text = stringResource(R.string.home_btn_continue_draft),
                enabled = true,
                modifier = modifier,
            )
        }

        DailyStateButtonType.REPLY_DISABLED -> {
            ClodyButton(
                onClick = { /* no-action */ },
                text = stringResource(R.string.home_btn_check_reply),
                enabled = false,
                modifier = modifier,
                disabledContainerColor = ClodyTheme.colors.gray05,
                disabledContentColor = ClodyTheme.colors.white,
            )
        }

        DailyStateButtonType.REPLY_ENABLED -> {
            ClodyReplyButton(
                onClick = onClickReplyDiary,
                text = stringResource(R.string.home_btn_check_reply),
                enabled = true,
                modifier = modifier,
            )
        }

        DailyStateButtonType.DIARY_ENABLED -> {
            ClodyButton(
                onClick = onClickWriteDiary,
                text = stringResource(R.string.home_btn_write_diary),
                enabled = true,
                modifier = modifier,
            )
        }

        else -> {
            ClodyButton(
                onClick = { /* no-action */ },
                text = stringResource(R.string.home_btn_write_diary),
                enabled = false,
                modifier = modifier,
            )
        }
    }
}
