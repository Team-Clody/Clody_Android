package com.sopt.clody.presentation.ui.component.popup

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.sopt.clody.ui.theme.ClodyTheme

@Composable
fun ClodyPopupBottomSheet(
    onDismissRequest: () -> Unit,
    content: @Composable () -> Unit
) {
    var isVisible by remember { mutableStateOf(true) }

    if (isVisible) {
        Box(
            Modifier
                .fillMaxSize()
                .background(Color(0x70000000))
                .clickable(onClick = { isVisible = false; onDismissRequest() })
        ) {
            Popup(  // 팝업 컴포넌트
                alignment = Alignment.BottomCenter,
                onDismissRequest = { isVisible = false; onDismissRequest() },
                properties = PopupProperties(
                    focusable = true,
                    dismissOnBackPress = true,
                    dismissOnClickOutside = true,
                    excludeFromSystemGesture = false
                )
            ) {
                AnimatedVisibility(
                    visible = isVisible,
                    enter = slideInVertically(
                        initialOffsetY = { it },
                        animationSpec = tween(durationMillis = 300)
                    ),
                    exit = slideOutVertically(
                        targetOffsetY = { it },
                        animationSpec = tween(durationMillis = 300)
                    )
                ) {
                    Surface(  // 팝업의 내용을 담을 서피스
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .align(Alignment.BottomCenter),
                        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                        color = ClodyTheme.colors.white
                    ) {
                        content()  // 팝업 내부에 제공된 컴포저블 콘텐츠를 렌더링
                    }
                }
            }
        }
    }
}
