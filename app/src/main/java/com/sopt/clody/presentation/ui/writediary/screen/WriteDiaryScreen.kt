package com.sopt.clody.presentation.ui.writediary.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.sopt.clody.R
import com.sopt.clody.presentation.ui.component.LoadingScreen
import com.sopt.clody.presentation.ui.component.button.ClodyButton
import com.sopt.clody.presentation.ui.component.dialog.ClodyDialog
import com.sopt.clody.presentation.ui.component.dialog.FailureDialog
import com.sopt.clody.presentation.ui.component.toast.ClodyToastMessage
import com.sopt.clody.presentation.ui.writediary.component.bottomsheet.DeleteWriteDiaryBottomSheet
import com.sopt.clody.presentation.ui.writediary.component.text.DiaryTitleText
import com.sopt.clody.presentation.ui.writediary.component.textfield.WriteDiaryTextField
import com.sopt.clody.presentation.ui.writediary.component.tooltip.TooltipIcon
import com.sopt.clody.presentation.ui.writediary.navigation.WriteDiaryNavigator
import com.sopt.clody.presentation.utils.extension.getDayOfWeek
import com.sopt.clody.ui.theme.ClodyTheme

@Composable
fun WriteDiaryRoute(
    navigator: WriteDiaryNavigator,
    year: Int,
    month: Int,
    day: Int,
    viewModel: WriteDiaryViewModel = hiltViewModel()
) {
    val entries = viewModel.entries
    val showWarnings = viewModel.showWarnings
    val showLimitMessage by viewModel::showLimitMessage
    val showEmptyFieldsMessage by viewModel::showEmptyFieldsMessage
    val showDeleteBottomSheet by viewModel::showDeleteBottomSheet
    val entryToDelete by viewModel::entryToDelete
    val showDialog by viewModel::showDialog
    val writeDiaryState by viewModel.writeDiaryState.collectAsState()
    val showFailureDialog by viewModel.showFailureDialog.collectAsState()
    val failureMessage by viewModel.failureMessage.collectAsState()

    LaunchedEffect(writeDiaryState) {
        when (writeDiaryState) {
            is WriteDiaryState.Success -> {
                navigator.navigateReplyLoading(year, month, day)
            }

            is WriteDiaryState.Failure -> {
                viewModel.updateShowDialog(false)
            }

            else -> {}
        }
    }

    WriteDiaryScreen(
        viewModel = viewModel,
        writeDiaryState = writeDiaryState,
        entries = entries,
        showWarnings = showWarnings,
        showLimitMessage = showLimitMessage,
        showEmptyFieldsMessage = showEmptyFieldsMessage,
        showDeleteBottomSheet = showDeleteBottomSheet,
        entryToDelete = entryToDelete,
        allFieldsEmpty = entries.all { it.isEmpty() },
        showDialog = showDialog,
        onClickBack = { navigator.navigateBack() },
        onCompleteClick = { viewModel.writeDiary(year, month, day, entries) },
        year = year,
        month = month,
        day = day
    )

    if (showFailureDialog) {
        FailureDialog(
            message = failureMessage,
            onDismiss = { viewModel.resetFailureDialog() }
        )
    }
}

