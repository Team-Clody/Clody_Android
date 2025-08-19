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
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.sopt.clody.R
import com.sopt.clody.core.review.InAppReviewManager
import com.sopt.clody.domain.type.ReplyStatus
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
import com.sopt.clody.presentation.utils.extension.repeatOnStarted
import com.sopt.clody.presentation.utils.extension.toLocalizedMonthLabel
import com.sopt.clody.presentation.utils.extension.toLocalizedYearLabel
import com.sopt.clody.presentation.utils.navigation.Route
import com.sopt.clody.ui.theme.ClodyTheme

@Composable
fun HomeRoute(
    viewModel: HomeViewModel = mavericksViewModel(),
    isFromReplyDiary: Boolean,
    navigateToDiaryList: (year: Int, month: Int) -> Unit,
    navigateToSetting: () -> Unit,
    navigateToWriteDiary: (year: Int, month: Int, date: Int) -> Unit,
    navigateToReplyLoading: (year: Int, month: Int, date: Int, from: Route.ReplyLoading.ReplyLoadingFrom, replyStatus: ReplyStatus) -> Unit,
) {
    val state by viewModel.collectAsState()
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(viewModel) {
        lifecycleOwner.repeatOnStarted {
            viewModel.sideEffects.collect { effect ->
                when (effect) {
                    is HomeContract.HomeSideEffect.NavigateToDiaryList -> navigateToDiaryList(state.year, state.month)
                    is HomeContract.HomeSideEffect.NavigateToSetting -> navigateToSetting()
                    is HomeContract.HomeSideEffect.NavigateToWriteDiary -> navigateToWriteDiary(state.year, state.month, state.dayOfMonth)
                    is HomeContract.HomeSideEffect.NavigateToReplyLoading -> {
                        val replyStatus = state.calendarMonthlyInfo.calendarDailyInfoList
                            .find { it.date == java.time.LocalDate.of(state.year, state.month, state.dayOfMonth).toString() }
                            ?.replyStatus ?: ReplyStatus.UNREADY
                        navigateToReplyLoading(
                            state.year,
                            state.month,
                            state.dayOfMonth,
                            Route.ReplyLoading.ReplyLoadingFrom.HOME,
                            replyStatus,
                        )
                    }
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        // 먼저 월간 캘린더 데이터를 로드
        viewModel.postIntent(HomeContract.HomeIntent.LoadCalendarMonthlyInfo(state.year, state.month))
        // 월간 데이터 로드 완료 후 일간 데이터 로드 (ViewModel에서 순서 보장)
    }

    // 백핸들러 조작
    var backPressedTime by remember { mutableLongStateOf(0L) }
    val backPressThreshold = 2000
    BackHandler {
        val currentTime = System.currentTimeMillis()
        if (currentTime - backPressedTime <= backPressThreshold) {
            (context as? Activity)?.finish()
        } else {
            backPressedTime = currentTime
        }
    }

    // 알림 권한 신청 다이얼로그
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        viewModel.postIntent(HomeContract.HomeIntent.SendNotification(isGranted))
    }
    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val notificationPermission = Manifest.permission.POST_NOTIFICATIONS
            if (ContextCompat.checkSelfPermission(context, notificationPermission) != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(notificationPermission)
            } else {
                viewModel.postIntent(HomeContract.HomeIntent.SendNotification(true))
            }
        } else {
            viewModel.postIntent(HomeContract.HomeIntent.SendNotification(true))
        }
    }

    // 인앱리뷰 팝업 노출
    LaunchedEffect(Unit) {
        AmplitudeUtils.trackEvent(eventName = AmplitudeConstraints.HOME)
        if (state.showInAppReviewPopup && isFromReplyDiary) {
            InAppReviewManager.showPopup(context as Activity)
            viewModel.postIntent(HomeContract.HomeIntent.UpdateInAppReview(false))
        }
    }

    when (state.homeUiState) {
        HomeUiState.Idle -> {
        }
        HomeUiState.Loading -> {
            LoadingScreen()
        }
        HomeUiState.Success -> {
            HomeScreen(
                state = state,
                onIntent = { viewModel.postIntent(it) },
            )
        }
        HomeUiState.Error -> {
            FailureScreen(
                message = state.errorMessage ?: stringResource(R.string.error_unknown),
                confirmAction = { viewModel.postIntent(HomeContract.HomeIntent.LoadCalendarMonthlyInfo(state.year, state.month)) },
            )
        }
    }
}

