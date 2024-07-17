package com.sopt.clody.presentation.ui.replyloading.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.sopt.clody.R
import com.sopt.clody.presentation.ui.component.button.ClodyButton
import com.sopt.clody.presentation.ui.replyloading.navigation.ReplyLoadingNavigator
import com.sopt.clody.ui.theme.ClodyTheme
import kotlinx.coroutines.delay

@Composable
fun ReplyLoadingRoute(
    navigator: ReplyLoadingNavigator,
) {
    ReplyLoadingScreen()
}
@Composable
fun ReplyLoadingScreen() {
    var remainingTime by remember { mutableStateOf(1 * 10) }
    var isComplete by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        while (remainingTime > 0) {
            delay(1000L)
            remainingTime--
        }
        isComplete = true
    }

    val hours = remainingTime / 3600
    val minutes = (remainingTime % 3600) / 60
    val seconds = remainingTime % 60

    val loadingMessage = stringResource(id = R.string.loading_message)
    val completeMessage = stringResource(id = R.string.complete_message)

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(ClodyTheme.colors.white)
            .padding(horizontal = 24.dp)
    ) {
        val (animation, timer, message, completeButton) = createRefs()
        val guideline = createGuidelineFromTop(0.25f)

        val fadeInOutAnimationSpec = tween<Float>(durationMillis = 3000) // 3000ms = 3s

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
                top.linkTo(animation.bottom, margin = 12.dp)
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
                    top.linkTo(timer.bottom, margin = 4.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )

        ClodyButton(
            onClick = { /*TODO*/ },
            text = "확인",
            enabled = isComplete,
            modifier = Modifier.constrainAs(completeButton) {
                bottom.linkTo(parent.bottom, margin = 26.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewReplyLoadingScreen() {
    ReplyLoadingScreen()
}
