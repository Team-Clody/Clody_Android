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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sopt.clody.R
import com.sopt.clody.presentation.ui.component.FailureScreen
import com.sopt.clody.presentation.ui.component.LoadingScreen
import com.sopt.clody.presentation.ui.replydiary.navigation.ReplyDiaryNavigator
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
    LaunchedEffect(Unit) {
        viewModel.getReplyDiary(year, month, date)
    }

    var backPressedTime by remember { mutableStateOf(0L) }
    val backPressThreshold = 2000 // 2 seconds

    BackHandler {
        val currentTime = System.currentTimeMillis()
        if (currentTime - backPressedTime <= backPressThreshold) {
            navigator.navigateHome()
        } else {
            backPressedTime = currentTime
        }
    }

    ReplyDiaryScreen(
        viewModel = viewModel,
        onClickBack = { navigator.navigateHome() },
        replyStatus = replyStatus
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReplyDiaryScreen(
    viewModel: ReplyDiaryViewModel,
    onClickBack: () -> Unit,
    replyStatus: String
) {
    val replyDiaryState by viewModel.replyDiaryState.collectAsState()

    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(replyDiaryState) {
        if (replyStatus == "READY_NOT_READ" && replyDiaryState is ReplyDiaryState.Success) {
            showDialog = true
        }
    }

    when (replyDiaryState) {
        is ReplyDiaryState.Loading -> {
            LoadingScreen()
        }

        is ReplyDiaryState.Failure -> {
            FailureScreen(
                message = (replyDiaryState as ReplyDiaryState.Failure).error,
                confirmAction = { viewModel.retryLastRequest() }
            )
        }

        is ReplyDiaryState.Success -> {
            if (showDialog) {
                val nickname = (replyDiaryState as ReplyDiaryState.Success).nickname
                CloverDialog(
                    onDismiss = { showDialog = false },
                    titleMassage = "${nickname}님을 위한 행운 도착",
                    descriptionMassage = "1개의 네잎클로버 획득",
                    confirmOption = "확인",
                    confirmAction = {
                        showDialog = false
                    },
                    confirmButtonColor = ClodyTheme.colors.mainYellow
                )
            } else {
                Scaffold(
                    topBar = {
                        val month = (replyDiaryState as ReplyDiaryState.Success).month
                        val date = (replyDiaryState as ReplyDiaryState.Success).date
                        CenterAlignedTopAppBar(
                            title = {
                                Text(
                                    text = "${month}월 ${date}일",
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
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(ClodyTheme.colors.white)
                                .padding(innerPadding)
                                .padding(horizontal = 24.dp)
                        ) {
                            Spacer(modifier = Modifier.height(13.dp))

                            Box(
                                contentAlignment = Alignment.TopCenter,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 28.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(ClodyTheme.colors.gray08)
                                    .weight(1f)
                            ) {
                                val content = (replyDiaryState as ReplyDiaryState.Success).content
                                val nickname = (replyDiaryState as ReplyDiaryState.Success).nickname
                                LazyColumn(
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    item {
                                        Box(
                                            modifier = Modifier.fillMaxWidth(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Image(
                                                painter = painterResource(id = R.drawable.img_reply_logo),
                                                contentDescription = null,
                                                modifier = Modifier
                                                    .padding(top = 20.dp)
                                            )
                                        }

                                        Box(
                                            modifier = Modifier.fillMaxWidth(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = "${nickname}님을 위한 행운의 답장",
                                                style = ClodyTheme.typography.body2Medium,
                                                color = ClodyTheme.colors.gray01,
                                                modifier = Modifier.padding(top = 8.dp)
                                            )
                                        }

                                        Box(
                                            modifier = Modifier.fillMaxWidth(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = content,
                                                modifier = Modifier.padding(24.dp),
                                                style = ClodyTheme.typography.letterMedium,
                                                color = ClodyTheme.colors.gray02,
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                )
            }
        }

        else -> {}
    }
}
