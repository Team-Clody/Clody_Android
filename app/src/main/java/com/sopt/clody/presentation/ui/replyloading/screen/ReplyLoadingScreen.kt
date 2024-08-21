package com.sopt.clody.presentation.ui.replyloading.screen

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
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
        viewModel.getDiaryTime(year, month, day)
    }

    var backPressedTime by remember { mutableStateOf(0L) }
    val backPressThreshold = 2000

    BackHandler {
        val currentTime = System.currentTimeMillis()
        if (currentTime - backPressedTime <= backPressThreshold) {
            navigator.navigateHome()
        } else {
            backPressedTime = currentTime
        }
    }

    when (replyLoadingState) {
        is ReplyLoadingState.Loading -> {
            LoadingScreen()
        }
        is ReplyLoadingState.Success -> {
            ReplyLoadingScreen(
                onCompleteClick = { navigator.navigateReplyDiary(year, month, day, replyStatus) },
                onBackClick = { navigator.navigateBack(year, month, from) },
                replyLoadingState = replyLoadingState
            )
        }
        is ReplyLoadingState.Failure -> {
            FailureScreen(
                message = (replyLoadingState as ReplyLoadingState.Failure).error,
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
    replyLoadingState: ReplyLoadingState
) {
    var remainingTime by remember { mutableStateOf(0L) }
    var isComplete by remember { mutableStateOf(false) }

    LaunchedEffect(replyLoadingState) {
        if (replyLoadingState is ReplyLoadingState.Success) {
            val targetDateTime = (replyLoadingState as ReplyLoadingState.Success).targetDateTime
            val currentDateTime = LocalDateTime.now()

            val timeDiff = java.time.Duration.between(currentDateTime, targetDateTime).seconds
            remainingTime = if (timeDiff > 0) timeDiff else 0
            isComplete = remainingTime <= 0
        }
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
    val completeMessage = stringResource(id = R.string.complete_message)

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(ClodyTheme.colors.white)
            .padding(horizontal = 12.dp)
    ) {
        val (backButton, animation, timer, message, completeButton) = createRefs()
        val guideline = createGuidelineFromTop(0.25f)

        val fadeInOutAnimationSpec = tween<Float>(durationMillis = 3000) // 3000ms = 3s

        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .padding(top = 6.dp)
                .constrainAs(backButton) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_nickname_back),
                contentDescription = null,
            )
        }
        Box(
            modifier = Modifier
                .constrainAs(animation) {
                    top.linkTo(guideline)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                }
        ) {
            AnimatedVisibility(
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

            AnimatedVisibility(
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

        Text(
            text = String.format("%02d:%02d:%02d", hours, minutes, seconds),
            style = ClodyTheme.typography.head2,
            color = ClodyTheme.colors.gray01,
            modifier = Modifier.constrainAs(timer) {
                top.linkTo(animation.bottom, margin = 24.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        )

        Text(
            text = if (isComplete) completeMessage else loadingMessage,
            style = ClodyTheme.typography.body2Medium,
            color = ClodyTheme.colors.gray04,
            modifier = Modifier
                .constrainAs(message) {
                    top.linkTo(timer.bottom, margin = 8.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )

        ClodyButton(
            onClick = { onCompleteClick() },
            text = if (isComplete) "열어보기" else "확인",
            enabled = isComplete,
            modifier = Modifier
                .constrainAs(completeButton) {
                    bottom.linkTo(parent.bottom, margin = 26.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                }
                .padding(horizontal = 12.dp)
        )
    }
}
