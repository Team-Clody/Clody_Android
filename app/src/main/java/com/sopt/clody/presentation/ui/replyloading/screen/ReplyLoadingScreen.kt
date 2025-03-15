package com.sopt.clody.presentation.ui.replyloading.screen

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieConstants
import com.sopt.clody.R
import com.sopt.clody.presentation.ui.component.FailureScreen
import com.sopt.clody.presentation.ui.component.LoadingScreen
import com.sopt.clody.presentation.ui.component.button.ClodyButton
import com.sopt.clody.presentation.ui.component.toast.ClodyToastMessage
import com.sopt.clody.presentation.ui.replyloading.component.LottieAnimation
import com.sopt.clody.presentation.ui.replyloading.component.QuickReplyAdButton
import com.sopt.clody.presentation.ui.replyloading.navigation.ReplyLoadingNavigator
import com.sopt.clody.presentation.utils.amplitude.AmplitudeConstraints
import com.sopt.clody.presentation.utils.amplitude.AmplitudeUtils
import com.sopt.clody.presentation.utils.extension.heightForScreenPercentage
import com.sopt.clody.ui.theme.ClodyTheme
import kotlinx.coroutines.delay
import java.time.LocalDateTime

@Composable
fun ReplyLoadingRoute(
    navigator: ReplyLoadingNavigator,
    year: Int,
    month: Int,
    day: Int,
    from: String,
    replyStatus: String,
    viewModel: ReplyLoadingViewModel = hiltViewModel()
) {
    val replyLoadingState by viewModel.replyLoadingState.collectAsState()
    val isAdLoadingState by viewModel.isAdLoading.collectAsState()
    val isWaitingForPatchResponse by viewModel.isWaitingForPatchResponse.collectAsState()
    val isAdCompleted by viewModel.isAdCompleted.collectAsState()
    val adErrorMessage by viewModel.adErrorMessage.collectAsState()
    val activity = LocalContext.current as Activity

    LaunchedEffect(Unit) {
        AmplitudeUtils.trackEvent(eventName = AmplitudeConstraints.WAITING_DIARY)
        viewModel.getDiaryTime(year, month, day)
    }

    var backPressedTime by remember { mutableStateOf(0L) }
    val backPressThreshold = 2000

    BackHandler {
        val currentTime = System.currentTimeMillis()
        if (currentTime - backPressedTime <= backPressThreshold) {
            navigator.navigateHome(year, month)
        } else {
            backPressedTime = currentTime
        }
    }

    when (replyLoadingState) {
        is ReplyLoadingState.Loading -> {
            LoadingScreen()
        }

        is ReplyLoadingState.Success -> {
            val successState = replyLoadingState as ReplyLoadingState.Success
            ReplyLoadingScreen(
                onCompleteClick = { navigator.navigateReplyDiary(year, month, day, replyStatus) },
                onBackClick = { navigator.navigateBack(year, month, from) },
                replyLoadingState = successState,
                onShowAdClick = {
                    viewModel.loadAndShowRewardedAd(activity)
                },
                isAdLoading = isAdLoadingState,
                isWaitingForPatchResponse = isWaitingForPatchResponse,
                isAdCompleted = isAdCompleted,
                adErrorMessage = adErrorMessage,
                onDismissToast = { viewModel.clearAdErrorMessage() }
            )
        }

        is ReplyLoadingState.Failure -> {
            val failureState = replyLoadingState as ReplyLoadingState.Failure
            FailureScreen(
                message = failureState.error,
                confirmAction = { viewModel.retryLastRequest() }
            )
        }

        else -> {}
    }
}

