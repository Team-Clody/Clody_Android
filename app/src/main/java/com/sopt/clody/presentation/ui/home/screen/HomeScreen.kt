package com.sopt.clody.presentation.ui.home.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sopt.clody.presentation.ui.home.calendar.ClodyCalendar
import com.sopt.clody.presentation.ui.home.component.CloverCount
import com.sopt.clody.presentation.ui.home.component.DiaryStateButton
import com.sopt.clody.presentation.ui.home.component.HomeTopAppBar
import com.sopt.clody.presentation.ui.home.navigator.HomeNavigator

@Composable
fun HomeRoute(
    navigator: HomeNavigator
) {
    HomeScreen(
        onClickBack = { navigator.navigateBack() },
        onClickDiaryList = { navigator.navigateDiaryList() },
        onClickSetting = { navigator.navigateSetting() },
        onClickReplyDiary = { navigator.navigateReplyDiary() }
    )
}

@Composable
fun HomeScreen(
    onClickBack: () -> Unit,
    onClickDiaryList: () -> Unit,
    onClickSetting: () -> Unit,
    onClickReplyDiary: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HomeTopAppBar(
            onClickDiaryList = onClickDiaryList,
            onClickSetting = onClickSetting
        )
        ScrollableCalendarView()
    }

    if (showDiaryDeleteState) {
        DiaryDeleteSheet(
            onDismiss = { showDiaryDeleteState = false },
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

@Composable
fun ScrollableCalendarView() {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        CloverCount()
        Spacer(modifier = Modifier.height(20.dp))
        ClodyCalendar()
        Spacer(modifier = Modifier.height(14.dp))
        DiaryStateButton(diaryCount = 5, replyStatus = "READY_NOT_READ")
        Spacer(modifier = Modifier.height(14.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun CalendarScreenPreview() {

}
