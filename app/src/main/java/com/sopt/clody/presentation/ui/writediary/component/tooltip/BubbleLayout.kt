package com.sopt.clody.presentation.ui.writediary.component.tooltip

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import com.sopt.clody.ui.theme.ClodyTheme

@Composable
fun BubbleLayout(
    modifier: Modifier = Modifier,
    alignment: TooltipAlignment = TooltipAlignment.TopCenter,
    arrowHeight: Dp,
    arrowPositionX: Float,
    content: @Composable () -> Unit
) {
    val arrowHeightPx = with(LocalDensity.current) {
        arrowHeight.toPx()
    }
    val arrowColor = ClodyTheme.colors.lightBlue

    Box(
        modifier = modifier
            .drawBehind {
                if (arrowPositionX <= 0f) return@drawBehind

                val isBottomCenter = alignment == TooltipAlignment.BottomCenter

                val path = Path()

                if (isBottomCenter) {
                    val position = Offset(arrowPositionX, drawContext.size.height)
                    path.apply {
                        moveTo(x = position.x, y = position.y)
                        lineTo(x = position.x - arrowHeightPx, y = position.y)
                        lineTo(x = position.x, y = position.y + arrowHeightPx)
                        lineTo(x = position.x + arrowHeightPx, y = position.y)
                        lineTo(x = position.x, y = position.y)
                    }
                } else {
                    val position = Offset(arrowPositionX, 0f)
                    path.apply {
                        moveTo(x = position.x, y = position.y)
                        lineTo(x = position.x + arrowHeightPx, y = position.y)
                        lineTo(x = position.x, y = position.y - arrowHeightPx)
                        lineTo(x = position.x - arrowHeightPx, y = position.y)
                        lineTo(x = position.x, y = position.y)
                    }
                }

                drawPath(
                    path = path,
                    color = arrowColor,
                )
                path.close()
            }
    ) {
        content()
    }
}
