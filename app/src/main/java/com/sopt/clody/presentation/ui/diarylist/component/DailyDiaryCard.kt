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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sopt.clody.R
import com.sopt.clody.data.remote.dto.response.ResponseMonthlyDiaryDto
import com.sopt.clody.presentation.ui.diarylist.screen.DiaryListViewModel
import com.sopt.clody.ui.theme.ClodyTheme

@Composable
fun DailyDiaryCard(
    index: Int,
    diaryListViewModel: DiaryListViewModel,
    dailyDiary: ResponseMonthlyDiaryDto.DailyDiary,
    year: Int,
    month: Int,
    day: Int,
    dayOfWeek: String,
    showDiaryDeleteBottomSheet: () -> Unit,
    onClickReplyDiary: (Int, Int, Int) -> Unit,
) {
    val iconRes = when {
        dailyDiary.replyStatus == "READY_NOT_READ" && dailyDiary.diaryCount > 0 -> R.drawable.ic_home_ungiven_clover
        dailyDiary.replyStatus == "UNREADY" && dailyDiary.diaryCount > 0 -> R.drawable.ic_home_ungiven_clover
        dailyDiary.diaryCount == 0 -> R.drawable.ic_home_ungiven_clover
        dailyDiary.diaryCount in 1..2 -> R.drawable.ic_home_bottom_clover
        dailyDiary.diaryCount in 3..4 -> R.drawable.ic_home_mid_clover
        dailyDiary.diaryCount == 5 -> R.drawable.ic_home_top_clover
        else -> R.drawable.ic_home_ungiven_clover
    }

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
                .padding(vertical = 20.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(start = 20.dp, end = 12.dp, bottom = 18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = iconRes),
                    contentDescription = "clover",
                    modifier = Modifier
                        .padding(end = 6.dp)
                )
                Row(
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = stringResource(R.string.diarylist_daily_diary_day, day),
                        modifier = Modifier
                            .padding(end = 2.dp),
                        color = ClodyTheme.colors.gray01,
                        style = ClodyTheme.typography.body1SemiBold
                    )
                    Text(
                        text = stringResource(R.string.diarylist_daily_diary_day_of_week, dayOfWeek),
                        color = ClodyTheme.colors.gray04,
                        style = ClodyTheme.typography.body2SemiBold
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                ReplyDiaryButton(
                    dailyDiary = dailyDiary,
                    year = year,
                    month = month,
                    day = day,
                    onClickReplyDiary = onClickReplyDiary,
                )
                DiaryDeleteButton(
                    diaryListViewModel = diaryListViewModel,
                    dailyDiary = dailyDiary,
                    showDiaryDeleteBottomSheet = showDiaryDeleteBottomSheet
                )
            }
            DailyDiaryCardContent(dailyDiary.diary.map { it.content })
        }
    }
}

@Composable
fun ReplyDiaryButton(
    dailyDiary: ResponseMonthlyDiaryDto.DailyDiary,
    year: Int,
    month: Int,
    day: Int,
    onClickReplyDiary: (Int, Int, Int) -> Unit,
) {
    Box(
        contentAlignment = Alignment.TopEnd
    ) {
        Button(
            onClick = { onClickReplyDiary(year,month,day) },
            modifier = Modifier
                .height(33.dp)
                .padding(horizontal = 3.dp, vertical = 3.dp),
            enabled = !(dailyDiary.isDeleted),
            colors = ButtonDefaults.buttonColors(
                containerColor = ClodyTheme.colors.lightBlue,
                contentColor = ClodyTheme.colors.blue,
                disabledContainerColor = ClodyTheme.colors.gray08,
                disabledContentColor = ClodyTheme.colors.gray06
            ),
            shape = RoundedCornerShape(size = 9.dp),
            contentPadding = PaddingValues(0.dp),
        ) {
            Text(
                text = stringResource(R.string.diarylist_check_reply),
                modifier = Modifier
                    .padding(horizontal = 10.dp, vertical = 2.dp),
                style = ClodyTheme.typography.detail1SemiBold,
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
}

@Composable
fun DiaryDeleteButton(
    diaryListViewModel: DiaryListViewModel,
    dailyDiary: ResponseMonthlyDiaryDto.DailyDiary,
    showDiaryDeleteBottomSheet: () -> Unit
) {
    Image(
        painter = painterResource(id = R.drawable.ic_listview_kebab_menu),
        contentDescription = "kebab menu",
        modifier = Modifier
            .clickable(
                onClick = {
                    diaryListViewModel.setSelectedDiaryDate(dailyDiary.date)
                    showDiaryDeleteBottomSheet()
                }
            )
    )
}
