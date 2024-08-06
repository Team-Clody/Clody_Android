package com.sopt.clody.presentation.ui.writediary.component.tooltip

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalView

@Composable
fun TooltipPopup(
    modifier: Modifier = Modifier,
    requesterView: @Composable (Modifier) -> Unit,
    tooltipContent: @Composable () -> Unit,
    isShowTooltip: Boolean,
    onDismissRequest: () -> Unit
) {
    val view = LocalView.current.rootView
    var position by remember { mutableStateOf(TooltipPopupPosition()) }

    if (isShowTooltip) {
        DisplayTooltipPopup(
            position = position,
            onDismissRequest = onDismissRequest,
            content = tooltipContent
        )
    }

    requesterView(
        modifier
            .noRippleClickable {
                position = calculateTooltipPopupPosition(view, coordinates = null, isTop = true)
            }
            .onGloballyPositioned { coordinates ->
                position = calculateTooltipPopupPosition(view, coordinates, isTop = true)
            }
    )
}