@Composable
fun WriteDiaryScreen(
    viewModel: WriteDiaryViewModel,
    writeDiaryState: WriteDiaryState,
    entries: List<String>,
    showWarnings: List<Boolean>,
    showLimitMessage: Boolean,
    showEmptyFieldsMessage: Boolean,
    showDeleteBottomSheet: Boolean,
    entryToDelete: Int,
    allFieldsEmpty: Boolean,
    showDialog: Boolean,
    onClickBack: () -> Unit,
    onCompleteClick: () -> Unit,
    year: Int,
    month: Int,
    day: Int
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(ClodyTheme.colors.white)
    ) {
        val (backButton, titleRow, list, completeButton, addButton, toastMessage) = createRefs()

        IconButton(
            onClick = { onClickBack() },
            modifier = Modifier
                .constrainAs(backButton) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
                .padding(top = 26.dp, start = 12.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_nickname_back),
                contentDescription = "back",
                modifier = Modifier.size(33.dp)
            )
        }

        Row(
            modifier = Modifier
                .constrainAs(titleRow) {
                    top.linkTo(backButton.bottom, margin = 16.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            DiaryTitleText(
                date = "${month}월 ${day}일",
                separator = " ",
                day = getDayOfWeek(year, month, day)
            )
            Spacer(modifier = Modifier.weight(1f))
            TooltipIcon(
                tooltipsText = stringResource(id = R.string.write_diary_help_message),
            )
        }

        LazyColumn(
            modifier = Modifier
                .constrainAs(list) {
                    top.linkTo(titleRow.bottom, margin = 16.dp)
                    bottom.linkTo(completeButton.top, margin = 16.dp)
                    height = Dimension.fillToConstraints
                }
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            itemsIndexed(entries) { index, text ->
                WriteDiaryTextField(
                    entryNumber = index + 1,
                    text = text,
                    onTextChange = { newText ->
                        viewModel.updateEntry(index, newText)
                        viewModel.validateEntry(index, newText)
                    },
                    onRemove = {
                        viewModel.setEntryToDeleteIndex(index)
                        viewModel.updateShowDeleteBottomSheet(true)
                    },
                    isRemovable = entries.size > 1,
                    maxLength = 50,
                    showWarning = showWarnings[index]
                )
            }
        }

        ClodyButton(
            onClick = {
                viewModel.validateEntries()
                if (showWarnings.all { !it }) {
                    if (entries.size > 1 && entries.any { it.isEmpty() }) {
                        viewModel.updateShowEmptyFieldsMessage(true)
                    } else {
                        viewModel.updateShowDialog(true)
                    }
                }
            },
            text = stringResource(R.string.write_diary_dialog_confirm_option),
            enabled = !allFieldsEmpty,
            modifier = Modifier
                .constrainAs(completeButton) {
                    bottom.linkTo(parent.bottom, margin = 14.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                }
                .padding(24.dp)
        )

        Box(
            modifier = Modifier
                .constrainAs(addButton) {
                    bottom.linkTo(completeButton.top, margin = 24.dp)
                    end.linkTo(completeButton.end)
                }
                .offset(x = (-24).dp)
                .background(
                    color = if (entries.size < 5) ClodyTheme.colors.gray02 else ClodyTheme.colors.gray06,
                    shape = RoundedCornerShape(10.dp)
                )
                .size(41.dp)
                .imePadding()
        ) {
            IconButton(
                onClick = {
                    if (entries.size < 5) {
                        viewModel.addEntry()
                    }
                },
                enabled = entries.size < 5,
                modifier = Modifier.fillMaxSize()
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add",
                    tint = ClodyTheme.colors.white
                )
            }
        }

        if (showDeleteBottomSheet) {
            DeleteWriteDiaryBottomSheet(
                onDismissRequest = { viewModel.updateShowDeleteBottomSheet(false) },
                onDeleteConfirm = {
                    if (entryToDelete != -1) {
                        viewModel.removeEntry(entryToDelete)
                    }
                }
            )
        }

        if (writeDiaryState is WriteDiaryState.Loading) {
            LoadingScreen()
        }

        if (showDialog) {
            ClodyDialog(
                onDismiss = { viewModel.updateShowDialog(false) },
                titleMassage = stringResource(R.string.write_diary_dialog_title),
                descriptionMassage = stringResource(R.string.write_diary_dialog_description),
                confirmOption = stringResource(R.string.write_diary_dialog_confirm_option),
                dismissOption = stringResource(R.string.write_diary_dialog_dismiss_option),
                confirmAction = { onCompleteClick() },
                confirmButtonColor = ClodyTheme.colors.mainYellow,
                confirmButtonTextColor = ClodyTheme.colors.gray01
            )
        }


        ShowToastMessages(
            showLimitMessage = showLimitMessage,
            showEmptyFieldsMessage = showEmptyFieldsMessage,
            onShowLimitMessageChange = { viewModel.updateShowLimitMessage(it) },
            onShowEmptyFieldsMessageChange = { viewModel.updateShowEmptyFieldsMessage(it) },
            modifier = Modifier.constrainAs(toastMessage) {
                bottom.linkTo(parent.bottom, margin = 16.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        )
    }
}

@Composable
private fun ShowToastMessages(
    showLimitMessage: Boolean,
    showEmptyFieldsMessage: Boolean,
    onShowLimitMessageChange: (Boolean) -> Unit,
    onShowEmptyFieldsMessageChange: (Boolean) -> Unit,
    modifier: Modifier
) {
    Box(modifier = modifier) {
        if (showLimitMessage) {
            ClodyToastMessage(
                message = stringResource(R.string.toast_limit_message),
                iconResId = R.drawable.ic_toast_error,
                backgroundColor = ClodyTheme.colors.gray04,
                contentColor = ClodyTheme.colors.white,
                durationMillis = 3000,
                onDismiss = { onShowLimitMessageChange(false) }
            )
        }

        if (showEmptyFieldsMessage) {
            ClodyToastMessage(
                message = stringResource(R.string.toast_empty_fields_message),
                iconResId = R.drawable.ic_toast_error,
                backgroundColor = ClodyTheme.colors.gray04,
                contentColor = ClodyTheme.colors.white,
                durationMillis = 3000,
                onDismiss = { onShowEmptyFieldsMessageChange(false) }
            )
        }
    }
}