@Composable
fun ReplyLoadingScreen(
    onCompleteClick: () -> Unit,
    onBackClick: () -> Unit,
    replyLoadingState: ReplyLoadingState.Success,
    onShowAdClick: () -> Unit,
    isAdLoading: Boolean,
    isWaitingForPatchResponse: Boolean,
    isAdCompleted: Boolean,
    adErrorMessage: String?,
    onDismissToast: () -> Unit
) {
    var remainingTime by remember { mutableStateOf(0L) }
    var isComplete by remember { mutableStateOf(false) }

    val animationResId = if (isComplete) {
        R.raw.excepted_rody
    } else {
        R.raw.writing_rody
    }

    LaunchedEffect(replyLoadingState) {
        val targetDateTime = replyLoadingState.targetDateTime
        val currentDateTime = LocalDateTime.now()

        val timeDiff = java.time.Duration.between(currentDateTime, targetDateTime).seconds
        remainingTime = if (timeDiff > 0) timeDiff else 0
        isComplete = remainingTime <= 0
    }

    LaunchedEffect(remainingTime) {
        if (remainingTime > 0) {
            while (remainingTime > 0) {
                delay(1000L)
                remainingTime--
            }
            isComplete = true
        }
    }

    val hours = (remainingTime / 3600).toInt()
    val minutes = ((remainingTime % 3600) / 60).toInt()
    val seconds = (remainingTime % 60).toInt()

    val loadingMessage = stringResource(id = R.string.loading_message)
    val nearlyDoneMessage = stringResource(id = R.string.loading_nearly_done_message)
    val completeMessage = stringResource(id = R.string.loading_complete_message)

    // 메시지 분기
    val textToShow = when {
        isComplete -> completeMessage
        isWaitingForPatchResponse -> nearlyDoneMessage
        isAdCompleted -> completeMessage
        else -> loadingMessage
    }

    Scaffold(
        topBar = {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .padding(top = 6.dp)
                    .padding(start = 8.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_nickname_back),
                    contentDescription = null
                )
            }
        },
        bottomBar = {
            ClodyButton(
                onClick = {
                    if (isComplete) AmplitudeUtils.trackEvent(eventName = AmplitudeConstraints.WAITING_DIARY_REPLY)
                    onCompleteClick()
                },
                text = stringResource(R.string.loading_button_open),
                enabled = isComplete,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 28.dp)
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(ClodyTheme.colors.white)
                    .padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.heightForScreenPercentage(0.15f))

                Crossfade(
                    targetState = animationResId,
                    animationSpec = tween(durationMillis = 300)
                ) { targetResId ->
                    LottieAnimation(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(16f / 9f),
                        resId = targetResId,
                        iterations = LottieConstants.IterateForever
                    )
                }
                Spacer(modifier = Modifier.heightForScreenPercentage(0.015f))

                if (!isWaitingForPatchResponse) {
                    Text(
                        text = String.format("%02d:%02d:%02d", hours, minutes, seconds),
                        style = ClodyTheme.typography.head2,
                        color = ClodyTheme.colors.gray01
                    )
                }
                Spacer(modifier = Modifier.heightForScreenPercentage(0.005f))
                Text(
                    text = textToShow,
                    style = ClodyTheme.typography.body2Medium,
                    color = ClodyTheme.colors.gray04,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.heightForScreenPercentage(0.036f))
                if (!isWaitingForPatchResponse && !isComplete) {
                    QuickReplyAdButton(
                        onClick = onShowAdClick
                    )
                }
            }
        }
    )

    if (isAdLoading) {
        LoadingScreen(
            backgroundColor = Color.Transparent
        )
    }

    if (adErrorMessage != null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(6.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            ClodyToastMessage(
                message = adErrorMessage,
                iconResId = R.drawable.ic_toast_check_on_18,
                backgroundColor = ClodyTheme.colors.gray04,
                contentColor = ClodyTheme.colors.white,
                durationMillis = 3000,
                onDismiss = onDismissToast
            )
        }
    }
}

@Composable
@Preview
fun ReplyLoadingScreenPreview() {
    ReplyLoadingScreen(
        onCompleteClick = {},
        onBackClick = {},
        replyLoadingState = ReplyLoadingState.Success(
            targetDateTime = LocalDateTime.now().plusSeconds(10)
        ),
        onShowAdClick = {},
        isAdLoading = false,
        isWaitingForPatchResponse = false,
        isAdCompleted = false,
        adErrorMessage = "잠시후 다시 시도해주세요!",
        onDismissToast = {}
    )
}
