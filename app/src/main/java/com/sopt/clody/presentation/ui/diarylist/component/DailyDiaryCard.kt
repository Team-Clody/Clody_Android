package com.sopt.clody.presentation.ui.diarylist.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sopt.clody.R
import com.sopt.clody.data.remote.dto.response.ResponseMonthlyDiaryDto
import com.sopt.clody.presentation.ui.component.bottomsheet.DiaryDeleteSheet
import com.sopt.clody.presentation.ui.component.dialog.ClodyDialog
import com.sopt.clody.ui.theme.ClodyTheme
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun DailyDiaryCard(
    index: Int,
    dailyDiary: ResponseMonthlyDiaryDto.DailyDiary,
    onClickReplyDiary: (Int, Int, Int) -> Unit,
    onDeleteDiary: (Int, Int, Int) -> Unit
) {
    var showDiaryDeleteSheet by remember { mutableStateOf(false) }
    var showDiaryDeleteDialog by remember { mutableStateOf(false) }

    val iconRes = when {
        dailyDiary.replyStatus == "READY_NOT_READ" && dailyDiary.diaryCount > 0 -> R.drawable.ic_home_ungiven_clover
        dailyDiary.replyStatus == "UNREADY" && dailyDiary.diaryCount > 0 -> R.drawable.ic_home_ungiven_clover
        dailyDiary.diaryCount == 0 -> R.drawable.ic_home_ungiven_clover
        dailyDiary.diaryCount in 1..2 -> R.drawable.ic_home_bottom_clover
        dailyDiary.diaryCount in 3..4 -> R.drawable.ic_home_mid_clover
        dailyDiary.diaryCount == 5 -> R.drawable.ic_home_top_clover
        else -> R.drawable.ic_home_ungiven_clover
    }

    val diaryDay = dailyDiary.date.split("-")
    val year = diaryDay[0].toInt()
    val month = diaryDay[1].toInt()
    val day = diaryDay[2].toInt()
    val dayOfWeek = getDayOfWeek(dailyDiary.date)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(bottom = 14.dp)
            .wrapContentSize(align = Alignment.TopStart),
        colors = CardDefaults.cardColors(containerColor = ClodyTheme.colors.white)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 20.dp)
                .padding(bottom = 8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(start = 20.dp, end = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            )
            {
                Image(
                    painter = painterResource(id = iconRes),
                    contentDescription = "clover",
                    modifier = Modifier
                        .padding(end = 6.dp)
                )
                Text(
                    text = "${day}일",
                    modifier = Modifier
                        .padding(end = 2.dp),
                    color = ClodyTheme.colors.gray01,
                    style = ClodyTheme.typography.body1SemiBold
                )
                Text(
                    text = "/${dayOfWeek}",
                    color = ClodyTheme.colors.gray04,
                    style = ClodyTheme.typography.body2SemiBold
                )
                Spacer(modifier = Modifier.weight(1f))
                Box(
                    contentAlignment = Alignment.TopEnd
                ) {
                    Button(
                        modifier = Modifier
                            .height(33.dp)
                            .padding(horizontal = 3.dp, vertical = 3.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ClodyTheme.colors.lightBlue),
                        shape = RoundedCornerShape(size = 9.dp),
                        onClick = { onClickReplyDiary(year, month, day) },
                        contentPadding = PaddingValues(0.dp),
                    ) {
                        Text(
                            text = stringResource(R.string.diarylist_check_reply),
                            modifier = Modifier
                                .padding(horizontal = 10.dp, vertical = 2.dp),
                            style = ClodyTheme.typography.detail1SemiBold,
                            color = ClodyTheme.colors.blue
                        )
                    }
                    if (dailyDiary.replyStatus == "READY_NOT_READ") {
                        Image(
                            painter = painterResource(id = R.drawable.ic_reply_diary_new),
                            modifier = Modifier
                                .align(Alignment.TopEnd),
                            contentDescription = null,
                        )
                    }
                }
                Image(
                    painter = painterResource(id = R.drawable.ic_listview_kebab_menu),
                    contentDescription = "kebab menu",
                    modifier = Modifier
                        .clickable(onClick = { showDiaryDeleteSheet = true })
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            DailyDiaryCardItem(dailyDiary.diary.map { it.content })
        }
    }

    if (showDiaryDeleteSheet) {
        DiaryDeleteSheet(
            onDismiss = { showDiaryDeleteSheet = false },
            onShowDiaryDeleteDialogStateChange = { newState -> showDiaryDeleteDialog = newState }
        )
    }

    if (showDiaryDeleteDialog) {
        ClodyDialog(
            titleMassage = stringResource(R.string.delete_diary_dialog_title),
            descriptionMassage = stringResource(R.string.delete_diary_dialog_description),
            confirmOption = stringResource(R.string.delete_diary_dialog_confirm_option),
            dismissOption = stringResource(R.string.delete_diary_dialog_dismiss_option),
            confirmAction = {
                onDeleteDiary(year, month, day)
                showDiaryDeleteDialog = false
            },
            onDismiss = { showDiaryDeleteDialog = false },
            confirmButtonColor = ClodyTheme.colors.red,
            confirmButtonTextColor = ClodyTheme.colors.white
        )
    }
}

fun getDayOfWeek(dateString: String): String {
    val parts = dateString.split("-")
    val year = parts[0].toInt()
    val month = parts[1].toInt()
    val day = parts[2].toInt()

    return LocalDate.of(year, month, day).dayOfWeek.getDisplayName(TextStyle.FULL, Locale.KOREAN)
}
