package com.sopt.clody.presentation.ui.writediary.component.tooltip

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.sopt.clody.R
import com.sopt.clody.ui.theme.ClodyTheme

@Composable
fun TooltipIcon(
    modifier: Modifier = Modifier,
    tooltipsText: String,
) {
    Tooltip(
        modifier = modifier,
        tooltip = tooltipsText,
    )
}

@Composable
private fun Tooltip(
    modifier: Modifier,
    tooltip: String,
) {
    var isShowTooltip by remember { mutableStateOf(false) }

    TooltipPopup(
        requesterView = { innerModifier ->
            Icon(
                modifier = innerModifier
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { isShowTooltip = true },
                painter = painterResource(id = R.drawable.ic_writediary_help),
                contentDescription = "TooltipPopup",
                tint = ClodyTheme.colors.gray07,
            )
        },
        isShowTooltip = isShowTooltip,
        onDismissRequest = { isShowTooltip = false },
        tooltipContent = {
            Box(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .padding(vertical = 4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = tooltip,
                        style = ClodyTheme.typography.detail1Medium,
                        color = ClodyTheme.colors.blue,
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.ic_writediary__help_close),
                        contentDescription = "Close",
                        tint = ClodyTheme.colors.blue,
                        modifier = Modifier
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) { isShowTooltip = false }
                    )
                }
            }
        }
    )
}
