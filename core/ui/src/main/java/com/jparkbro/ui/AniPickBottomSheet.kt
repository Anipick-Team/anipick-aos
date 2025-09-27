package com.jparkbro.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import com.jparkbro.model.common.MetaData
import com.jparkbro.model.common.ResponseMap
import com.jparkbro.ui.picker.APPicker
import com.jparkbro.ui.picker.PickerState
import com.jparkbro.ui.picker.rememberPickerState
import com.jparkbro.ui.theme.APColors
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun APExpireBottomSheet(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    onApply: (FilterParams) -> Unit,
    metaData: MetaData,
    includeYearQuarter: Boolean = true,
    includeGenres: Boolean = true,
    includeTypeFilter: Boolean = true,
    allowMultipleSelection: Boolean = false,
    initData: FilterParams,
    initFilterType: FilterType = FilterType.YEAR_AND_QUARTER,
    yearPickerState: PickerState<String> = rememberPickerState(initData.year),
    quarterPickerState: PickerState<String> = rememberPickerState(initData.quarter),
) {
    var filterType by remember { mutableStateOf(initFilterType) }
    var resetTrigger by remember { mutableIntStateOf(0) }
    var tempData by remember { mutableStateOf(initData) }

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )
    val scope = rememberCoroutineScope()

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        modifier = modifier,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp),
        scrimColor = APColors.ScrimColor,
        containerColor = APColors.White,
        dragHandle = null,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 380.dp),
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
                    if (includeYearQuarter) {
                        Text(
                            text = "년도/분기",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.W500,
                            color = if (filterType == FilterType.YEAR_AND_QUARTER) APColors.Black else APColors.TextGray,
                            modifier = Modifier
                                .padding(4.dp)
                                .clip(CircleShape)
                                .clickable { filterType = FilterType.YEAR_AND_QUARTER }
                                .padding(12.dp)
                        )
                    }
                    if (includeGenres) {
                        Text(
                            text = "장르",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.W500,
                            color = if (filterType == FilterType.GENRE) APColors.Black else APColors.TextGray,
                            modifier = Modifier
                                .padding(4.dp)
                                .clip(CircleShape)
                                .clickable { filterType = FilterType.GENRE }
                                .padding(12.dp)
                        )
                    }
                    if (includeTypeFilter) {
                        Text(
                            text = "타입",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.W500,
                            color = if (filterType == FilterType.TYPE) APColors.Black else APColors.TextGray,
                            modifier = Modifier
                                .padding(4.dp)
                                .clip(CircleShape)
                                .clickable { filterType = FilterType.TYPE }
                                .padding(12.dp)
                        )
                    }
                }
                Icon(
                    painter = painterResource(R.drawable.ic_close),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(4.dp)
                        .clip(CircleShape)
                        .clickable {
                            scope.launch {
                                sheetState.hide()
                            }.invokeOnCompletion {
                                onDismiss()
                            }
                        }
                        .padding(12.dp)
                        .size(16.dp)
                )
            }
            HorizontalDivider(modifier = Modifier.fillMaxWidth(), thickness = 2.dp, color = APColors.Surface)
            val yearItems = metaData.seasonYear.reversed() + listOf("전체년도")
            val quarterItems = listOf("전체분기") + metaData.season.map { it.name }
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
                        resetTrigger = resetTrigger,
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
                                checked = tempData.isMatchAllConditions,
                                onCheckedChange = { tempData = tempData.copy(isMatchAllConditions = !tempData.isMatchAllConditions) },
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
                                FilterType.GENRE -> metaData.genres
                                FilterType.TYPE -> metaData.type
                                else -> emptyList()
                            },
                            selectedItems = when (filterType) {
                                FilterType.GENRE -> tempData.genres
                                FilterType.TYPE -> listOfNotNull(tempData.type.takeIf { it.isNotEmpty() })
                                else -> emptyList()
                            },
                            selectionMode = when (filterType) {
                                FilterType.GENRE -> if (!allowMultipleSelection) SelectionMode.SINGLE else SelectionMode.MULTI
                                else -> SelectionMode.SINGLE
                            },
                            onItemSelected = {
                                when (filterType) {
                                    FilterType.GENRE -> {
                                        tempData = tempData.copy(genres = it as List<ResponseMap>)
                                    }

                                    FilterType.TYPE -> {
                                        tempData = tempData.copy(type = it.firstOrNull().toString())
                                    }

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
                    onClick = {
                        tempData = FilterParams()
                        resetTrigger += 1
                        yearPickerState.selectedItem = yearItems.lastOrNull() ?: "전체년도"
                        quarterPickerState.selectedItem = quarterItems.firstOrNull() ?: "전체분기"
                    },
                    colors = ButtonDefaults.buttonColors(
                        contentColor = APColors.TextGray,
                        containerColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier
                        .width(60.dp)
                        .height(32.dp),
                    contentPadding = PaddingValues(0.dp)
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
                            tempData.copy(
                                year = yearPickerState.selectedItem,
                                quarter = if (yearPickerState.selectedItem == "전체년도") "전체분기" else quarterPickerState.selectedItem,
                            )
                        )
                        scope.launch {
                            sheetState.hide()
                        }.invokeOnCompletion {
                            onDismiss()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.White,
                        containerColor = APColors.Primary
                    ),
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier
                        .width(60.dp)
                        .height(32.dp),
                    contentPadding = PaddingValues(0.dp)
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
}

@Composable
private fun YearQuarterPicker(
    yearItems: List<String> = emptyList(),
    quarterItems: List<String> = emptyList(),
    yearPickerState: PickerState<String> = rememberPickerState("전체년도"),
    quarterPickerState: PickerState<String> = rememberPickerState("전체분기"),
    resetTrigger: Int = 0,
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
            .width(96.dp),
        resetTrigger = resetTrigger,
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
        enabled = yearPickerState.selectedItem != "전체년도",
        resetTrigger = resetTrigger,
    )
}

@Composable
private fun FilterChipGroup(
    items: List<Any> = emptyList(),
    selectedItems: List<Any> = emptyList(),
    selectionMode: SelectionMode = SelectionMode.SINGLE,
    onItemSelected: (List<Any>) -> Unit = {},
) {
    items.forEach { item ->
        val displayText = when (item) {
            is String -> item
            is ResponseMap -> item.name
            else -> item.toString()
        }
        val isSelected = when (selectionMode) {
            SelectionMode.SINGLE -> selectedItems.firstOrNull() == item
            SelectionMode.MULTI -> item in selectedItems
        }

        Text(
            text = displayText,
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
                                onItemSelected(listOf(item))
                            }
                        }

                        SelectionMode.MULTI -> {
                            val newSelectedItems = selectedItems.toMutableList()
                            if (item in newSelectedItems) {
                                newSelectedItems.remove(item)
                            } else {
                                newSelectedItems.add(item)
                            }
                            onItemSelected(newSelectedItems)
                        }
                    }
                }
                .padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
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

data class SheetData(
    val type: FilterType,
    val initData: FilterParams,
    val onDismiss: () -> Unit,
    val onConfirm: (FilterParams) -> Unit,
)

data class FilterParams(
    val year: String = "전체년도",
    val quarter: String = "전체분기",
    val genres: List<ResponseMap> = emptyList(),
    val type: String = "",
    val isMatchAllConditions: Boolean = false,
)