package com.sopt.clody.presentation.ui.component.timepicker

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.sopt.clody.ui.theme.ClodyTheme
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ClodyPicker(
    modifier: Modifier = Modifier,
    items: List<String>,
    state: PickerState = rememberPickerState(),
    startIndex: Int = 0,
    visibleItemsCount: Int,
    textModifier: Modifier = Modifier,
    infiniteScroll: Boolean = true
) {
    val visibleItemsMiddle = visibleItemsCount / 2
    val listScrollCount = if (infiniteScroll) Integer.MAX_VALUE else items.size + visibleItemsMiddle * 2
    val listScrollMiddle = listScrollCount / 2
    val listStartIndex = if (infiniteScroll) {
        listScrollMiddle - listScrollMiddle % items.size - visibleItemsMiddle + startIndex
    } else {
        startIndex
    }

    fun getItem(index: Int) = items.getOrNull(index % items.size) ?: ""

    val listState = rememberLazyListState(initialFirstVisibleItemIndex = listStartIndex)
    val flingBehavior = rememberSnapFlingBehavior(lazyListState = listState)

    val itemHeightPixels = remember { mutableIntStateOf(0) }
    val itemHeightDp = with(LocalDensity.current) { itemHeightPixels.intValue.toDp() }

    val fadingEdgeGradient = remember {
        Brush.verticalGradient(
            0f to Color.White.copy(alpha = 0.9f),
            0.1f to Color.White.copy(alpha = 0.8f),
            0.2f to Color.White.copy(alpha = 0.7f),
            0.3f to Color.White.copy(alpha = 0.6f),
            0.4f to Color.Transparent,
            0.5f to Color.Transparent,
            0.6f to Color.Transparent,
            0.7f to Color.White.copy(alpha = 0.7f),
            0.8f to Color.White.copy(alpha = 0.8f),
            0.9f to Color.White.copy(alpha = 0.9f),
        )
    }

    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .map { index -> getItem(index + visibleItemsMiddle) }
            .distinctUntilChanged()
            .collect { item -> state.selectedItem = item }
    }

    Box(modifier = modifier) {
        LazyColumn(
            state = listState,
            flingBehavior = flingBehavior,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .height(itemHeightDp * visibleItemsCount)
                .pointerInput(Unit) {
                    detectVerticalDragGestures { change, dragAmount ->
                        change.consume()
                    }
                }
                .drawWithContent {
                    drawContent()
                    drawRect(fadingEdgeGradient, size = size)
                }
        ) {
            items(listScrollCount) { index ->
                if (!infiniteScroll && (index < visibleItemsMiddle || index >= items.size + visibleItemsMiddle)) {
                    Text(
                        text = "",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = ClodyTheme.typography.head3Medium.copy(color = ClodyTheme.colors.gray01),
                        modifier = Modifier.height(itemHeightDp)
                    )
                } else {
                    Text(
                        text = getItem(index),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = ClodyTheme.typography.head3Medium.copy(color = ClodyTheme.colors.gray01),
                        modifier = Modifier
                            .onSizeChanged { size -> itemHeightPixels.intValue = size.height }
                            .then(textModifier)
                    )
                }
            }
        }
    }
}
