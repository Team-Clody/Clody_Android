package com.sopt.clody.presentation.ui.replyloading.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.sopt.clody.R
import com.sopt.clody.presentation.ui.component.button.ClodyButton
import com.sopt.clody.ui.theme.ClodyTheme
import kotlinx.coroutines.delay

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

        Image(
            painterResource(id = if (isComplete) R.drawable.img_loading_complete else R.drawable.img_loading_wait),
            contentDescription = if (isComplete) "complete image" else "loading image",
            modifier = Modifier
                .constrainAs(animation) {
                    top.linkTo(guideline)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                }
        )

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
