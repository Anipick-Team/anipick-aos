package jpark.bro.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.FlowRowScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jpark.bro.ui.picker.APPicker
import jpark.bro.ui.picker.PickerState
import jpark.bro.ui.picker.rememberPickerState
import jpark.bro.ui.theme.APColors

@Composable
fun APFilterBottomSheet(
    modifier: Modifier = Modifier,
    showSheet: Boolean = false,
    onDismiss: () -> Unit,
    initType: FilterType = FilterType.YEAR_AND_QUARTER,
    includeTypeFilter: Boolean = false,
    allowMultipleSelection: Boolean = false,
    yearItems: List<String> = emptyList<String>(),
    quarterItems: List<String> = emptyList<String>(),
    genreItems: List<String> = emptyList<String>(),
    typeItems: List<String> = emptyList<String>(),
    selectedYear: String = "",
    selectedQuarter: String = "",
    selectedGenres: List<String> = emptyList<String>(),
    selectedType: String = "",
    isMatchAllConditions: Boolean = false,
    onFilterTypeChange: (FilterType) -> Unit = {},
    onApply: (params: FilterParams) -> Unit = {},
) {
    var filterType by remember(initType) { mutableStateOf<FilterType>(initType) }
    var tempGenres by remember(showSheet, selectedGenres) { mutableStateOf(selectedGenres) }
    var tempType by remember(showSheet, selectedType) { mutableStateOf(selectedType) }
    var tempMatchAllConditions by remember(showSheet, isMatchAllConditions) { mutableStateOf(isMatchAllConditions) }

    val yearPickerState: PickerState<String> = rememberPickerState(selectedYear)
    val quarterPickerState: PickerState<String> = rememberPickerState(selectedQuarter)

    val bottomSheetState = remember(showSheet) {
        MutableTransitionState(false).apply {
            targetState = showSheet
        }
    }
    LaunchedEffect(showSheet) {
        bottomSheetState.targetState = showSheet
        if (!showSheet) {
            yearPickerState.selectedItem = selectedYear
            quarterPickerState.selectedItem = selectedQuarter
        }
    }
    val isAnimating = bottomSheetState.isIdle.not()
    val showSheet = bottomSheetState.targetState || isAnimating

    if (showSheet) {
        Box(
            modifier = modifier
                .fillMaxSize()
        ) {
            AnimatedVisibility(
                visibleState = bottomSheetState,
                enter = fadeIn(
                    animationSpec = tween(durationMillis = 300)
                ),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(APColors.ScrimColor)
                        .pointerInput(Unit) {
                            detectTapGestures {
                                onDismiss()
                            }
                        }
                )
            }
            AnimatedVisibility(
                visibleState = bottomSheetState,
                enter = slideInVertically(
                    initialOffsetY = { fullHeight -> fullHeight },
                    animationSpec = tween(
                        durationMillis = 400,
                        easing = FastOutSlowInEasing
                    )
                ),
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                BottomSheetContent(
                    modifier = Modifier
                        .pointerInput(Unit) {},
                    filterType = filterType,
                    includeTypeFilter = includeTypeFilter,
                    onDismiss = onDismiss,
                    onFilterTypeChange = onFilterTypeChange,
                    allowMultipleSelection = allowMultipleSelection,
                    yearItems = yearItems,
                    quarterItems = quarterItems,
                    genreItems = genreItems,
                    typeItems = typeItems,
                    tempGenres = tempGenres,
                    tempType = tempType,
                    tempMatchAllConditions = tempMatchAllConditions,
                    yearPickerState = yearPickerState,
                    quarterPickerState = quarterPickerState,
                    onGenresChange = { tempGenres = it },
                    onTypeChange = { tempType = it },
                    onMatchAllConditionToggle = { tempMatchAllConditions = !tempMatchAllConditions },
                    onResetFilter = {
                        tempGenres = emptyList()
                        tempType = ""
                        tempMatchAllConditions = false
                        yearPickerState.selectedItem = "전체년도"
                        quarterPickerState.selectedItem = "전체분기"
                    },
                    onApply = onApply
                )
            }
        }
    }
}

