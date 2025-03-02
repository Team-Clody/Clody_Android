package com.sopt.clody.presentation.ui.replyloading.screen

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.sopt.clody.R
import com.sopt.clody.presentation.ui.component.FailureScreen
import com.sopt.clody.presentation.ui.component.LoadingScreen
import com.sopt.clody.presentation.ui.component.button.ClodyButton
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
                replyLoadingState = successState
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
    replyLoadingState: ReplyLoadingState.Success
) {
    var remainingTime by remember { mutableStateOf(0L) }
    var isComplete by remember { mutableStateOf(false) }

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
    val completeMessage = stringResource(id = R.string.loading_complete_message)

    val fadeInOutAnimationSpec = tween<Float>(durationMillis = 3000) // 3000ms = 3s

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
                    if(isComplete) AmplitudeUtils.trackEvent(eventName = AmplitudeConstraints.WAITING_DIARY_REPLY)
                    onCompleteClick() },
                text = if (isComplete) stringResource(R.string.loading_button_open) else stringResource(R.string.loading_button_confirm),
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
                Spacer(modifier = Modifier.heightForScreenPercentage(0.2f))
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    this@Column.AnimatedVisibility(
                        visible = !isComplete,
                        enter = fadeIn(animationSpec = fadeInOutAnimationSpec),
                        exit = fadeOut(animationSpec = fadeInOutAnimationSpec)
                    ) {
                        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.writing_rody))
                        val progress by animateLottieCompositionAsState(composition, iterations = LottieConstants.IterateForever)
                        LottieAnimation(
                            composition,
                            { progress },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    this@Column.AnimatedVisibility(
                        visible = isComplete,
                        enter = fadeIn(animationSpec = fadeInOutAnimationSpec),
                        exit = fadeOut(animationSpec = fadeInOutAnimationSpec)
                    ) {
                        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.excepted_rody))
                        val progress by animateLottieCompositionAsState(composition, iterations = LottieConstants.IterateForever)
                        LottieAnimation(
                            composition,
                            { progress },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                Spacer(modifier = Modifier.heightForScreenPercentage(0.015f))
                Text(
                    text = String.format("%02d:%02d:%02d", hours, minutes, seconds),
                    style = ClodyTheme.typography.head2,
                    color = ClodyTheme.colors.gray01
                )
                Spacer(modifier = Modifier.heightForScreenPercentage(0.005f))
                Text(
                    text = if (isComplete) completeMessage else loadingMessage,
                    style = ClodyTheme.typography.body2Medium,
                    color = ClodyTheme.colors.gray04
                )
            }
        }
    )
}
