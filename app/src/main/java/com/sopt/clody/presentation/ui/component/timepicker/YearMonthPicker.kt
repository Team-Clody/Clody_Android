package com.sopt.clody.presentation.ui.component.timepicker

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.sopt.clody.R
import com.sopt.clody.presentation.ui.component.button.ClodyButton
import com.sopt.clody.presentation.utils.extension.YearMonthLabelUtil
import com.sopt.clody.presentation.utils.extension.toLocalizedMonthLabel
import com.sopt.clody.presentation.utils.extension.toLocalizedYearLabel
import com.sopt.clody.ui.theme.ClodyTheme
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import java.util.Locale

@Composable
fun YearMonthPicker(
    onDismissRequest: () -> Unit,
    selectedYear: Int,
    selectedMonth: Int,
    onYearMonthSelected: (Int, Int) -> Unit,
) {
    val yearItems = remember { (YearMonthLabelUtil.MIN_YEAR..YearMonthLabelUtil.MAX_YEAR).toList() }
    val monthItems = remember { (1..12).toList() }

    val yearLabelItems = yearItems.map { it.toLocalizedYearLabel() }
    val monthLabelItems = monthItems.map { it.toLocalizedMonthLabel() }

    val yearPickerState = rememberPickerState()
    val monthPickerState = rememberPickerState()

    val startYearIndex = (yearItems.indexOf(selectedYear) - 2).coerceAtLeast(0)
    val startMonthIndex = (monthItems.indexOf(selectedMonth) - 2).coerceAtLeast(0)

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        color = ClodyTheme.colors.white,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier
                .wrapContentSize()
                .background(color = ClodyTheme.colors.white)
                .padding(horizontal = 24.dp),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 30.dp),
            ) {
                Text(
                    text = stringResource(R.string.bottom_sheet_year_month_picker_title),
                    style = ClodyTheme.typography.body2SemiBold,
                    color = ClodyTheme.colors.gray01,
                    modifier = Modifier.align(Alignment.Center),
                )

                IconButton(
                    onClick = onDismissRequest,
                    modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.CenterEnd),
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_picker_dismiss),
                        contentDescription = null,
                    )
                }
            }

            Box(modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center)
                        .height(35.dp)
                        .background(ClodyTheme.colors.gray08, shape = RoundedCornerShape(8.dp)),
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    if (LocalConfiguration.current.locales[0] == Locale.KOREA) {
                        YearMonthPickerItem(
                            state = yearPickerState,
                            items = yearLabelItems,
                            startIndex = startYearIndex,
                            visibleItemsCount = 5,
                            infiniteScroll = false,
                            modifier = Modifier.weight(2f),
                            textModifier = Modifier.padding(8.dp),
                        )
                        Spacer(modifier = Modifier.width(20.dp))
                        YearMonthPickerItem(
                            state = monthPickerState,
                            items = monthLabelItems,
                            startIndex = startMonthIndex,
                            visibleItemsCount = 5,
                            infiniteScroll = false,
                            modifier = Modifier.weight(2f),
                            textModifier = Modifier.padding(8.dp),
                        )
                    } else {
                        YearMonthPickerItem(
                            state = monthPickerState,
                            items = monthLabelItems,
                            startIndex = startMonthIndex,
                            visibleItemsCount = 5,
                            infiniteScroll = false,
                            modifier = Modifier.weight(2f),
                            textModifier = Modifier.padding(8.dp),
                        )
                        Spacer(modifier = Modifier.width(20.dp))
                        YearMonthPickerItem(
                            state = yearPickerState,
                            items = yearLabelItems,
                            startIndex = startYearIndex,
                            visibleItemsCount = 5,
                            infiniteScroll = false,
                            modifier = Modifier.weight(2f),
                            textModifier = Modifier.padding(8.dp),
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                }
            }

            ClodyButton(
                onClick = {
                    val year = yearItems[yearLabelItems.indexOf(yearPickerState.selectedItem)]
                    val month = monthItems[monthLabelItems.indexOf(monthPickerState.selectedItem)]
                    onYearMonthSelected(year, month)
                    onDismissRequest()
                },
                text = stringResource(R.string.bottom_sheet_year_month_picker_btn_confirm),
                enabled = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 30.dp, bottom = 28.dp),
            )
        }
    }
}

@Composable
fun YearMonthPickerItem(
    modifier: Modifier = Modifier,
    items: List<String>,
    state: PickerState = rememberPickerState(),
    startIndex: Int = 0,
    visibleItemsCount: Int,
    textModifier: Modifier = Modifier,
    infiniteScroll: Boolean = true,
) {
    val visibleItemsMiddle = visibleItemsCount / 2
    val emptyItems = List(visibleItemsMiddle) { "" }
    val paddedItems = emptyItems + items + emptyItems
    val listScrollCount = if (infiniteScroll) Integer.MAX_VALUE else paddedItems.size
    val listScrollMiddle = listScrollCount / 2
    val listStartIndex = if (infiniteScroll) {
        listScrollMiddle - listScrollMiddle % paddedItems.size - visibleItemsMiddle + startIndex
    } else {
        startIndex + visibleItemsMiddle
    }

    fun getItem(index: Int) = paddedItems.getOrNull(index).orEmpty()

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
                },
        ) {
            items(listScrollCount) { index ->
                Text(
                    text = getItem(index),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = ClodyTheme.typography.head3Medium.copy(color = ClodyTheme.colors.gray01),
                    modifier = Modifier
                        .onSizeChanged { size -> itemHeightPixels.intValue = size.height }
                        .then(textModifier),
                )
            }
        }
    }
}
