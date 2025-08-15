package com.sopt.clody.presentation.ui.home.screen

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sopt.clody.R
import com.sopt.clody.core.review.InAppReviewManager
import com.sopt.clody.data.remote.dto.response.DailyDiariesResponseDto
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
import com.sopt.clody.presentation.ui.home.component.DailyStateButton
import com.sopt.clody.presentation.ui.home.component.HomeTopAppBar
import com.sopt.clody.presentation.ui.home.component.MonthlyCalendarAndDailyDiary
import com.sopt.clody.presentation.utils.amplitude.AmplitudeConstraints
import com.sopt.clody.presentation.utils.amplitude.AmplitudeUtils
import com.sopt.clody.presentation.utils.extension.toLocalizedMonthLabel
import com.sopt.clody.presentation.utils.extension.toLocalizedYearLabel
import com.sopt.clody.presentation.utils.navigation.Route
import com.sopt.clody.ui.theme.ClodyTheme
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
    val dailyDiariesState by homeViewModel.dailyDiariesState.collectAsStateWithLifecycle()
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
    var backPressedTime by remember { mutableLongStateOf(0L) }
    val backPressThreshold = 2000

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        homeViewModel.sendNotification(isGranted)
    }

    BackHandler {
        val currentTime = System.currentTimeMillis()
        if (currentTime - backPressedTime <= backPressThreshold) {
            (context as? Activity)?.finish()
        } else {
            backPressedTime = currentTime
        }
    }

    LaunchedEffect(Unit) {
        AmplitudeUtils.trackEvent(eventName = AmplitudeConstraints.HOME)

        if (showInAppReviewPopup && isFromReplyDiary) {
            InAppReviewManager.showPopup(context as Activity)
            homeViewModel.updateShowInAppReviewPopup(false)
        }
    }

    // 알림 권한 요청
    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val notificationPermission = Manifest.permission.POST_NOTIFICATIONS
            if (ContextCompat.checkSelfPermission(context, notificationPermission) != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(notificationPermission)
            } else {
                homeViewModel.sendNotification(true)
            }
        } else {
            homeViewModel.sendNotification(true)
        }
    }

    LaunchedEffect(Unit) {
        homeViewModel.updateYearMonthAndLoadData(
            selectedDiaryDate.year,
            selectedDiaryDate.month,
            selectedDate.dayOfMonth,
        )
    }

    if (isError) {
        FailureScreen(
            message = errorMessage,
            confirmAction = {
                homeViewModel.updateYearMonthAndLoadData(
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
            dailyDiariesState = dailyDiariesState,
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
                            text = stringResource(R.string.bottom_sheet_home_initial_draft_title),
                            color = ClodyTheme.colors.gray01,
                            textAlign = TextAlign.Center,
                            style = ClodyTheme.typography.head3,
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = stringResource(R.string.bottom_sheet_home_initial_draft_description),
                            color = ClodyTheme.colors.gray04,
                            textAlign = TextAlign.Center,
                            style = ClodyTheme.typography.body3Regular,
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = stringResource(R.string.bottom_sheet_home_initial_draft_guide),
                            color = ClodyTheme.colors.gray04,
                            textAlign = TextAlign.Center,
                            style = ClodyTheme.typography.body3Regular,
                        )
                        Spacer(modifier = Modifier.height(28.dp))
                        ClodyButton(
                            text = stringResource(R.string.bottom_sheet_home_initial_draft_accept),
                            onClick = {
                                homeViewModel.enableDraftAlarm()
                                homeViewModel.updateFirstDraftUse(false)
                            },
                            enabled = true,
                            modifier = Modifier.fillMaxWidth(),
                        )
                        Text(
                            text = stringResource(R.string.bottom_sheet_home_initial_draft_skip),
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
                        message = stringResource(R.string.toast_home_draft_alarm_enabled),
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
                titleMassage = stringResource(R.string.dialog_home_continue_draft_title),
                descriptionMassage = stringResource(R.string.dialog_home_continue_draft_description),
                confirmOption = stringResource(R.string.dialog_home_continue_draft_confirm),
                dismissOption = stringResource(R.string.dialog_home_continue_draft_dismiss),
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
                titleMassage = stringResource(R.string.dialog_diary_delete_title),
                descriptionMassage = stringResource(R.string.dialog_diary_delete_description),
                confirmOption = stringResource(R.string.dialog_diary_delete_confirm),
                dismissOption = stringResource(R.string.dialog_diary_delete_dismiss),
                confirmAction = {
                    val d = homeViewModel.selectedDate.value
                    homeViewModel.deleteDailyDiary(
                        d.year,
                        d.monthValue,
                        d.dayOfMonth,
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
    dailyDiariesState: DailyDiariesState<DailyDiariesResponseDto>,
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
    selectedYear: Int,
    selectedMonth: Int,
    selectedDate: LocalDate,
    hasDraft: Boolean,
    canWrite: Boolean,
    canReply: Boolean,
    isInvalidDraft: Boolean,
) {
    Scaffold(
        topBar = {
            HomeTopAppBar(
                onClickDiaryList = {
                    AmplitudeUtils.trackEvent(eventName = AmplitudeConstraints.HOME_LIST_DIARY)
                    onClickDiaryList(selectedYear, selectedMonth)
                },
                onClickSetting = onClickSetting,
                onShowYearMonthPickerStateChange = { newState -> homeViewModel.setShowYearMonthPickerState(newState) },
                selectedYear = selectedYear.toLocalizedYearLabel(),
                selectedMonth = selectedMonth.toLocalizedMonthLabel(),
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
                    MonthlyCalendarAndDailyDiary(
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
                        dailyDiariesState = dailyDiariesState,
                    )
                }

                is CalendarState.Error -> {
                    homeViewModel.setErrorState(true, calendarState.message)
                }
            }

            when (deleteDiaryState) {
                is DeleteDiaryState.Idle -> {}

                is DeleteDiaryState.Loading -> {
                    LoadingScreen()
                }

                is DeleteDiaryState.Success -> {}

                is DeleteDiaryState.Failure -> {
                    homeViewModel.setErrorState(true, stringResource(R.string.home_error_delete_diary))
                }
            }
        },
        bottomBar = {
            DailyStateButton(
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
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .background(ClodyTheme.colors.white)
                    .padding(horizontal = 16.dp, vertical = 14.dp),
            )
        },
    )

    if (showYearMonthPickerState) {
        ClodyPopupBottomSheet(onDismissRequest = { homeViewModel.setShowYearMonthPickerState(false) }) {
            YearMonthPicker(
                onDismissRequest = { homeViewModel.setShowYearMonthPickerState(false) },
                selectedYear = selectedYear,
                selectedMonth = selectedMonth,
                onYearMonthSelected = { year, month ->
                    homeViewModel.updateYearMonthAndLoadData(year, month)
                },
            )
        }
    }
}
