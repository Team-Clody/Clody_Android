package com.sopt.clody.presentation.utils.extension

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun Modifier.heightForScreenPercentage(percentage: Float): Modifier {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    return this.height(screenHeight * percentage)
}

@Composable
fun Modifier.widthForScreenPercentage(percentage: Float): Modifier {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    return this.width(screenWidth * percentage)
}

@Composable
fun Modifier.paddingForScreenPercentage(
    allPercentage: Float = 0f,
    horizontalPercentage: Float = 0f,
    verticalPercentage: Float = 0f,
    topPercentage: Float = 0f,
    bottomPercentage: Float = 0f,
): Modifier {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    val allPadding = screenHeight * allPercentage
    val horizontalPadding = if (allPercentage > 0f) allPadding else screenWidth * horizontalPercentage
    val verticalPadding = if (allPercentage > 0f) allPadding else screenHeight * verticalPercentage
    val topPadding = if (allPercentage > 0f) allPadding else screenHeight * topPercentage + verticalPadding
    val bottomPadding = if (allPercentage > 0f) allPadding else screenHeight * bottomPercentage + verticalPadding

    return this.padding(
        start = horizontalPadding,
        top = topPadding,
        end = horizontalPadding,
        bottom = bottomPadding,
    )
}

fun Modifier.showIf(condition: Boolean): Modifier {
    return if (condition) this else Modifier.size(0.dp)
}

fun Modifier.clickableWithoutRipple(
    enabled: Boolean = true,
    onClick: () -> Unit,
): Modifier = composed {
    this.clickable(
        indication = null,
        interactionSource = remember { MutableInteractionSource() },
        enabled = enabled
    ) {
        onClick()
    }
}

fun Modifier.roundedBackgroundWithBorder(
    cornerRadius: Dp,
    backgroundColor: Color,
    borderColor: Color = Color.Transparent,
    borderWidth: Dp = 0.dp,
): Modifier {
    return this
        .background(backgroundColor, shape = RoundedCornerShape(cornerRadius))
        .border(
            width = borderWidth,
            color = borderColor,
            shape = RoundedCornerShape(cornerRadius)
        )
}
