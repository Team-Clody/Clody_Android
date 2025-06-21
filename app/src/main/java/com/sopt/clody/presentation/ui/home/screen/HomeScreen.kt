package com.sopt.clody.presentation.ui.home.screen

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sopt.clody.R
import com.sopt.clody.core.review.InAppReviewManager
import com.sopt.clody.data.remote.dto.response.MonthlyCalendarResponseDto
import com.sopt.clody.domain.model.ReplyStatus
import com.sopt.clody.presentation.ui.component.FailureScreen
import com.sopt.clody.presentation.ui.component.LoadingScreen
import com.sopt.clody.presentation.ui.component.bottomsheet.DiaryDeleteSheet
import com.sopt.clody.presentation.ui.component.button.ClodyButton
import com.sopt.clody.presentation.ui.component.dialog.ClodyDialog
import com.sopt.clody.presentation.ui.component.popup.ClodyPopupBottomSheet
import com.sopt.clody.presentation.ui.component.timepicker.YearMonthPicker
import com.sopt.clody.presentation.ui.component.toast.ClodyToastMessage
import com.sopt.clody.presentation.ui.home.calendar.model.DiaryDateData
import com.sopt.clody.presentation.ui.home.component.DiaryStateButton
import com.sopt.clody.presentation.ui.home.component.HomeTopAppBar
import com.sopt.clody.presentation.utils.amplitude.AmplitudeConstraints
import com.sopt.clody.presentation.utils.amplitude.AmplitudeUtils
import com.sopt.clody.presentation.utils.navigation.Route
import com.sopt.clody.ui.theme.ClodyTheme
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.time.LocalDate

