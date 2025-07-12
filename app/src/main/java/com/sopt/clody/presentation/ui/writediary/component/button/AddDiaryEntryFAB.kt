package com.sopt.clody.presentation.ui.writediary.component.button

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sopt.clody.R
import com.sopt.clody.presentation.utils.extension.clickableWithoutRipple
import com.sopt.clody.ui.theme.ClodyTheme

@Composable
fun BoxScope.AddDiaryEntryFAB(
    isKeyboardVisible: Boolean,
    isMaxReached: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val containerColor = if (isMaxReached) ClodyTheme.colors.gray04 else ClodyTheme.colors.gray02
    val contentColor = ClodyTheme.colors.white

    AnimatedContent(
        targetState = isKeyboardVisible,
        transitionSpec = {
            fadeIn(tween(300)) togetherWith fadeOut(tween(300))
        },
        modifier = modifier
            .align(Alignment.BottomEnd)
            .imePadding()
            .padding(end = 24.dp, bottom = 12.dp),
        label = "FAB Animation",
    ) { keyboardVisible ->
        if (keyboardVisible) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(28.dp))
                    .background(containerColor)
                    .clickableWithoutRipple(enabled = !isMaxReached) {
                        onClick()
                    },
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_writediary_add),
                    contentDescription = null,
                    tint = contentColor,
                )
            }
        } else {
            Row(
                modifier = Modifier
                    .height(42.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(containerColor)
                    .clickableWithoutRipple(enabled = !isMaxReached) {
                        onClick()
                    }
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_writediary_add),
                    contentDescription = null,
                    tint = contentColor,
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = stringResource(R.string.write_diary_fab_add_entry),
                    color = contentColor,
                    style = ClodyTheme.typography.body2SemiBold,
                )
            }
        }
    }
}
