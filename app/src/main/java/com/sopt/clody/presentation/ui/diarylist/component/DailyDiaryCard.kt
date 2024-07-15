package com.sopt.clody.presentation.ui.diarylist.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.sopt.clody.R
import com.sopt.clody.presentation.ui.component.bottomsheet.ClodyBottomSheet
import com.sopt.clody.presentation.ui.component.bottomsheet.DiaryDeleteSheet
import com.sopt.clody.presentation.ui.component.dialog.ClodyDialog
import com.sopt.clody.ui.theme.ClodyTheme

@Composable
fun DailyDiaryCard(
    index: Int,
    order: Int,
    onClickReplyDiary: () -> Unit
) {
    var showDiaryDeleteSheet by remember { mutableStateOf(false) }
    var showDiaryDeleteDialog by remember { mutableStateOf(false) }

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
                    painter = painterResource(id = R.drawable.ic_listview_clover),
                    contentDescription = "clover",
                    modifier = Modifier
                        .padding(end = 6.dp)
                )
                Text(
                    text = "26일",
                    modifier = Modifier
                        .padding(end = 2.dp),
                    color = ClodyTheme.colors.gray01,
                    style = ClodyTheme.typography.body1SemiBold
                )
                Text(
                    text = "/목요일",
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
                        onClick = onClickReplyDiary,
                        contentPadding = PaddingValues(0.dp),
                    ) {
                        Text(
                            text = "답장 확인",
                            modifier = Modifier
                                .padding(horizontal = 10.dp, vertical = 2.dp),
                            style = ClodyTheme.typography.detail1SemiBold,
                            color = ClodyTheme.colors.blue
                        )
                    }
                    Image(
                        painter = painterResource(id = R.drawable.ic_reply_diary_new),
                        modifier = Modifier
                            .align(Alignment.TopEnd),
                        contentDescription = null,
                    )
                }
                Image(
                    painter = painterResource(id = R.drawable.ic_listview_kebab_menu),
                    contentDescription = "케밥 메뉴",
                    modifier = Modifier
                        .clickable(onClick = { showDiaryDeleteSheet = true })
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            DailyDiaryCardItem(index)
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
            titleMassage = "정말 일기를 삭제할까요?",
            descriptionMassage = "아직 답장이 오지 않았거나 삭제하고\n다시 작성한 일기는 답장을 받을 수 없어요.",
            confirmOption = "삭제할래요",
            dismissOption = "아니요",
            confirmAction = { /* TODO : 일기 삭제 로직 */ },
            onDismiss = { showDiaryDeleteDialog = false },
            confirmButtonColor = ClodyTheme.colors.red,
            confirmButtonTextColor = ClodyTheme.colors.white
        )
    }
}
