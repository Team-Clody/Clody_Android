package com.sopt.clody.presentation.ui.writediary.component.tooltip

import androidx.compose.ui.unit.IntOffset

data class TooltipPopupPosition(
    val offset: IntOffset = IntOffset(0, 0),
    val alignment: TooltipAlignment = TooltipAlignment.TopCenter,
    val centerPositionX: Float = 0f,
)
