package com.sopt.clody.presentation.ui.writediary.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
import com.sopt.clody.presentation.utils.amplitude.AmplitudeConstraints
import com.sopt.clody.presentation.utils.amplitude.AmplitudeUtils
import com.sopt.clody.presentation.utils.extension.getDayOfWeek
import com.sopt.clody.presentation.utils.extension.heightForScreenPercentage
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

    val allFieldsEmpty by remember {
        derivedStateOf { entries.all { it.isEmpty() } }
    }

    LaunchedEffect(writeDiaryState) {
        when (writeDiaryState) {
            is WriteDiaryState.Success -> navigator.navigateReplyLoading(year, month, day)
            is WriteDiaryState.NoReply -> navigator.navigateHome(year, month)
            is WriteDiaryState.Failure -> viewModel.updateShowDialog(false)
            else -> {}
        }
    }

    WriteDiaryScreen(
        viewModel = viewModel,
        isLoading = writeDiaryState is WriteDiaryState.Loading,
        entries = entries,
        showWarnings = showWarnings,
        showLimitMessage = showLimitMessage,
        showEmptyFieldsMessage = showEmptyFieldsMessage,
        showDeleteBottomSheet = showDeleteBottomSheet,
        entryToDelete = entryToDelete,
        allFieldsEmpty = allFieldsEmpty,
        showDialog = showDialog,
        onClickBack = {
            AmplitudeUtils.trackEvent(eventName = AmplitudeConstraints.WRITING_DIARY_BACK)
            navigator.navigateHome(year, month) },
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
    isLoading: Boolean,
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
    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = {
            IconButton(
                onClick = onClickBack,
                modifier = Modifier
                    .padding(top = 26.dp)
                    .padding(start = 4.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_nickname_back),
                    contentDescription = null
                )
            }
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Transparent),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier.align(Alignment.BottomCenter)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 24.dp)
                            .padding(bottom = 28.dp),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        IconButton(
                            onClick = {
                                AmplitudeUtils.trackEvent(eventName = AmplitudeConstraints.WRITING_DIARY_ADD_LIST)
                                if (entries.size < 5) {
                                    viewModel.addEntry()
                                }
                            },
                            enabled = entries.size < 5,
                            modifier = Modifier
                                .background(
                                    color = if (entries.size < 5) ClodyTheme.colors.gray02 else ClodyTheme.colors.gray06,
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .size(41.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_writediary_add),
                                contentDescription = "Add",
                            )
                        }
                    }

                    ClodyButton(
                        onClick = {
                            AmplitudeUtils.trackEvent(eventName = AmplitudeConstraints.WRITING_DIARY_COMPLETE)
                            viewModel.validateEntries()
                            if (showWarnings.all { !it }) {
                                if (entries.size > 1 && entries.any { it.isEmpty() }) {
                                    viewModel.updateShowEmptyFieldsMessage(true)
                                } else {
                                    viewModel.updateShowDialog(true)
                                }
                            }
                        },
                        text = stringResource(R.string.write_diary_confirm_button),
                        enabled = !allFieldsEmpty,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                            .padding(bottom = 28.dp)
                    )
                }

                ShowToastMessages(
                    showLimitMessage = showLimitMessage,
                    showEmptyFieldsMessage = showEmptyFieldsMessage,
                    onShowLimitMessageChange = { viewModel.updateShowLimitMessage(it) },
                    onShowEmptyFieldsMessageChange = { viewModel.updateShowEmptyFieldsMessage(it) },
                    modifier = Modifier.align(Alignment.Center)
                )
            }
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
                        interactionSource = remember { MutableInteractionSource() }
                    ) { focusManager.clearFocus() },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.heightForScreenPercentage(0.017f))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    DiaryTitleText(
                        date = stringResource(R.string.write_diary_month_and_date, month, day),
                        separator = " ",
                        day = getDayOfWeek(year, month, day)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    TooltipIcon(
                        tooltipsText = stringResource(id = R.string.write_diary_help_message),
                    )
                }
                Spacer(modifier = Modifier.heightForScreenPercentage(0.02f))
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    itemsIndexed(
                        items = entries,
                        key = { index, _ -> index }
                    ) { index, text ->
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

                if (showDeleteBottomSheet) {
                    DeleteWriteDiaryBottomSheet(
                        onDismissRequest = { viewModel.updateShowDeleteBottomSheet(false) },
                        onDeleteConfirm = {
                            AmplitudeUtils.trackEvent(eventName = AmplitudeConstraints.WRITING_DIARY_DELETE_LIST)
                            if (entryToDelete != -1) {
                                viewModel.removeEntry(entryToDelete)
                            }
                        }
                    )
                }

                if (showDialog) {
                    ClodyDialog(
                        onDismiss = {
                            AmplitudeUtils.trackEvent(eventName = AmplitudeConstraints.WRITING_DIARY_NO_COMPLETE)
                            viewModel.updateShowDialog(false) },
                        titleMassage = stringResource(R.string.write_diary_dialog_title),
                        descriptionMassage = stringResource(R.string.write_diary_dialog_description),
                        confirmOption = stringResource(R.string.write_diary_dialog_confirm_option),
                        dismissOption = stringResource(R.string.write_diary_dialog_dismiss_option),
                        confirmAction = { onCompleteClick() },
                        confirmButtonColor = ClodyTheme.colors.mainYellow,
                        confirmButtonTextColor = ClodyTheme.colors.gray01
                    )
                }
            }
        }
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
    modifier: Modifier
) {
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
