package com.jparkbro.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.jparkbro.model.common.MetaData
import com.jparkbro.model.common.ResponseMap
import com.jparkbro.model.enum.BottomSheetType
import com.jparkbro.ui.R
import com.jparkbro.ui.components.picker.APPicker
import com.jparkbro.ui.components.picker.PickerState
import com.jparkbro.ui.components.picker.rememberPickerState
import com.jparkbro.ui.model.BottomSheetParams
import com.jparkbro.ui.preview.DevicePreviews
import com.jparkbro.ui.theme.AniPick12Normal
import com.jparkbro.ui.theme.AniPick14Normal
import com.jparkbro.ui.theme.AniPick16Normal
import com.jparkbro.ui.theme.AniPick18ExtraBold
import com.jparkbro.ui.theme.AniPickBlack
import com.jparkbro.ui.theme.AniPickExtraSmallShape
import com.jparkbro.ui.theme.AniPickGray100
import com.jparkbro.ui.theme.AniPickGray400
import com.jparkbro.ui.theme.AniPickPrimary
import com.jparkbro.ui.theme.AniPickScrimColor
import com.jparkbro.ui.theme.AniPickSecondary
import com.jparkbro.ui.theme.AniPickSmallShape
import com.jparkbro.ui.theme.AniPickSurface
import com.jparkbro.ui.theme.AniPickWhite
import com.jparkbro.ui.theme.CloseIcon
import com.jparkbro.ui.util.extension.quarterStringToInt
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun APFilterBottomSheet(
    metaData: MetaData,
    initData: BottomSheetParams,
    includeYearQuarter: Boolean = false,
    includeGenres: Boolean = false,
    includeTypeFilter: Boolean = false,
    allowMultipleSelection: Boolean = false,
    initSheetType: BottomSheetType = BottomSheetType.YEAR_QUARTER,
    onDismiss: () -> Unit,
    onApply: (BottomSheetParams) -> Unit,
    modifier: Modifier = Modifier,
    yearPickerState: PickerState<String> = rememberPickerState(initData.year),
    quarterPickerState: PickerState<String> = rememberPickerState(initData.quarter.name),
) {
    var sheetType by rememberSaveable { mutableStateOf(initSheetType) }
    var resetTrigger by remember { mutableIntStateOf(0) }
    var tempData by remember { mutableStateOf(initData) }

    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        modifier = modifier,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp),
        scrimColor = AniPickScrimColor,
        containerColor = AniPickWhite,
        dragHandle = null
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
                            text = stringResource(R.string.bottom_sheet_year_quarter),
                            style = AniPick16Normal.copy(
                                color = if (sheetType == BottomSheetType.YEAR_QUARTER) AniPickBlack else AniPickGray400
                            ),
                            modifier = Modifier
                                .padding(dimensionResource(R.dimen.padding_extra_small))
                                .clip(AniPickSmallShape)
                                .clickable { sheetType = BottomSheetType.YEAR_QUARTER }
                                .padding(dimensionResource(R.dimen.padding_medium))
                        )
                    }
                    if (includeGenres) {
                        Text(
                            text = stringResource(R.string.bottom_sheet_genre),
                            style = AniPick16Normal.copy(
                                color = if (sheetType == BottomSheetType.GENRE) AniPickBlack else AniPickGray400
                            ),
                            modifier = Modifier
                                .padding(dimensionResource(R.dimen.padding_extra_small))
                                .clip(AniPickSmallShape)
                                .clickable { sheetType = BottomSheetType.GENRE }
                                .padding(dimensionResource(R.dimen.padding_medium))
                        )
                    }
                    if (includeTypeFilter) {
                        Text(
                            text = stringResource(R.string.bottom_sheet_type),
                            style = AniPick16Normal.copy(
                                color = if (sheetType == BottomSheetType.TYPE) AniPickBlack else AniPickGray400
                            ),
                            modifier = Modifier
                                .padding(dimensionResource(R.dimen.padding_extra_small))
                                .clip(AniPickSmallShape)
                                .clickable { sheetType = BottomSheetType.TYPE }
                                .padding(dimensionResource(R.dimen.padding_medium))
                        )
                    }
                }
                Icon(
                    imageVector = CloseIcon,
                    contentDescription = stringResource(R.string.close_icon),
                    modifier = Modifier
                        .padding(dimensionResource(R.dimen.padding_extra_small))
                        .clip(CircleShape)
                        .clickable {
                            scope.launch {
                                sheetState.hide()
                            }.invokeOnCompletion {
                                onDismiss()
                            }
                        }
                        .padding(dimensionResource(R.dimen.padding_medium))
                        .size(dimensionResource(R.dimen.icon_size_small))
                )
            }
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = dimensionResource(R.dimen.border_width_thick),
                color = AniPickSurface
            )
            val yearItems = metaData.seasonYear.reversed() + listOf("전체년도")
            val quarterItems = listOf("전체분기") + metaData.season.map { it.name }
            when (sheetType) {
                BottomSheetType.YEAR_QUARTER -> Row(
                    modifier = Modifier
                        .weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_small)),
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
                        .padding(
                            horizontal = dimensionResource(R.dimen.padding_default),
                            vertical = dimensionResource(R.dimen.padding_medium)
                        )
                        .weight(1f)
                ) {
                    if (allowMultipleSelection && sheetType == BottomSheetType.GENRE) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = dimensionResource(R.dimen.padding_medium)),
                            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_small), Alignment.End),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(R.string.bottom_sheet_match_all),
                                style = AniPick14Normal.copy(color = AniPickBlack),
                            )
                            APToggleSwitch(
                                checked = tempData.isMatchAllConditions,
                                onCheckedChange = { tempData = tempData.copy(isMatchAllConditions = !tempData.isMatchAllConditions) },
                            )
                        }
                    }
                    FlowRow(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_small)),
                        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_small)),
                    ) {
                        FilterChipGroup(
                            items = when (sheetType) {
                                BottomSheetType.GENRE -> metaData.genres
                                BottomSheetType.TYPE -> metaData.type
                                else -> emptyList()
                            },
                            selectedItems = when (sheetType) {
                                BottomSheetType.GENRE -> tempData.genres
                                BottomSheetType.TYPE -> listOfNotNull(tempData.type.takeIf { it.isNotEmpty() })
                                else -> emptyList()
                            },
                            selectionMode = when (sheetType) {
                                BottomSheetType.GENRE -> if (!allowMultipleSelection) SelectionMode.SINGLE else SelectionMode.MULTI
                                else -> SelectionMode.SINGLE
                            },
                            onItemSelected = {
                                when (sheetType) {
                                    BottomSheetType.GENRE -> {
                                        tempData = tempData.copy(genres = it as List<ResponseMap>)
                                    }

                                    BottomSheetType.TYPE -> {
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
                    .padding(vertical = dimensionResource(R.dimen.padding_small))
                    .padding(end = dimensionResource(R.dimen.padding_default)),
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_extra_small), Alignment.End),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(
                    onClick = {
                        tempData = BottomSheetParams()
                        resetTrigger += 1
                        yearPickerState.selectedItem = yearItems.lastOrNull() ?: "전체년도"
                        quarterPickerState.selectedItem = quarterItems.firstOrNull() ?: "전체분기"
                    },
                    colors = ButtonDefaults.buttonColors(
                        contentColor = AniPickGray400,
                        containerColor = Color.Transparent
                    ),
                    shape = AniPickExtraSmallShape,
                    modifier = Modifier
                        .width(60.dp)
                        .height(32.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(
                        text = stringResource(R.string.bottom_sheet_reset_btn),
                        style = AniPick14Normal
                    )
                }
                TextButton(
                    onClick = {
                        onApply(
                            tempData.copy(
                                year = yearPickerState.selectedItem,
                                quarter = if (yearPickerState.selectedItem == "전체년도") ResponseMap(name = "전체분기")
                                else ResponseMap(
                                    id = quarterPickerState.selectedItem.quarterStringToInt() ?: -1,
                                    name = quarterPickerState.selectedItem
                                ),
                            )
                        )
                        scope.launch {
                            sheetState.hide()
                        }.invokeOnCompletion {
                            onDismiss()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        contentColor = AniPickWhite,
                        containerColor = AniPickPrimary
                    ),
                    shape = AniPickExtraSmallShape,
                    modifier = Modifier
                        .width(60.dp)
                        .height(32.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(
                        text = stringResource(R.string.bottom_sheet_complete_btn),
                        style = AniPick12Normal
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
        textStyle = AniPick18ExtraBold.copy(color = AniPickGray400),
        selectedTextStyle = AniPick18ExtraBold.copy(color = AniPickSecondary),
        modifier = Modifier
            .width(96.dp),
        resetTrigger = resetTrigger,
    )
    APPicker(
        items = quarterItems,
        state = quarterPickerState,
        startIndex = quarterStartIndex,
        textStyle = AniPick18ExtraBold.copy(color = AniPickGray400),
        selectedTextStyle = AniPick18ExtraBold.copy(color = AniPickSecondary),
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
            style = AniPick14Normal.copy(color = if (isSelected) AniPickSecondary else AniPickBlack),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .clip(AniPickSmallShape)
                .border(
                    dimensionResource(R.dimen.border_width_default),
                    if (isSelected) AniPickSecondary else AniPickGray100,
                    AniPickSmallShape
                )
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
                .padding(horizontal = dimensionResource(R.dimen.padding_default), vertical = dimensionResource(R.dimen.padding_small))
        )
    }
}

@DevicePreviews
@Composable
private fun APFilterBottomSheetPreview() {
    APFilterBottomSheet(
        metaData = MetaData(),
        initData = BottomSheetParams(),
        onDismiss = {},
        onApply = {},
        includeYearQuarter = true,
        includeGenres = true,
        includeTypeFilter = true,
        initSheetType = BottomSheetType.GENRE,
        allowMultipleSelection = true
    )
}