@Composable
fun HomeScreen(
    state: HomeContract.HomeState,
    onIntent: (HomeContract.HomeIntent) -> Unit,
) {
    Scaffold(
        topBar = {
            HomeTopAppBar(
                selectedYear = state.year.toLocalizedYearLabel(),
                selectedMonth = state.month.toLocalizedMonthLabel(),
                onClickDiaryList = {
                    AmplitudeUtils.trackEvent(eventName = AmplitudeConstraints.HOME_LIST_DIARY)
                    onIntent(HomeContract.HomeIntent.OnClickDiaryList)
                },
                onClickYearMonth = {
                    onIntent(HomeContract.HomeIntent.OnClickYearMonth)
                },
                onClickSetting = {
                    onIntent(HomeContract.HomeIntent.OnClickSetting)
                },
            )
        },
        containerColor = ClodyTheme.colors.white,
        content = { innerPadding ->
            MonthlyCalendarAndDailyDiary(
                year = state.year,
                month = state.month,
                selectedDate = state.selectedDate,
                calendarMonthlyInfo = state.calendarMonthlyInfo,
                onClickDay = { dayOfMonth -> onIntent(HomeContract.HomeIntent.LoadDailyDiaryInfo(state.year, state.month, dayOfMonth)) },
                selectedDailyInfo = state.selectedDailyInfo,
                onClickDiaryDelete = { onIntent(HomeContract.HomeIntent.OnClickDiaryDelete) },
                modifier = Modifier.padding(innerPadding),
            )
        },
        bottomBar = {
            // 캘린더 데이터가 로드되었을 때만 DailyStateButton 표시
            state.getCurrentCalendarDailyInfo()?.let { calendarDailyInfo ->
                DailyStateButton(
                    calendarDailyInfo = calendarDailyInfo,
                    selectedDailyInfo = state.selectedDailyInfo,
                    onClickWriteDiary = {
                        AmplitudeUtils.trackEvent(eventName = AmplitudeConstraints.HOME_WRITING_DIARY)
                        if (state.isDraftExpired()) {
                            onIntent(HomeContract.HomeIntent.ShowDraftExpiredDialog)
                        } else { onIntent(HomeContract.HomeIntent.OnClickWriteDiary) }
                    },
                    onClickReplyDiary = {
                        AmplitudeUtils.trackEvent(eventName = AmplitudeConstraints.HOME_REPLY)
                        onIntent(HomeContract.HomeIntent.OnClickReplyDiary)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .background(ClodyTheme.colors.white)
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                )
            }
        },
    )

    if (state.showYearMonthPicker) {
        ClodyPopupBottomSheet(onDismissRequest = { onIntent(HomeContract.HomeIntent.DismissYearMonthPicker) }) {
            YearMonthPicker(
                onDismissRequest = { onIntent(HomeContract.HomeIntent.DismissYearMonthPicker) },
                selectedYear = state.year,
                selectedMonth = state.month,
                onYearMonthSelected = { newYear, newMonth ->
                    onIntent(HomeContract.HomeIntent.UpdateYearMonth(newYear, newMonth))
                },
            )
        }
    }

    if (state.showDraftNotificationPopup) {
        ClodyPopupBottomSheet(
            onDismissRequest = { onIntent(HomeContract.HomeIntent.UpdateDraftPopup(false)) },
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
                            onIntent(HomeContract.HomeIntent.EnableDraftAlarm)
                            onIntent(HomeContract.HomeIntent.UpdateDraftPopup(false))
                        },
                        enabled = true,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Text(
                        text = stringResource(R.string.bottom_sheet_home_initial_draft_skip),
                        modifier = Modifier
                            .clickable(onClick = { onIntent(HomeContract.HomeIntent.UpdateDraftPopup(false)) })
                            .padding(12.dp),
                        color = ClodyTheme.colors.gray05,
                        style = ClodyTheme.typography.body4Medium,
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }
            },
        )
    }

    if (state.showDraftNotificationToast) {
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
                    onDismiss = { /* handled by state reset elsewhere if needed */ },
                    modifier = Modifier
                        .navigationBarsPadding()
                        .padding(40.dp),
                )
            },
        )
    }

    if (state.showDraftExpiredDialog) {
        ClodyDialog(
            titleMassage = stringResource(R.string.dialog_home_continue_draft_title),
            descriptionMassage = stringResource(R.string.dialog_home_continue_draft_description),
            confirmOption = stringResource(R.string.dialog_home_continue_draft_confirm),
            dismissOption = stringResource(R.string.dialog_home_continue_draft_dismiss),
            confirmAction = { onIntent(HomeContract.HomeIntent.OnClickWriteDiary) },
            onDismiss = { },
            confirmButtonColor = ClodyTheme.colors.mainYellow,
            confirmButtonTextColor = ClodyTheme.colors.gray01,
        )
    }

    if (state.showDiaryDeleteBottomSheet) {
        DiaryDeleteSheet(
            onDismiss = { onIntent(HomeContract.HomeIntent.DismissDiaryDelete) },
            showDiaryDeleteDialog = {
                AmplitudeUtils.trackEvent(eventName = AmplitudeConstraints.HOME_DELETE_DIARY)
                onIntent(HomeContract.HomeIntent.ShowDiaryDeleteDialog)
            },
        )
    }

    if (state.showDiaryDeleteDialog) {
        ClodyDialog(
            titleMassage = stringResource(R.string.dialog_diary_delete_title),
            descriptionMassage = stringResource(R.string.dialog_diary_delete_description),
            confirmOption = stringResource(R.string.dialog_diary_delete_confirm),
            dismissOption = stringResource(R.string.dialog_diary_delete_dismiss),
            confirmAction = {
                onIntent(HomeContract.HomeIntent.ConfirmDiaryDelete(state.year, state.month, state.dayOfMonth))
            },
            onDismiss = {
                AmplitudeUtils.trackEvent(eventName = AmplitudeConstraints.HOME_NO_DELETE_DIARY)
                onIntent(HomeContract.HomeIntent.DismissDiaryDelete)
            },
            confirmButtonColor = ClodyTheme.colors.red,
            confirmButtonTextColor = ClodyTheme.colors.white,
        )
    }
}
