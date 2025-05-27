package com.sopt.clody.presentation.ui.writediary.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sopt.clody.R
import com.sopt.clody.presentation.ui.component.LoadingScreen
import com.sopt.clody.presentation.ui.component.dialog.ClodyDialog
import com.sopt.clody.presentation.ui.component.dialog.FailureDialog
import com.sopt.clody.presentation.ui.component.toast.ClodyToastMessage
import com.sopt.clody.presentation.ui.writediary.component.bottomsheet.DeleteWriteDiaryBottomSheet
import com.sopt.clody.presentation.ui.writediary.component.text.DiaryTitleText
import com.sopt.clody.presentation.ui.writediary.component.textfield.WriteDiaryTextField
import com.sopt.clody.presentation.ui.writediary.component.tooltip.TooltipIcon
import com.sopt.clody.presentation.utils.extension.getDayOfWeek
import com.sopt.clody.presentation.utils.extension.heightForScreenPercentage
import com.sopt.clody.ui.theme.CLODYTheme
import com.sopt.clody.ui.theme.ClodyTheme

@Composable
fun WriteDiaryRoute(
    year: Int,
    month: Int,
    date: Int,
    navigateToReplyLoading: (Int, Int, Int) -> Unit,
    navigateToHome: (Int, Int) -> Unit,
    navigateToPrevious: () -> Unit,
    viewModel: WriteDiaryViewModel = hiltViewModel(),
) {
    val writeDiaryState by viewModel.writeDiaryState.collectAsState()
    val showFailureDialog by viewModel.showFailureDialog.collectAsState()
    val failureMessage by viewModel.failureMessage.collectAsState()
    val entries = viewModel.entries
    val showWarnings = viewModel.showWarnings
    val showLimitMessage by viewModel::showLimitMessage
    val showEmptyFieldsMessage by viewModel::showEmptyFieldsMessage
    val showDeleteBottomSheet by viewModel::showDeleteBottomSheet
    val entryToDelete by viewModel::entryToDelete
    val showDialog by viewModel::showDialog

    val allFieldsEmpty by remember(entries) {
        derivedStateOf { entries.all { it.isEmpty() } }
    }

    LaunchedEffect(writeDiaryState) {
        when (writeDiaryState) {
            is WriteDiaryState.Success -> navigateToReplyLoading(year, month, date)
            is WriteDiaryState.NoReply -> navigateToHome(year, month)
            is WriteDiaryState.Failure -> viewModel.updateShowDialog(false)
            else -> {}
        }
    }

    WriteDiaryScreen(
        isLoading = writeDiaryState is WriteDiaryState.Loading,
        entries = entries,
        showWarnings = showWarnings,
        showLimitMessage = showLimitMessage,
        showEmptyFieldsMessage = showEmptyFieldsMessage,
        showDeleteBottomSheet = showDeleteBottomSheet,
        entryToDelete = entryToDelete,
        showDialog = showDialog,
        allFieldsEmpty = allFieldsEmpty,
        onClickBack = navigateToPrevious,
        onClickAdd = viewModel::addEntry,
        onClickRemove = { index ->
            viewModel.setEntryToDeleteIndex(index)
            viewModel.updateShowDeleteBottomSheet(true)
        },
        onConfirmDelete = {
            if (entryToDelete != -1) viewModel.removeEntry(entryToDelete)
        },
        onDismissDelete = { viewModel.updateShowDeleteBottomSheet(false) },
        onTextChange = { index, text ->
            viewModel.updateEntry(index, text)
            viewModel.validateEntry(index, text)
        },
        onClickComplete = {
            viewModel.validateEntries()
            if (showWarnings.all { !it }) {
                if (entries.size > 1 && entries.any { it.isEmpty() }) {
                    viewModel.updateShowEmptyFieldsMessage(true)
                } else {
                    viewModel.updateShowDialog(true)
                }
            }
        },
        onConfirmDialog = { viewModel.writeDiary(year, month, date, entries) },
        onDismissDialog = { viewModel.updateShowDialog(false) },
        onDismissLimitMessage = { viewModel.updateShowLimitMessage(false) },
        onDismissEmptyFieldsMessage = { viewModel.updateShowEmptyFieldsMessage(false) },
        year = year,
        month = month,
        day = date,
    )

    if (showFailureDialog) {
        FailureDialog(
            message = failureMessage,
            onDismiss = viewModel::resetFailureDialog,
        )
    }
}

