package jpark.bro.preferencesetup

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import jpark.bro.ui.APBaseTextField
import jpark.bro.ui.APBaseTopAppBar
import jpark.bro.ui.APFilterBottomSheet
import jpark.bro.ui.APFilterTriggerChip
import jpark.bro.ui.DraggableStarRating
import jpark.bro.ui.FilterParams
import jpark.bro.ui.FilterType
import jpark.bro.ui.theme.APColors

@Composable
fun PreferenceSetup(
    onNavigateToHome: () -> Unit,
    viewModel: PreferenceSetupViewModel = hiltViewModel()
) {
    val searchText by viewModel.searchText.collectAsState()
    val yearFilter by viewModel.yearFilter.collectAsState()
    val quarterFilter by viewModel.quarterFilter.collectAsState()
    val genreFilter by viewModel.genreFilter.collectAsState()
    val showBottomSheet by viewModel.showBottomSheet.collectAsState()
    val filterType by viewModel.filterType.collectAsState()

    val years = mutableListOf<String>()
    for (year in 1940..2026) {
        years.add(year.toString())
    }
    years.add("전체년도")

    val quarters = mutableListOf("전체분기")
    for (quarter in 1..4) {
        quarters.add("${quarter}분기")
    }

    val genres = mutableListOf<String>()
    val animeGenres = listOf(
        "액션", "모험", "코미디", "드라마", "판타지",
        "공포", "미스터리", "로맨스", "SF", "스포츠",
        "학원물", "일상", "음악", "심리", "역사"
    )
    genres.addAll(animeGenres)

    PreferenceSetup(
        searchText = searchText,
        yearFilter = yearFilter,
        quarterFilter = quarterFilter,
        genreFilter = genreFilter,
        showBottomSheet = showBottomSheet,
        filterType = filterType,
        yearItems = years,
        quarterItems = quarters,
        genreItems = genres,
        onBottomSheetStateChange = viewModel::updateShowBottomSheet,
        onSearchTextChange = viewModel::updateSearch,
        onFilterTypeChange = viewModel::updateFilterType,
        onApply = viewModel::searchByFilters,
        onNavigateToHome = onNavigateToHome,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun PreferenceSetup(
    searchText: String = "",
    yearFilter: String = "",
    quarterFilter: String = "",
    genreFilter: String = "",
    showBottomSheet: Boolean = false,
    filterType: FilterType = FilterType.YEAR_AND_QUARTER,
    yearItems: List<String> = emptyList<String>(),
    quarterItems: List<String> = emptyList<String>(),
    genreItems: List<String> = emptyList<String>(),
    onBottomSheetStateChange: (Boolean) -> Unit = {},
    onSearchTextChange: (String) -> Unit = {},
    onFilterTypeChange: (FilterType) -> Unit = {},
    onApply: (params: FilterParams) -> Unit = {},
    onNavigateToHome: () -> Unit = {},
) {
    val focusManager = LocalFocusManager.current
    val interactionSource = remember { MutableInteractionSource() }

    Scaffold(
        topBar = {
            APBaseTopAppBar(
                actions = {
                    TextButton(
                        onClick = { onNavigateToHome() }
                    ) {
                        Text(
                            text = "건너뛰기",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.W500,
                            color = Color(0xFFC9C9C9)
                        )
                    }
                }
            )
        },
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                focusManager.clearFocus()
            },
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "좋아하는 애니메이션을 평가해주세요.\n취향에 맞는 작품을 추천할게요.",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.W600,
                    color = APColors.Black,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "좋아하는 애니메이션을 골라 주세요.",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W500,
                    color = APColors.Secondary,
                )
                Spacer(modifier = Modifier.height(40.dp))
                Column {
                    Text(
                        text = "평가한 작품 0", // TODO
                        fontSize = 14.sp,
                        fontWeight = FontWeight.W500,
                        color = APColors.Gray
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    APBaseTextField(
                        value = searchText,
                        onValueChange = { onSearchTextChange(it) },
                        placeholder = {
                            Text(
                                text = "검색",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.W500,
                                color = APColors.TextGray
                            )
                        },
                        keyboardActions = KeyboardActions(
                            // TODO 검색 api
                            onDone = null
                        ),
                        leadingIcon = {
                            Icon(
                                painter = painterResource(R.drawable.ic_search_outline),
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(end = 12.dp)
                                    .size(16.dp),
                                tint = APColors.TextGray
                            )
                        },
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    // TODO
                                }
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_close),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(16.dp),
                                    tint = APColors.TextGray
                                )
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        APFilterTriggerChip(
                            title = if (yearFilter == "전체년도") "년도" else yearFilter,
                            isSelected = yearFilter != "전체년도",
                            onClick = {
                                onFilterTypeChange(FilterType.YEAR_AND_QUARTER)
                                onBottomSheetStateChange(true)
                                focusManager.clearFocus()
                            }
                        )
                        APFilterTriggerChip(
                            title = if (quarterFilter == "전체분기") "분기" else quarterFilter,
                            isSelected = quarterFilter != "전체분기",
                            onClick = {
                                onFilterTypeChange(FilterType.YEAR_AND_QUARTER)
                                onBottomSheetStateChange(true)
                                focusManager.clearFocus()
                            }
                        )
                        APFilterTriggerChip(
                            title = if (genreFilter.isEmpty()) "장르" else genreFilter,
                            isSelected = !genreFilter.isEmpty(),
                            onClick = {
                                onFilterTypeChange(FilterType.GENRE)
                                onBottomSheetStateChange(true)
                                focusManager.clearFocus()
                            }
                        )
                    }
                }
            }
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                thickness = 2.dp,
                color = APColors.Surface
            )
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
            ) {
                items(10) {
                    AnimeRatingCard(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                    )
                }
            }
            Column(
                modifier = Modifier
                    .padding(bottom = 40.dp)
            ) {
                HorizontalDivider(
                    modifier = Modifier
                        .padding(bottom = 32.dp),
                    color = APColors.Gray
                )
                Button(
                    onClick = {
                        // TODO
                        // 1. 서버에 데이터 전송

                        // 2. Home
                        onNavigateToHome()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = APColors.Primary,
                        disabledContainerColor = APColors.Gray
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = "완료",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W600,
                        color = Color(0xFFFFFFFF)
                    )
                }
            }
        }
    }

    val genreFilterList = if (genreFilter.isNotEmpty()) listOf(genreFilter) else emptyList()
    APFilterBottomSheet(
        showSheet = showBottomSheet,
        initType = filterType,
        includeTypeFilter = false,
        allowMultipleSelection = false,
        onDismiss = { onBottomSheetStateChange(false) },
        yearItems = yearItems,
        quarterItems = quarterItems,
        genreItems = genreItems,
        selectedYear = yearFilter,
        selectedQuarter = quarterFilter,
        selectedGenres = genreFilterList,
        onFilterTypeChange = onFilterTypeChange,
        onApply = {
            onApply(it)
            onBottomSheetStateChange(false)
        },
    )
}

