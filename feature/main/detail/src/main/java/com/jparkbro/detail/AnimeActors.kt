package com.jparkbro.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.jparkbro.model.detail.AnimeActorsResponse
import com.jparkbro.model.detail.DetailActor
import com.jparkbro.ui.APTitledBackTopAppBar
import com.jparkbro.ui.theme.APColors
import com.jparkbro.ui.util.calculateCardWidth
import com.jparkbro.ui.util.calculateItemSpacing
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

@Composable
internal fun AnimeActors(
    onNavigateBack: () -> Unit,
    onNavigateToActorDetail: (Int) -> Unit,
    viewModel: AnimeActorsViewModel = hiltViewModel()
) {

    val response by viewModel.response.collectAsState()
    val actors by viewModel.actors.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    AnimeActors(
        response = response,
        actors = actors,
        isLoading = isLoading,
        onLoadMoreData = viewModel::loadData,
        onNavigateBack = onNavigateBack,
        onNavigateToActorDetail = onNavigateToActorDetail,
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AnimeActors(
    response: AnimeActorsResponse? = null,
    actors: List<DetailActor> = emptyList(),
    isLoading: Boolean = false,
    onLoadMoreData: () -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateToActorDetail: (Int) -> Unit,
) {
    val listState = rememberLazyGridState()
    LaunchedEffect(listState, actors.size) {
        snapshotFlow { listState.layoutInfo }
            .map { layoutInfo ->
                val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
                val totalItemsCount = layoutInfo.totalItemsCount

                lastVisibleItemIndex >= totalItemsCount - 2
            }
            .distinctUntilChanged()
            .collect { shouldLoadMore ->
                if (shouldLoadMore && !isLoading && actors.isNotEmpty()) {
                    val lastId = response?.cursor?.lastId
                    if (lastId != null) {
                        onLoadMoreData()
                    }
                }
            }
    }

    Scaffold(
        topBar = {
            APTitledBackTopAppBar(
                title = "캐릭터/성우진",
                handleBackNavigation = { onNavigateBack() },
            )
        },
    ) { innerPadding ->
        LazyVerticalGrid(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(top = 12.dp)
                .background(APColors.White),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 24.dp),
            columns = GridCells.Fixed(2),
            state = listState,
            verticalArrangement = Arrangement.spacedBy(14.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            items(actors) { actor ->
                Row(
                    modifier = Modifier
                        .background(APColors.Surface, RoundedCornerShape(8.dp))
                        .clickable {
                            onNavigateToActorDetail(actor.voiceActor.id)
                        },
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        AsyncImage(
                            model = actor.character.imageUrl,
                            contentDescription = null,
                            modifier = Modifier
                                .width(65.dp)
                                .height(95.dp)
                                .clip(RoundedCornerShape(topStart = 8.dp))
                                .background(APColors.Gray, RoundedCornerShape(topStart = 8.dp)),
                        )
                        Box(
                            modifier = Modifier
                                .height(46.dp)
                                .padding(start = 8.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Text(
                                text = "${actor.character.name}",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.W500,
                                color = APColors.Black
                            )
                        }
                    }
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        AsyncImage(
                            model = actor.voiceActor.imageUrl,
                            contentDescription = null,
                            modifier = Modifier
                                .width(65.dp)
                                .height(95.dp)
                                .clip(RoundedCornerShape(topEnd = 8.dp))
                                .background(APColors.Gray, RoundedCornerShape(topEnd = 8.dp)),
                        )
                        Box(
                            modifier = Modifier
                                .height(46.dp)
                                .padding(start = 8.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Text(
                                text = "${actor.voiceActor.name}",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.W500,
                                color = APColors.Black
                            )
                        }
                    }
                }
            }
        }
    }

}