@Composable
fun WriteDiaryScreen(
    isLoading: Boolean,
    entries: List<String>,
    showWarnings: List<Boolean>,
    showLimitMessage: Boolean,
    showEmptyFieldsMessage: Boolean,
    showDeleteBottomSheet: Boolean,
    entryToDelete: Int,
    showDialog: Boolean,
    allFieldsEmpty: Boolean,
    onClickBack: () -> Unit,
    onClickAdd: () -> Unit,
    onClickRemove: (Int) -> Unit,
    onConfirmDelete: () -> Unit,
    onDismissDelete: () -> Unit,
    onTextChange: (Int, String) -> Unit,
    onClickComplete: () -> Unit,
    onConfirmDialog: () -> Unit,
    onDismissDialog: () -> Unit,
    onDismissLimitMessage: (Boolean) -> Unit,
    onDismissEmptyFieldsMessage: (Boolean) -> Unit,
    year: Int,
    month: Int,
    day: Int,
) {
    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = {

        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(ClodyTheme.colors.white)
                    .padding(innerPadding)
                    .padding(horizontal = 24.dp)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                    ) { focusManager.clearFocus() },
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier.heightForScreenPercentage(0.017f))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    DiaryTitleText(
                        date = stringResource(R.string.write_diary_month_and_date, month, day),
                        separator = " ",
                        day = getDayOfWeek(year, month, day),
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    TooltipIcon(
                        tooltipsText = stringResource(id = R.string.write_diary_help_message),
                    )
                }
                Spacer(modifier = Modifier.heightForScreenPercentage(0.02f))
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    itemsIndexed(entries, key = { index, _ -> index }) { index, text ->
                        WriteDiaryTextField(
                            entryNumber = index + 1,
                            text = text,
                            onTextChange = { newText -> onTextChange(index, newText) },
                            onRemove = { onClickRemove(index) },
                            isRemovable = entries.size > 1,
                            maxLength = 50,
                            showWarning = showWarnings[index],
                        )
                    }
                }

                if (showDeleteBottomSheet) {
                    DeleteWriteDiaryBottomSheet(
                        onDismissRequest = onDismissDelete,
                        onDeleteConfirm = onConfirmDelete,
                    )
                }

                if (showDialog) {
                    ClodyDialog(
                        onDismiss = onDismissDialog,
                        titleMassage = stringResource(R.string.write_diary_dialog_title),
                        descriptionMassage = stringResource(R.string.write_diary_dialog_description),
                        confirmOption = stringResource(R.string.write_diary_dialog_confirm_option),
                        dismissOption = stringResource(R.string.write_diary_dialog_dismiss_option),
                        confirmAction = onConfirmDialog,
                        confirmButtonColor = ClodyTheme.colors.mainYellow,
                        confirmButtonTextColor = ClodyTheme.colors.gray01,
                    )
                }
            }
        },
    )
    if (isLoading) {
        LoadingScreen()
    }
}

@Composable
private fun ShowToastMessages(
    showLimitMessage: Boolean,
    showEmptyFieldsMessage: Boolean,
    onShowLimitMessageChange: (Boolean) -> Unit,
    onShowEmptyFieldsMessageChange: (Boolean) -> Unit,
    modifier: Modifier,
) {
    if (showLimitMessage) {
        ClodyToastMessage(
            message = stringResource(R.string.toast_limit_message),
            iconResId = R.drawable.ic_toast_error,
            backgroundColor = ClodyTheme.colors.gray04,
            contentColor = ClodyTheme.colors.white,
            durationMillis = 3000,
            onDismiss = { onShowLimitMessageChange(false) },
        )
    }

    if (showEmptyFieldsMessage) {
        ClodyToastMessage(
            message = stringResource(R.string.toast_empty_fields_message),
            iconResId = R.drawable.ic_toast_error,
            backgroundColor = ClodyTheme.colors.gray04,
            contentColor = ClodyTheme.colors.white,
            durationMillis = 3000,
            onDismiss = { onShowEmptyFieldsMessageChange(false) },
        )
    }
}

@Composable
@Preview
private fun WriteDiaryScreenPreview() {
    CLODYTheme {
        WriteDiaryScreen(
            isLoading = false,
            entries = listOf("Entry 1", "Entry 2", "Entry 3"),
            showWarnings = listOf(false, true, false),
            showLimitMessage = false,
            showEmptyFieldsMessage = false,
            showDeleteBottomSheet = false,
            entryToDelete = -1,
            showDialog = false,
            allFieldsEmpty = false,
            onClickBack = {},
            onClickAdd = {},
            onClickRemove = {},
            onConfirmDelete = {},
            onDismissDelete = {},
            onTextChange = { _, _ -> },
            onClickComplete = {},
            onConfirmDialog = {},
            onDismissDialog = {},
            onDismissLimitMessage = {},
            onDismissEmptyFieldsMessage = {},
            year = 2023,
            month = 10,
            day = 5,
        )
    }
}

