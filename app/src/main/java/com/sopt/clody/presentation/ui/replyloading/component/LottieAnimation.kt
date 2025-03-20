package com.sopt.clody.presentation.ui.replyloading.component

import androidx.annotation.RawRes
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition

@Composable
fun LottieAnimation(
    modifier: Modifier = Modifier,
    @RawRes resId: Int,
    iterations: Int = LottieConstants.IterateForever,
    contentScale: ContentScale = ContentScale.FillWidth,
    scaleXAdjustment: Float = 1f,
    scaleYAdjustment: Float = 1f,
    onAnimationEnd: (() -> Unit)? = null,
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(resId))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = iterations,
    )
    val alpha = remember { Animatable(0f) }

    LaunchedEffect(composition) {
        if (composition != null) {
            alpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 500),
            )
        }
    }

    LaunchedEffect(progress) {
        if (progress == 1f) {
            onAnimationEnd?.invoke()
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .graphicsLayer(
                scaleX = getScaleFromContentScale(contentScale) * scaleXAdjustment,
                scaleY = getScaleFromContentScale(contentScale) * scaleYAdjustment,
                alpha = alpha.value,
            ),
    ) {
        if (composition != null) {
            com.airbnb.lottie.compose.LottieAnimation(
                composition = composition,
                progress = { progress },
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

private fun getScaleFromContentScale(contentScale: ContentScale): Float {
    return when (contentScale) {
        ContentScale.Fit -> 1f
        ContentScale.FillWidth -> 1.2f
        ContentScale.FillHeight -> 1.5f
        else -> 1f
    }
}
