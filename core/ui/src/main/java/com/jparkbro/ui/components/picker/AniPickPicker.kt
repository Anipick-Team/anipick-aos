package com.jparkbro.ui.components.picker

import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import com.jparkbro.ui.theme.AniPickGray100
import com.jparkbro.ui.theme.AniPickSecondary
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.mapNotNull
import kotlin.math.abs

@Composable
fun <T> APPicker(
    items: List<T>,
    state: PickerState<T>,
    modifier: Modifier = Modifier,
    startIndex: Int = 0,
    visibleItemsCount: Int = 5,
    textModifier: Modifier = Modifier,
    textStyle: TextStyle = LocalTextStyle.current,
    selectedTextStyle: TextStyle = LocalTextStyle.current,
    itemPadding: PaddingValues = PaddingValues(horizontal = 8.dp, vertical = 12.dp),
    fadingEdgeGradient: Brush = Brush.verticalGradient(
        0f to Color.Transparent,
        0.5f to Color.Black,
        1f to Color.Transparent
    ),
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    itemTextAlignment: Alignment.Vertical = Alignment.CenterVertically,
    enabled: Boolean = true,
    resetTrigger: Int = 0,
) {
    val density = LocalDensity.current
    val visibleItemsMiddle = remember { visibleItemsCount / 2 }

    val paddingItemsCount = visibleItemsCount / 2
    val adjustedItems = List(paddingItemsCount) { null } + items + List(paddingItemsCount) { null }
    val listScrollCount = adjustedItems.size

    val listStartIndex = remember { startIndex }

    fun getItem(index: Int) = adjustedItems[index % listScrollCount]

    val listState = rememberLazyListState(initialFirstVisibleItemIndex = listStartIndex)
    val flingBehavior = rememberSnapFlingBehavior(lazyListState = listState)

    val itemHeight = with(density) {
        val textHeightDp = if (selectedTextStyle.lineHeight.isSp) {
            selectedTextStyle.lineHeight.toDp()
        } else {
            selectedTextStyle.fontSize.toDp() * 1.2f
        }
        textHeightDp + itemPadding.calculateTopPadding() + itemPadding.calculateBottomPadding()
    }

    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo }
            .mapNotNull { layoutInfo ->
                val viewportCenter =
                    (layoutInfo.viewportEndOffset - layoutInfo.viewportStartOffset) / 2
                layoutInfo.visibleItemsInfo
                    .minByOrNull { abs((it.offset + it.size / 2) - viewportCenter) }
                    ?.index
            }
            .distinctUntilChanged()
            .mapNotNull { index -> getItem(index) }
            .collect { item -> state.selectedItem = item }
    }

    LaunchedEffect(resetTrigger) {
        resetTrigger.let {
            val targetIndex = items.indexOf(state.selectedItem)
            if (targetIndex != -1) {
                if (targetIndex in 0..<listScrollCount) {
                    listState.animateScrollToItem(targetIndex)
                }
            }
        }
    }

    Box(
        modifier = modifier
    ) {
        LazyColumn(
            state = listState,
            flingBehavior = flingBehavior,
            horizontalAlignment = horizontalAlignment,
            userScrollEnabled = enabled,
            modifier = Modifier
                .align(Alignment.Center)
                .wrapContentSize()
                .height(itemHeight * visibleItemsCount)
                .fadingEdge(fadingEdgeGradient)
        ) {
            items(
                listScrollCount,
                key = { it },
            ) { index ->
                val fraction by remember {
                    derivedStateOf {
                        val currentItem =
                            listState.layoutInfo.visibleItemsInfo.firstOrNull { it.key == index }
                        currentItem?.offset?.let { offset ->
                            val itemHeightPx = with(density) { itemHeight.toPx() }
                            val fraction =
                                (offset - itemHeightPx * visibleItemsMiddle) / itemHeightPx
                            abs(fraction.coerceIn(-1f, 1f))
                        } ?: 0f
                    }
                }

                val currentItemText by remember {
                    mutableStateOf(if (getItem(index) == null) "" else getItem(index).toString())
                }

                val textColor = if (enabled) {
                    lerp(
                        selectedTextStyle.color,
                        textStyle.color,
                        fraction
                    )
                } else {
                    AniPickGray100
                }

                Text(
                    text = currentItemText,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = textStyle.copy(
                        fontSize = lerp(
                            selectedTextStyle.fontSize,
                            textStyle.fontSize,
                            fraction
                        ),
                        color = textColor
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .height(itemHeight)
                        .wrapContentHeight(align = itemTextAlignment)
                        .fillParentMaxWidth()
                        .then(textModifier)
                        .semantics { contentDescription = currentItemText }
                )
            }
        }
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .height(itemHeight)
                .border(
                    2.dp,
                    if (enabled) AniPickSecondary else AniPickGray100,
                    RoundedCornerShape(8.dp)
                )
        )
    }
}

private fun Modifier.fadingEdge(brush: Brush) = this
    .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
    .drawWithContent {
        drawContent()
        drawRect(brush = brush, blendMode = BlendMode.DstIn)
    }