@Composable
fun HomeRoute(
    isFromReplyDiary: Boolean,
    navigateToDiaryList: (year: Int, month: Int) -> Unit,
    navigateToSetting: () -> Unit,
    navigateToWriteDiary: (year: Int, month: Int, date: Int) -> Unit,
    navigateToReplyLoading: (
        year: Int,
        month: Int,
        date: Int,
        from: Route.ReplyLoading.ReplyLoadingFrom,
        replyStatus: ReplyStatus,
    ) -> Unit,
    homeViewModel: HomeViewModel = hiltViewModel(),
) {
    val calendarState by homeViewModel.calendarState.collectAsStateWithLifecycle()
    val replyStatus by homeViewModel.replyStatus.collectAsStateWithLifecycle()
    val showFirstDraftPopup by homeViewModel.showFirstDraftPopup.collectAsStateWithLifecycle()
    val draftAlarmEnableToast by homeViewModel.draftAlarmEnableToast.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val showInAppReviewPopup by homeViewModel.showInAppReviewPopup.collectAsStateWithLifecycle()
    val showContinueDraftDialog by homeViewModel.showContinueDraftDialog.collectAsStateWithLifecycle()
    val showDiaryDeleteState by homeViewModel.showDiaryDeleteState.collectAsStateWithLifecycle()
    val showDiaryDeleteDialog by homeViewModel.showDiaryDeleteDialog.collectAsStateWithLifecycle()
    val selectedDiaryDate by homeViewModel.selectedDiaryDate.collectAsStateWithLifecycle()
    val selectedDate by homeViewModel.selectedDate.collectAsStateWithLifecycle()
    val deleteDiaryState by homeViewModel.deleteDiaryState.collectAsStateWithLifecycle()
    val (isError, errorMessage) = homeViewModel.errorState.collectAsStateWithLifecycle().value
    val showYearMonthPickerState by homeViewModel.showYearMonthPickerState.collectAsStateWithLifecycle()
    val hasDraft by homeViewModel.hasDraft.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        AmplitudeUtils.trackEvent(eventName = AmplitudeConstraints.HOME)

        if (showInAppReviewPopup && isFromReplyDiary) {
            InAppReviewManager.showPopup(context as Activity)
            homeViewModel.updateShowInAppReviewPopup(false)
        }
    }

    LaunchedEffect(Unit) {
        val year = selectedDiaryDate.year
        val month = selectedDiaryDate.month
        val day = selectedDate.dayOfMonth

        try {
            coroutineScope {
                val calendarDeferred = async { homeViewModel.loadCalendarData(year, month) }
                val dailyDeferred = async { homeViewModel.loadDailyDiariesData(year, month, day) }

                calendarDeferred.await()
                dailyDeferred.await()
            }
        } catch (e: Exception) {
            homeViewModel.setErrorState(true, "데이터를 불러오는데 실패했습니다.")
        }
    }
    if (isError) {
        FailureScreen(
            message = errorMessage,
            confirmAction = {
                homeViewModel.refreshCalendarDataCalendarData(
                    selectedDiaryDate.year,
                    selectedDiaryDate.month,
                )
                homeViewModel.loadDailyDiariesData(
                    selectedDiaryDate.year,
                    selectedDiaryDate.month,
                    selectedDate.dayOfMonth,
                )
            },
        )
    } else {
        HomeScreen(
            homeViewModel = homeViewModel,
            calendarState = calendarState,
            deleteDiaryState = deleteDiaryState,
            showYearMonthPickerState = showYearMonthPickerState,
            onClickDiaryList = navigateToDiaryList,
            onClickSetting = navigateToSetting,
            onClickWriteDiary = { year, month, day ->
                AmplitudeUtils.trackEvent(eventName = AmplitudeConstraints.HOME_WRITING_DIARY)
                if (hasDraft && !homeViewModel.isValidDraftDate()) {
                    homeViewModel.setShowContinueDraftDialog(true)
                } else {
                    navigateToWriteDiary(year, month, day)
                }
            },
            onClickReplyDiary = { year, month, day, _ ->
                AmplitudeUtils.trackEvent(eventName = AmplitudeConstraints.HOME_REPLY)
                navigateToReplyLoading(
                    year,
                    month,
                    day,
                    Route.ReplyLoading.ReplyLoadingFrom.HOME,
                    replyStatus,
                )
            },
            isError = isError,
            errorMessage = errorMessage,
            selectedYear = selectedDiaryDate.year,
            selectedMonth = selectedDiaryDate.month,
            selectedDate = selectedDate,
            hasDraft = hasDraft,
            canWrite = homeViewModel.canWriteDiary(),
            canReply = homeViewModel.canReplyDiary(),
            isInvalidDraft = replyStatus == ReplyStatus.INVALID_DRAFT,
        )

        if (showFirstDraftPopup) {
            ClodyPopupBottomSheet(
                onDismissRequest = { homeViewModel.updateFirstDraftUse(false) },
                content = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp)
                            .padding(horizontal = 16.dp),
                    ) {
                        Text(
                            text = "기한이 지나면\n로디의 답장을 받을 수 없어요!",
                            color = ClodyTheme.colors.gray01,
                            textAlign = TextAlign.Center,
                            style = ClodyTheme.typography.head3,
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "답장 마감 전에 일기를 이어쓸 수 있도록\n알려드리기 위해서는 알림 설정이 필요해요.",
                            color = ClodyTheme.colors.gray04,
                            textAlign = TextAlign.Center,
                            style = ClodyTheme.typography.body3Regular,
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "[설정 > 애플리케이션 > 클로디 > 알림 > 알림표시]",
                            color = ClodyTheme.colors.gray04,
                            textAlign = TextAlign.Center,
                            style = ClodyTheme.typography.body3Regular,
                        )
                        Spacer(modifier = Modifier.height(28.dp))
                        ClodyButton(
                            text = "알림 받기",
                            onClick = {
                                homeViewModel.enableDraftAlarm(context)
                                homeViewModel.updateFirstDraftUse(false)
                            },
                            enabled = true,
                            modifier = Modifier.fillMaxWidth(),
                        )
                        Text(
                            text = "다음에 하기",
                            modifier = Modifier
                                .clickable(onClick = { homeViewModel.updateFirstDraftUse(false) })
                                .padding(12.dp),
                            color = ClodyTheme.colors.gray05,
                            style = ClodyTheme.typography.body4Medium,
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                },
            )
        }

        if (draftAlarmEnableToast) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.BottomCenter,
                content = {
                    ClodyToastMessage(
                        message = "이어쓰기 알림 설정을 완료했어요.",
                        iconResId = R.drawable.ic_toast_check_on_18,
                        backgroundColor = ClodyTheme.colors.gray04,
                        contentColor = ClodyTheme.colors.white,
                        durationMillis = 3000,
                        onDismiss = { homeViewModel.resetDraftAlarmEnableToast() },
                        modifier = Modifier
                            .navigationBarsPadding()
                            .padding(40.dp),
                    )
                },
            )
        }

        if (showContinueDraftDialog) {
            ClodyDialog(
                titleMassage = "임시저장된 일기를 이어 쓸까요?",
                descriptionMassage = "답장 기한이 지나서 답장은 받을 수 없어요.",
                confirmOption = "이어쓰기",
                dismissOption = "아니오",
                confirmAction = {
                    homeViewModel.setShowContinueDraftDialog(false)
                    val date = homeViewModel.selectedDate.value
                    navigateToWriteDiary(date.year, date.monthValue, date.dayOfMonth)
                },
                onDismiss = {
                    homeViewModel.setShowContinueDraftDialog(false)
                },
                confirmButtonColor = ClodyTheme.colors.mainYellow,
                confirmButtonTextColor = ClodyTheme.colors.gray01,
            )
        }

        if (showDiaryDeleteState) {
            DiaryDeleteSheet(
                onDismiss = { homeViewModel.setShowDiaryDeleteState(false) },
                showDiaryDeleteDialog = {
                    AmplitudeUtils.trackEvent(eventName = AmplitudeConstraints.HOME_DELETE_DIARY)
                    homeViewModel.setShowDiaryDeleteDialog(true)
                },
            )
        }

        if (showDiaryDeleteDialog) {
            ClodyDialog(
                titleMassage = "정말 일기를 삭제할까요?",
                descriptionMassage = "아직 답장이 오지 않았거나 삭제하고\n다시 작성한 일기는 답장을 받을 수 없어요.",
                confirmOption = "삭제할래요",
                dismissOption = "아니요",
                confirmAction = {
                    homeViewModel.deleteDailyDiary(
                        selectedDiaryDate.year,
                        selectedDiaryDate.month,
                        selectedDate.dayOfMonth,
                    )
                    homeViewModel.setShowDiaryDeleteDialog(false)
                },
                onDismiss = {
                    AmplitudeUtils.trackEvent(eventName = AmplitudeConstraints.HOME_NO_DELETE_DIARY)
                    homeViewModel.setShowDiaryDeleteDialog(false)
                },
                confirmButtonColor = ClodyTheme.colors.red,
                confirmButtonTextColor = ClodyTheme.colors.white,
            )
        }
    }
}

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel,
    calendarState: CalendarState<MonthlyCalendarResponseDto>,
    deleteDiaryState: DeleteDiaryState,
    showYearMonthPickerState: Boolean,
    onClickDiaryList: (Int, Int) -> Unit,
    onClickSetting: () -> Unit,
    onClickWriteDiary: (Int, Int, Int) -> Unit,
    onClickReplyDiary: (
        year: Int,
        month: Int,
        date: Int,
        replyStatus: Route.ReplyLoading.ReplyLoadingFrom,
    ) -> Unit,
    isError: Boolean,
    errorMessage: String,
    selectedYear: Int,
    selectedMonth: Int,
    selectedDate: LocalDate,
    hasDraft: Boolean,
    canWrite: Boolean,
    canReply: Boolean,
    isInvalidDraft: Boolean,
) {
    if (isError) {
        FailureScreen(
            message = errorMessage,
            confirmAction = {
                homeViewModel.refreshCalendarDataCalendarData(selectedYear, selectedMonth)
                homeViewModel.loadDailyDiariesData(selectedYear, selectedMonth, selectedDate.dayOfMonth)
            },
        )
    } else {
        var backPressedTime by remember { mutableStateOf(0L) }
        val backPressThreshold = 2000
        val context = LocalContext.current

        BackHandler {
            val currentTime = System.currentTimeMillis()
            if (currentTime - backPressedTime <= backPressThreshold) {
                (context as? Activity)?.finish()
            } else {
                backPressedTime = currentTime
            }
        }

        Scaffold(
            topBar = {
                HomeTopAppBar(
                    onClickDiaryList = {
                        AmplitudeUtils.trackEvent(eventName = AmplitudeConstraints.HOME_LIST_DIARY)
                        onClickDiaryList(selectedYear, selectedMonth)
                    },
                    onClickSetting = onClickSetting,
                    onShowYearMonthPickerStateChange = { newState -> homeViewModel.setShowYearMonthPickerState(newState) },
                    selectedYear = selectedYear,
                    selectedMonth = selectedMonth,
                )
            },
            containerColor = ClodyTheme.colors.white,
            content = { innerPadding ->
                when (calendarState) {
                    is CalendarState.Idle -> {}

                    is CalendarState.Loading -> {
                        LoadingScreen()
                    }

                    is CalendarState.Success -> {
                        ScrollableCalendar(
                            selectedYear = selectedYear,
                            selectedMonth = selectedMonth,
                            cloverCount = calendarState.data.totalCloverCount,
                            diaries = calendarState.data.diaries,
                            homeViewModel = homeViewModel,
                            onShowDiaryDeleteStateChange = { newState -> homeViewModel.setShowDiaryDeleteState(newState) },
                            selectedDate = selectedDate,
                            onDiaryDataUpdated = { _, _ ->
                                homeViewModel.updateDiaryState(calendarState.data.diaries)
                            },
                            modifier = Modifier.padding(innerPadding),
                        )
                    }

                    is CalendarState.Error -> {
                        homeViewModel.setErrorState(true, calendarState.message ?: "알 수 없는 오류가 발생했습니다.")
                    }
                }

                when (deleteDiaryState) {
                    is DeleteDiaryState.Idle -> {}

                    is DeleteDiaryState.Loading -> {
                        LoadingScreen()
                    }

                    is DeleteDiaryState.Success -> {
                    }

                    is DeleteDiaryState.Failure -> {
                        homeViewModel.setErrorState(true, "일기 삭제 중 오류가 발생했습니다.")
                    }
                }
            },
            bottomBar = {
                Column(
                    modifier = Modifier
                        .navigationBarsPadding()
                        .background(ClodyTheme.colors.white),
                ) {
                    Spacer(modifier = Modifier.height(14.dp))
                    DiaryStateButton(
                        hasDraft = hasDraft,
                        canWrite = canWrite,
                        canReply = canReply,
                        isInvalidDraft = isInvalidDraft,
                        year = selectedYear,
                        month = selectedMonth,
                        day = selectedDate.dayOfMonth,
                        onClickWriteDiary = onClickWriteDiary,
                        onClickReplyDiary = {
                            onClickReplyDiary(
                                selectedYear,
                                selectedMonth,
                                selectedDate.dayOfMonth,
                                Route.ReplyLoading.ReplyLoadingFrom.HOME,
                            )
                        },
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                }
            },
        )

        if (showYearMonthPickerState) {
            ClodyPopupBottomSheet(onDismissRequest = { homeViewModel.setShowYearMonthPickerState(false) }) {
                YearMonthPicker(
                    onDismissRequest = { homeViewModel.setShowYearMonthPickerState(false) },
                    selectedYear = selectedYear,
                    selectedMonth = selectedMonth,
                    onYearMonthSelected = { year, month ->
                        homeViewModel.updateSelectedDiaryDate(DiaryDateData(year, month))
                        homeViewModel.loadCalendarData(year, month)
                    },
                )
            }
        }
    }
}
