package com.sopt.clody.presentation.ui.writediary.screen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.sopt.clody.R
import com.sopt.clody.presentation.ui.component.button.ClodyButton
import com.sopt.clody.presentation.ui.component.dialog.ClodyDialog
import com.sopt.clody.presentation.ui.writediary.component.DeleteWriteDiaryBottomSheet
import com.sopt.clody.presentation.ui.writediary.component.DiaryTitleText
import com.sopt.clody.presentation.ui.writediary.component.WriteDiaryTextField
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
    WriteDiaryScreen(
        viewModel = viewModel,
        onClickBack = { navigator.navigateBack() },
        onCompleteClick = { navigator.navigateReplyLoading(year, month, day) },
        year = year,
        month = month,
        day = day
    )
}


@Composable
fun WriteDiaryScreen(
    viewModel: WriteDiaryViewModel,
    onClickBack: () -> Unit,
    onCompleteClick: () -> Unit,
    year: Int,
    month: Int,
    day: Int
) {
    val entries = remember { mutableStateListOf("") }
    val showWarnings = remember { mutableStateListOf(false) }
    var showLimitMessage by remember { mutableStateOf(false) }
    var showDeleteBottomSheet by remember { mutableStateOf(false) }
    var entryToDelete by remember { mutableStateOf(-1) }
    val allFieldsEmpty = entries.all { it.isEmpty() }
    var showDialog by remember { mutableStateOf(false) }
    val writeDiaryState by viewModel.writeDiaryState.collectAsState()

    LaunchedEffect(writeDiaryState) {
        if (writeDiaryState is WriteDiaryState.Success) {
            onCompleteClick()
        }
    }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(ClodyTheme.colors.white)
    ) {
        val (backButton, list, completeButton, addButton) = createRefs()

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
                painterResource(id = R.drawable.ic_nickname_back),
                contentDescription = "back",
                modifier = Modifier.size(33.dp)
            )
        }

        LazyColumn(
            modifier = Modifier
                .constrainAs(list) {
                    top.linkTo(backButton.bottom, margin = 16.dp)
                    bottom.linkTo(completeButton.top, margin = 16.dp)
                    height = Dimension.fillToConstraints
                }
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                DiaryTitleText(
                    date = "${month}월 ${day}일",
                    separator = " ",
                    day = getDayOfWeek(year, month, day)
                )
            }

            itemsIndexed(entries) { index, text ->
                WriteDiaryTextField(
                    entryNumber = index + 1,
                    text = text,
                    onTextChange = { newText ->
                        entries[index] = newText
                        val textWithoutSpaces = newText.replace("\\s".toRegex(), "")
                        showWarnings[index] = !textWithoutSpaces.matches(Regex("^[a-zA-Z가-힣0-9ㄱ-ㅎㅏ-ㅣ가-힣\\W]{2,50}$"))
                    },
                    onRemove = {
                        entryToDelete = index
                        showDeleteBottomSheet = true
                    },
                    isRemovable = entries.size > 1,
                    maxLength = 50,
                    showWarning = showWarnings[index]
                )
            }
        }

        ClodyButton(
            onClick = {
                for (i in entries.indices) {
                    val textWithoutSpaces = entries[i].replace("\\s".toRegex(), "")
                    showWarnings[i] = !textWithoutSpaces.matches(Regex("^[a-zA-Z가-힣0-9ㄱ-ㅎㅏ-ㅣ가-힣\\W]{2,50}$"))
                }
                if (showWarnings.all { !it }) {
                    showDialog = true
                }
            },
            text = "저장",
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
                        entries.add("")
                        showWarnings.add(false)
                    } else {
                        showLimitMessage = true
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
                onDismissRequest = { showDeleteBottomSheet = false },
                onDeleteConfirm = {
                    if (entryToDelete != -1) {
                        entries.removeAt(entryToDelete)
                        showWarnings.removeAt(entryToDelete)
                        entryToDelete = -1
                    }
                }
            )
        }

        if (showDialog) {
            ClodyDialog(
                onDismiss = { showDialog = false },
                titleMassage = "일기를 저장할까요?",
                descriptionMassage = "저장한 일기는 수정이 어려워요.",
                confirmOption = "저장하기",
                dismissOption = "아니오",
                confirmAction = {
                    viewModel.writeDiary(year, month, day, entries.toList())
                },
                confirmButtonColor = ClodyTheme.colors.mainYellow,
                confirmButtonTextColor = ClodyTheme.colors.gray01
            )
        }
    }

    // 상태에 따른 UI 업데이트
    when (writeDiaryState) {
        is WriteDiaryState.Loading -> {
            CircularProgressIndicator(
                modifier = Modifier.fillMaxSize(),
                color = ClodyTheme.colors.mainYellow
            )
        }
        is WriteDiaryState.Success -> {
            val createdAt = (writeDiaryState as WriteDiaryState.Success).createdAt
            Log.d("WriteDiaryScreen", "Diary created at $createdAt")
        }
        is WriteDiaryState.Failure -> {
            val error = (writeDiaryState as WriteDiaryState.Failure).error
            Log.e("WriteDiaryScreen", "Failed: $error")
            showDialog = false // 상태가 실패일 때 다이얼로그 닫기
        }
        else -> {}
    }
}