@Composable
private fun AnimeRatingCard(
    modifier: Modifier = Modifier
) {
    var rating by remember { mutableFloatStateOf(0f) }
    var showRating by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .padding(top = 16.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(2.dp, APColors.Surface, RoundedCornerShape(8.dp))
                .clip(RoundedCornerShape(8.dp))
                .clickable { showRating = !showRating }
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                Icons.Default.Face,
                contentDescription = null,
                modifier = Modifier
                    .size(width = 132.dp, height = 88.dp)
                    .background(Color.LightGray)
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "애니메이션 제목",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W500,
                    color = APColors.Black
                )
                Text(
                    text = "드라마",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W500,
                    color = APColors.TextGray
                )
            }
        }
        if (showRating) {
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(APColors.Surface)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                DraggableStarRating(
                    starSize = 28.dp,
                    initialRating = rating,
                    onRatingChanged = { rating = it },
                )
                Text(
                    text = "(${rating}/5.0)",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W500,
                    color = if (rating == 0f) APColors.Gray else APColors.Point
                )
                TextButton(
                    onClick = {},
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.White,
                        containerColor = APColors.Primary,
                        disabledContainerColor = APColors.Gray
                    ),
                    enabled = rating != 0f
                ) {
                    Text(
                        text = "평가하기",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.W500
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreferenceSetupPreview() {
    PreferenceSetup(
        onNavigateToHome = {},
        searchText = "",
    )
}