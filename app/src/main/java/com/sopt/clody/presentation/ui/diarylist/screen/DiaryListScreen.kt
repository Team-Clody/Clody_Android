package com.sopt.clody.presentation.ui.diarylist.screen

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.sopt.clody.R
import com.sopt.clody.presentation.ui.component.FailureScreen
import com.sopt.clody.presentation.ui.component.LoadingScreen
import com.sopt.clody.presentation.ui.component.bottomsheet.DiaryDeleteSheet
import com.sopt.clody.presentation.ui.component.dialog.ClodyDialog
import com.sopt.clody.presentation.ui.component.dialog.FailureDialog
import com.sopt.clody.presentation.ui.component.popup.ClodyPopupBottomSheet
import com.sopt.clody.presentation.ui.component.timepicker.YearMonthPicker
import com.sopt.clody.presentation.ui.diarylist.component.DiaryListTopAppBar
import com.sopt.clody.presentation.ui.diarylist.component.EmptyDiaryList
import com.sopt.clody.presentation.ui.diarylist.component.MonthlyDiaryList
import com.sopt.clody.presentation.ui.diarylist.navigation.DiaryListNavigator
import com.sopt.clody.presentation.utils.amplitude.AmplitudeConstraints
import com.sopt.clody.presentation.utils.amplitude.AmplitudeUtils
import com.sopt.clody.ui.theme.ClodyTheme

@Composable
fun DiaryListRoute(
    navigator: DiaryListNavigator,
    diaryListViewModel: DiaryListViewModel = hiltViewModel(),
    selectedYearFromHome: Int,
    selectedMonthFromHome: Int
) {
    var selectedYearInDiaryList by remember { mutableIntStateOf(selectedYearFromHome) }
    var selectedMonthInDiaryList by remember { mutableIntStateOf(selectedMonthFromHome) }
    val diaryListState by diaryListViewModel.diaryListState.collectAsState()
    val selectedDiaryDate by diaryListViewModel.selectedDiaryDate.collectAsState()
    val diaryDeleteState by diaryListViewModel.diaryDeleteState.collectAsState()
    val showDiaryDeleteFailureDialog by diaryListViewModel.showDiaryDeleteFailureDialog.collectAsState()
    val failureDialogMessage by diaryListViewModel.failureDialogMessage.collectAsState()
    var yearMonthPickerState by remember { mutableStateOf(false) }
    var diaryDeleteBottomSheetState by remember { mutableStateOf(false) }
    var diaryDeleteDialogState by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        AmplitudeUtils.trackEvent(eventName = AmplitudeConstraints.LIST)
    }

    LaunchedEffect(selectedYearInDiaryList, selectedMonthInDiaryList) {
        diaryListViewModel.fetchMonthlyDiary(selectedYearInDiaryList, selectedMonthInDiaryList)
    }

    LaunchedEffect(diaryDeleteState) {
        if (diaryDeleteState is DiaryDeleteState.Success) {
            diaryListViewModel.fetchMonthlyDiary(selectedYearInDiaryList, selectedMonthInDiaryList)
        }
    }

    DiaryListScreen(
        diaryListViewModel = diaryListViewModel,
        selectedYearInDiaryList = selectedYearInDiaryList,
        selectedMonthInDiaryList = selectedMonthInDiaryList,
        updateYearAndMonth = { newYear, newMonth -> selectedYearInDiaryList = newYear; selectedMonthInDiaryList = newMonth },
        diaryListState = diaryListState,
        selectedDiaryDate = selectedDiaryDate,
        showDiaryDeleteFailureDialog = showDiaryDeleteFailureDialog,
        failureDialogMessage = failureDialogMessage,
        yearMonthPickerState = yearMonthPickerState,
        showYearMonthPicker = { yearMonthPickerState = true },
        dismissYearMonthPicker = { yearMonthPickerState = false },
        diaryDeleteBottomSheetState = diaryDeleteBottomSheetState,
        showDiaryDeleteBottomSheet = { diaryDeleteBottomSheetState = true },
        dismissDiaryDeleteBottomSheet = { diaryDeleteBottomSheetState = false },
        diaryDeleteDialogState = diaryDeleteDialogState,
        showDiaryDeleteDialog = {
            AmplitudeUtils.trackEvent(eventName = AmplitudeConstraints.LIST_DELETE_DIARY)
            diaryDeleteDialogState = true
        },
        dismissDiaryDeleteDialog = { diaryDeleteDialogState = false },
        onClickDiaryDelete = { year, month, day -> diaryListViewModel.deleteDailyDiary(year, month, day) },
        onClickCalendar = { navigator.navigateHome(selectedYearInDiaryList, selectedMonthInDiaryList) },
        onClickReplyDiary = { year, month, day, replyStatus ->
            navigator.navigateReplyLoading(year, month, day, replyStatus)
            AmplitudeUtils.trackEvent(eventName = AmplitudeConstraints.LIST_REPLY)
        }
    )
}