@Composable
private fun BottomSheetContent(
    modifier: Modifier = Modifier,
    filterType: FilterType = FilterType.YEAR_AND_QUARTER,
    includeTypeFilter: Boolean = false,
    onDismiss: () -> Unit = {},
    onFilterTypeChange: (FilterType) -> Unit = {},
    allowMultipleSelection: Boolean = false,
    yearItems: List<String> = emptyList(),
    quarterItems: List<String> = emptyList(),
    genreItems: List<String> = emptyList(),
    typeItems: List<String> = emptyList(),
    tempGenres: List<String> = emptyList(),
    tempType: String = "",
    tempMatchAllConditions: Boolean = false,
    yearPickerState: PickerState<String> = rememberPickerState(""),
    quarterPickerState: PickerState<String> = rememberPickerState(""),
    onGenresChange: (List<String>) -> Unit = {},
    onTypeChange: (String) -> Unit = {},
    onMatchAllConditionToggle: () -> Unit = {},
    onResetFilter: () -> Unit = {},
    onApply: (params: FilterParams) -> Unit = {},
) {
    Column(
        modifier = modifier
            .height(345.dp + WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding())
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White)
            .padding(bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "년도/분기",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W500,
                    color = if (filterType == FilterType.YEAR_AND_QUARTER) APColors.Black else APColors.TextGray,
                    modifier = Modifier
                        .padding(4.dp)
                        .clip(RoundedCornerShape(50))
                        .clickable { onFilterTypeChange(FilterType.YEAR_AND_QUARTER) }
                        .padding(12.dp)
                )
                Text(
                    text = "장르",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W500,
                    color = if (filterType == FilterType.GENRE) APColors.Black else APColors.TextGray,
                    modifier = Modifier
                        .padding(4.dp)
                        .clip(RoundedCornerShape(50))
                        .clickable { onFilterTypeChange(FilterType.GENRE) }
                        .padding(12.dp)
                )
                if (includeTypeFilter) {
                    Text(
                        text = "타입",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W500,
                        color = if (filterType == FilterType.TYPE) APColors.Black else APColors.TextGray,
                        modifier = Modifier
                            .padding(4.dp)
                            .clip(RoundedCornerShape(50))
                            .clickable { onFilterTypeChange(FilterType.TYPE) }
                            .padding(12.dp)
                    )
                }
            }
            Icon(
                painter = painterResource(R.drawable.ic_close),
                contentDescription = null,
                modifier = Modifier
                    .padding(4.dp)
                    .clip(RoundedCornerShape(50))
                    .clickable {
                        onResetFilter()
                        onDismiss()
                    }
                    .padding(12.dp)
                    .size(16.dp)
            )
        }
        HorizontalDivider(modifier = Modifier.fillMaxWidth(), thickness = 2.dp, color = APColors.Surface)
        when (filterType) {
            FilterType.YEAR_AND_QUARTER -> Row(
                modifier = Modifier
                    .weight(1f),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                YearQuarterPicker(
                    yearItems = yearItems,
                    quarterItems = quarterItems,
                    yearPickerState = yearPickerState,
                    quarterPickerState = quarterPickerState,
                )
            }
            else -> Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .weight(1f)
            ) {
                if (allowMultipleSelection && filterType == FilterType.GENRE) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "모든 조건 일치",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.W500,
                            color = APColors.Black,
                            modifier = Modifier
                                .padding(end = 8.dp)
                        )
                        APToggleSwitch(
                            checked = tempMatchAllConditions,
                            onCheckedChange = { onMatchAllConditionToggle() },
                            modifier = Modifier
                        )
                    }
                }
                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    FilterChipGroup(
                        items = when (filterType) {
                            FilterType.GENRE -> genreItems
                            FilterType.TYPE -> typeItems
                            else -> emptyList()
                        },
                        selectedItems = when (filterType) {
                            FilterType.GENRE -> tempGenres
                            FilterType.TYPE -> listOfNotNull(tempType.takeIf { it.isNotEmpty() })
                            else -> emptyList()
                        },
                        selectionMode = when (filterType) {
                            FilterType.GENRE -> if (!allowMultipleSelection) SelectionMode.SINGLE else SelectionMode.MULTI
                            else -> SelectionMode.SINGLE
                        },
                        onItemSelected = {
                            when (filterType) {
                                FilterType.GENRE -> onGenresChange(it)
                                FilterType.TYPE -> onTypeChange(it.firstOrNull() ?: "")
                                else -> {}
                            }
                        }
                    )
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .padding(end = 16.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(
                onClick = { onResetFilter() },
                colors = ButtonDefaults.buttonColors(
                    contentColor = APColors.TextGray,
                    containerColor = Color.Transparent
                ),
                shape = RoundedCornerShape(4.dp),
                modifier = Modifier
                    .width(60.dp)
                    .height(32.dp)
            ) {
                Text(
                    text = "초기화",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W500,
                )
            }
            Spacer(modifier = Modifier.width(4.dp))
            TextButton(
                onClick = {
                    onApply(
                        FilterParams(
                            year = yearPickerState.selectedItem,
                            quarter = if (yearPickerState.selectedItem == "전체년도") "전체분기" else quarterPickerState.selectedItem,
                            genres = tempGenres,
                            type = tempType,
                            isMatchAllConditions = tempMatchAllConditions
                        )
                    )
                },
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White,
                    containerColor = APColors.Primary
                ),
                shape = RoundedCornerShape(4.dp),
                modifier = Modifier
                    .width(60.dp)
                    .height(32.dp)
            ) {
                Text(
                    text = "완료",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.W500,
                )
            }
        }
    }
}

