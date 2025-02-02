package com.sopt.clody.presentation.ui.replydiary

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sopt.clody.R
import com.sopt.clody.presentation.ui.component.FailureScreen
import com.sopt.clody.presentation.ui.component.LoadingScreen
import com.sopt.clody.presentation.ui.replydiary.navigation.ReplyDiaryNavigator
import com.sopt.clody.presentation.utils.extension.heightForScreenPercentage
import com.sopt.clody.ui.theme.ClodyTheme


@Composable
fun ReplyDiaryRoute(
    navigator: ReplyDiaryNavigator,
    year: Int,
    month: Int,
    date: Int,
    replyStatus: String,
    viewModel: ReplyDiaryViewModel = hiltViewModel(),
) {
    val replyDiaryState by viewModel.replyDiaryState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getReplyDiary(year, month, date)
    }

    var backPressedTime by remember { mutableStateOf(0L) }
    val backPressThreshold = 2000 // 2 seconds

    BackHandler {
        val currentTime = System.currentTimeMillis()
        if (currentTime - backPressedTime <= backPressThreshold) {
            navigator.navigateHome(year, month)
        } else {
            backPressedTime = currentTime
        }
    }

    when (replyDiaryState) {
        is ReplyDiaryState.Loading -> {
            LoadingScreen()
        }

        is ReplyDiaryState.Success -> {
            val successState = replyDiaryState as ReplyDiaryState.Success
            ReplyDiaryScreen(
                onClickBack = { navigator.navigateHome(year, month) },
                replyStatus = replyStatus,
                replyDiaryState = successState
            )
        }

        is ReplyDiaryState.Failure -> {
            FailureScreen(
                message = (replyDiaryState as ReplyDiaryState.Failure).error,
                confirmAction = { viewModel.retryLastRequest() }
            )
        }

        else -> {}
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReplyDiaryScreen(
    onClickBack: () -> Unit,
    replyStatus: String,
    replyDiaryState: ReplyDiaryState.Success
) {
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(replyDiaryState) {
        if (replyStatus == "READY_NOT_READ") {
            showDialog = true
        }
    }

    Scaffold(
        topBar = {
            val month = replyDiaryState.month
            val date = replyDiaryState.date
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.reply_month_and_date, month, date),
                        style = ClodyTheme.typography.head4,
                        color = ClodyTheme.colors.gray01,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onClickBack) {
                        Image(
                            painterResource(id = R.drawable.ic_nickname_back),
                            contentDescription = "back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(ClodyTheme.colors.white),
            )
        },
        content = { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(ClodyTheme.colors.white)
                    .padding(innerPadding)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .padding(bottom = 28.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(ClodyTheme.colors.gray08),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val content = replyDiaryState.content
                    val nickname = replyDiaryState.nickname
                    val replyMessage = stringResource(R.string.reply_message, nickname)

                    Spacer(modifier = Modifier.heightForScreenPercentage(0.02f))
                    Image(
                        painter = painterResource(id = R.drawable.img_reply_logo),
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.heightForScreenPercentage(0.03f))
                    Text(
                        text = replyMessage,
                        style = ClodyTheme.typography.body2SemiBold,
                        color = ClodyTheme.colors.gray01,
                    )
                    Spacer(modifier = Modifier.heightForScreenPercentage(0.02f))
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        Text(
                            text = content,
                            style = ClodyTheme.typography.letterMedium,
                            color = ClodyTheme.colors.gray02,
                        )
                        Spacer(modifier = Modifier.height(32.dp))
                    }
                }
            }
        }
    )

    if (showDialog) {
        CloverDialog(
            onDismiss = { showDialog = false },
            titleMassage = stringResource(R.string.clover_dialog_title, replyDiaryState.nickname),
            descriptionMassage = stringResource(R.string.clover_dialog_description),
            confirmOption = stringResource(R.string.clover_dialog_confirm_option),
            confirmAction = { showDialog = false },
            confirmButtonColor = ClodyTheme.colors.mainYellow
        )
    }
}
