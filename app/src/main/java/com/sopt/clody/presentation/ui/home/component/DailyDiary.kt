package com.sopt.clody.presentation.ui.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.sopt.clody.R
import com.sopt.clody.domain.model.DailyDiaryInfo
import com.sopt.clody.ui.theme.ClodyTheme
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun DailyDiary(
    selectedDate: LocalDate,
    selectedDailyInfo: DailyDiaryInfo,
    onClickDiaryDelete: () -> Unit,
) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
        ) {
            Text(
                text = "${selectedDate.monthValue}.${selectedDate.dayOfMonth}",
                style = ClodyTheme.typography.body2Medium,
                color = ClodyTheme.colors.gray04,
                modifier = Modifier.padding(vertical = 3.dp),
            )
            Text(
                text = selectedDate.dayOfWeek.getDisplayName(
                    TextStyle.FULL,
                    LocalConfiguration.current.locales.let { if (it.isEmpty) Locale.getDefault() else it[0] },
                ),
                style = ClodyTheme.typography.body2SemiBold,
                color = ClodyTheme.colors.gray02,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
            )
            Spacer(modifier = Modifier.weight(1f))
            if (selectedDailyInfo.diaryList.isNotEmpty()) {
                Image(
                    painter = painterResource(id = R.drawable.ic_home_kebab),
                    contentDescription = "go to delete",
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .clickable(onClick = onClickDiaryDelete),
                )
            }
        }

        when {
            selectedDailyInfo.isDraft -> {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 44.dp),
                ) {
                    Text(
                        text = stringResource(R.string.home_daily_diary_draft_message),
                        style = ClodyTheme.typography.body3Regular,
                        color = ClodyTheme.colors.gray05,
                        textAlign = TextAlign.Center,
                    )
                }
            }

            selectedDailyInfo.diaryList.isEmpty() -> {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 44.dp),
                ) {
                    Text(
                        text = stringResource(R.string.home_daily_diary_empty_message),
                        style = ClodyTheme.typography.body3Regular,
                        color = ClodyTheme.colors.gray05,
                        textAlign = TextAlign.Center,
                    )
                }
            }

            else -> {
                selectedDailyInfo.diaryList.forEachIndexed { index, diary ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(ClodyTheme.colors.gray08, shape = RoundedCornerShape(10.dp))
                            .padding(18.dp),
                    ) {
                        Text(
                            text = "${index + 1}. $diary",
                            style = ClodyTheme.typography.body2Medium,
                            color = ClodyTheme.colors.gray01,
                        )
                    }
                }
            }
        }
    }
}
