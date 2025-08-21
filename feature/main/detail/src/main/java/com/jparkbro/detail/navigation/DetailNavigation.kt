package com.jparkbro.detail.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.jparkbro.detail.DetailAnime
import com.jparkbro.detail.DetailAnimeViewModel
import com.jparkbro.detail.StudioDetail
import com.jparkbro.detail.StudioDetailViewModel
import com.jparkbro.model.common.FormType
import kotlinx.serialization.Serializable

@Serializable data class AnimeDetail(val animeId: Int)

@Serializable data class StudioDetail(val studioName: String, val studioId: Int)

fun NavController.navigateToAnimeDetail(animeId: Int, navOptions: NavOptions? = null) = navigate(AnimeDetail(animeId), navOptions)

fun NavGraphBuilder.detailAnimeScreen(
    onNavigateBack: () -> Unit,
    onNavigateToReviewForm: (Int, Int?, FormType) -> Unit,
    onNavigateToStudioDetail: (String, Int) -> Unit,
    onCheckReviewRefresh: () -> Boolean,
    onClearReviewRefresh: () -> Unit,
    onStatusRefresh: () -> Unit,
) {
    composable<AnimeDetail> { entry ->
        val animeId = entry.toRoute<AnimeDetail>().animeId

        DetailAnime(
            onNavigateBack = onNavigateBack,
            onNavigateToReviewForm = onNavigateToReviewForm,
            onNavigateToStudioDetail = onNavigateToStudioDetail,
            onCheckReviewRefresh = onCheckReviewRefresh,
            onClearReviewRefresh = onClearReviewRefresh,
            onStatusRefresh = onStatusRefresh,
            viewModel = hiltViewModel<DetailAnimeViewModel, DetailAnimeViewModel.Factory>(
                key = "$animeId"
            ) { factory ->
                factory.create(animeId)
            }
        )
    }
}

fun NavController.navigateToStudioDetail(studioName: String, studioId: Int, navOptions: NavOptions? = null) = navigate(StudioDetail(studioName, studioId), navOptions)

fun NavGraphBuilder.studioDetailScreen(
    onNavigateBack: () -> Unit,
) {
    composable<StudioDetail> { entry ->
        val route = entry.toRoute<StudioDetail>()

        StudioDetail(
            onNavigateBack = onNavigateBack,
            viewModel = hiltViewModel<StudioDetailViewModel, StudioDetailViewModel.Factory>(
                key = "${route.studioId}"
            ) { factory ->
                factory.create(StudioDetail(
                    studioName = route.studioName,
                    studioId = route.studioId
                ))
            }
        )
    }
}