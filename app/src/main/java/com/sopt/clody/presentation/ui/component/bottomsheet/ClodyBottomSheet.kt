package com.sopt.clody.presentation.ui.component.bottomsheet

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sopt.clody.ui.theme.ClodyTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClodyBottomSheet(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
    onDismissRequest: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    ModalBottomSheet(
        onDismissRequest = {
            onDismissRequest()
        },
        sheetState = sheetState,
        dragHandle = null,
        windowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 0.dp),
        modifier = modifier
            .navigationBarsPadding()
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .pointerInput(Unit) {
                    detectVerticalDragGestures { _, _ -> }
                }
        ) {
            content()
        }

        LaunchedEffect(Unit) {
            scope.launch { sheetState.show() }
        }
    }
}

@Preview
@Composable
fun ClodyBottomSheetPreview() {
    ClodyBottomSheet(
        content = {},
        onDismissRequest = {}
    )
}