@Composable
fun DiaryListScreen(
    diaryListViewModel: DiaryListViewModel,
    selectedYearInDiaryList: Int,
    selectedMonthInDiaryList: Int,
    updateYearAndMonth: (Int, Int) -> Unit,
    diaryListState: DiaryListState,
    selectedDiaryDate: DiaryListViewModel.DiaryDate,
    showDiaryDeleteFailureDialog: Boolean,
    failureDialogMessage: String,
    yearMonthPickerState: Boolean,
    showYearMonthPicker: () -> Unit,
    dismissYearMonthPicker: () -> Unit,
    diaryDeleteBottomSheetState: Boolean,
    showDiaryDeleteBottomSheet: () -> Unit,
    dismissDiaryDeleteBottomSheet: () -> Unit,
    diaryDeleteDialogState: Boolean,
    showDiaryDeleteDialog: () -> Unit,
    dismissDiaryDeleteDialog: () -> Unit,
    onClickDiaryDelete: (Int, Int, Int) -> Unit,
    onClickCalendar: () -> Unit,
    onClickReplyDiary: (Int, Int, Int, String) -> Unit,
) {
    Scaffold(
        topBar = {
            DiaryListTopAppBar(
                selectedYear = selectedYearInDiaryList,
                selectedMonth = selectedMonthInDiaryList,
                showYearMonthPicker = showYearMonthPicker,
                onClickCalendar = onClickCalendar,
            )
        },
        containerColor = ClodyTheme.colors.gray08,
        content = { innerPadding ->
            when (diaryListState) {
                is DiaryListState.Idle -> {

                }

                is DiaryListState.Loading -> {
                    LoadingScreen()
                }

                is DiaryListState.Success -> {
                    if (diaryListState.data.diaries.isEmpty()) EmptyDiaryList()
                    else {
                        MonthlyDiaryList(
                            paddingValues = innerPadding,
                            diaryListViewModel = diaryListViewModel,
                            diaries = diaryListState.data.diaries,
                            showDiaryDeleteBottomSheet = showDiaryDeleteBottomSheet,
                            onClickReplyDiary = onClickReplyDiary
                        )
                    }
                }

                is DiaryListState.Failure -> {
                    FailureScreen(
                        message = diaryListState.errorMessage,
                        confirmAction = { diaryListViewModel.fetchMonthlyDiary(selectedYearInDiaryList, selectedMonthInDiaryList) }
                    )
                }
            }
        }
    )

    if (yearMonthPickerState) {
        ClodyPopupBottomSheet(
            onDismissRequest = dismissYearMonthPicker
        ) {
            YearMonthPicker(
                onDismissRequest = dismissYearMonthPicker,
                selectedYear = selectedYearInDiaryList,
                selectedMonth = selectedMonthInDiaryList,
                onYearMonthSelected = updateYearAndMonth
            )
        }
    }

    if (diaryDeleteBottomSheetState) {
        DiaryDeleteSheet(
            onDismiss = dismissDiaryDeleteBottomSheet,
            showDiaryDeleteDialog = showDiaryDeleteDialog
        )
    }

    if (diaryDeleteDialogState) {
        ClodyDialog(
            titleMassage = stringResource(R.string.diary_delete_dialog_title),
            descriptionMassage = stringResource(R.string.diary_delete_dialog_description),
            confirmOption = stringResource(R.string.diary_delete_dialog_confirm_option),
            dismissOption = stringResource(R.string.diary_delete_dialog_dismiss_option),
            confirmAction = {
                onClickDiaryDelete(selectedDiaryDate.year, selectedDiaryDate.month, selectedDiaryDate.day)
                dismissDiaryDeleteDialog()
            },
            onDismiss = {
                AmplitudeUtils.trackEvent(eventName = AmplitudeConstraints.LIST_NO_DELETE_DIARY)
                dismissDiaryDeleteDialog()
            },
            confirmButtonColor = ClodyTheme.colors.red,
            confirmButtonTextColor = ClodyTheme.colors.white
        )
    }

    if (showDiaryDeleteFailureDialog) {
        FailureDialog(
            message = failureDialogMessage,
            onDismiss = { diaryListViewModel.dismissDiaryDeleteFailureDialog() }
        )
    }
}