@Composable
private fun RowScope.YearQuarterPicker(
    yearItems: List<String> = emptyList<String>(),
    quarterItems: List<String> = emptyList<String>(),
    yearPickerState: PickerState<String> = rememberPickerState(""),
    quarterPickerState: PickerState<String> = rememberPickerState(""),
) {
    val yearStartIndex = remember {
        val index = yearItems.indexOf(yearPickerState.selectedItem)
        if (index == -1) 0 else index
    }
    val quarterStartIndex = remember {
        val index = quarterItems.indexOf(quarterPickerState.selectedItem)
        if (index == -1) 0 else index
    }

    APPicker(
        items = yearItems,
        state = yearPickerState,
        startIndex = yearStartIndex,
        textStyle = TextStyle(
            fontSize = 18.sp,
            fontWeight = FontWeight.W700,
            color = APColors.TextGray
        ),
        selectedTextStyle = TextStyle(
            fontSize = 18.sp,
            fontWeight = FontWeight.W700,
            color = APColors.Secondary
        ),
        modifier = Modifier
            .width(96.dp)
    )
    APPicker(
        items = quarterItems,
        state = quarterPickerState,
        startIndex = quarterStartIndex,
        textStyle = TextStyle(
            fontSize = 18.sp,
            fontWeight = FontWeight.W700,
            color = APColors.TextGray
        ),
        selectedTextStyle = TextStyle(
            fontSize = 18.sp,
            fontWeight = FontWeight.W700,
            color = APColors.Secondary
        ),
        modifier = Modifier
            .width(96.dp),
        enabled = yearPickerState.selectedItem != "전체년도"
    )
}

@Composable
private fun FlowRowScope.FilterChipGroup(
    items: List<String> = emptyList(),
    selectedItems: List<String> = emptyList(),
    selectionMode: SelectionMode = SelectionMode.SINGLE,
    onItemSelected: (List<String>) -> Unit = {},
) {
    items.forEach { genre ->
        val isSelected = when (selectionMode) {
            SelectionMode.SINGLE -> selectedItems.firstOrNull() == genre
            SelectionMode.MULTI -> genre in selectedItems
        }

        Text(
            text = genre,
            fontSize = 14.sp,
            fontWeight = FontWeight.W500,
            color = if (isSelected) APColors.Secondary else APColors.Black,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .border(1.dp, if (isSelected) APColors.Secondary else APColors.Gray, RoundedCornerShape(8.dp))
                .clickable {
                    when (selectionMode) {
                        SelectionMode.SINGLE -> {
                            if (isSelected) {
                                onItemSelected(emptyList())
                            } else {
                                onItemSelected(listOf(genre))
                            }
                        }
                        SelectionMode.MULTI -> {
                            val newSelectedItems = selectedItems.toMutableList()
                            if (genre in newSelectedItems) {
                                newSelectedItems.remove(genre)
                            } else {
                                newSelectedItems.add(genre)
                            }
                            onItemSelected(newSelectedItems)
                        }
                    }
                }
                .padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}

@Preview
@Composable
private fun BottomSheetContentPreview() {
    BottomSheetContent(
        onDismiss = {},
        allowMultipleSelection = true,
        filterType = FilterType.GENRE,
        yearItems = listOf("2020", "2021", "2024", "2025", "2026", "전체년도"),
        quarterItems = listOf("전체분기", "1분기", "2분기", "3분기", "4분기"),
        genreItems = listOf("액션", "모험", "코미디", "드라마", "판타지", "공포", "미스터리", "로맨스", "SF", "스포츠", "학원물", "일상", "음악", "심리", "역사")
    )
}

enum class FilterType {
    YEAR_AND_QUARTER,
    GENRE,
    TYPE,
}

enum class SelectionMode {
    SINGLE,
    MULTI
}

data class FilterParams(
    val year: String = "",
    val quarter: String = "",
    val genres: List<String> = emptyList(),
    val type: String = "",
    val isMatchAllConditions: Boolean = false,
)