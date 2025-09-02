package com.jparkbro.detail.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.jparkbro.detail.ActorDetail
import com.jparkbro.detail.ActorDetailViewModel
import com.jparkbro.detail.AnimeActors
import com.jparkbro.detail.AnimeActorsViewModel
import com.jparkbro.detail.DetailAnime
import com.jparkbro.detail.DetailAnimeViewModel
import com.jparkbro.detail.StudioDetail
import com.jparkbro.detail.StudioDetailViewModel
import com.jparkbro.model.common.FormType
import kotlinx.serialization.Serializable

@Serializable data class AnimeDetail(val animeId: Int)

@Serializable data class StudioDetail(val studioId: Int)

@Serializable data class AnimeActors(val animeId: Int)

@Serializable data class ActorDetail(val actorId: Int)

fun NavController.navigateToAnimeDetail(animeId: Int, navOptions: NavOptions? = null) = navigate(AnimeDetail(animeId), navOptions)

fun NavGraphBuilder.detailAnimeScreen(
    onNavigateBack: () -> Unit,
    onNavigateToReviewForm: (Int, Int?, FormType) -> Unit,
    onNavigateToStudioDetail: (Int) -> Unit,
    onNavigateToAnimeActors: (Int) -> Unit,
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
            onNavigateToAnimeActors = onNavigateToAnimeActors,
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

fun NavController.navigateToStudioDetail(studioId: Int, navOptions: NavOptions? = null) = navigate(StudioDetail(studioId), navOptions)

fun NavGraphBuilder.studioDetailScreen(
    onNavigateBack: () -> Unit,
    onNavigateToAnimeDetail: (Int) -> Unit,
) {
    composable<StudioDetail> { entry ->
        val route = entry.toRoute<StudioDetail>()

        StudioDetail(
            onNavigateBack = onNavigateBack,
            onNavigateToAnimeDetail = onNavigateToAnimeDetail,
            viewModel = hiltViewModel<StudioDetailViewModel, StudioDetailViewModel.Factory>(
                key = "${route.studioId}"
            ) { factory ->
                factory.create(
                    route.studioId
                )
            }
        )
    }
}

fun NavController.navigateToAnimeActors(animeId: Int, navOptions: NavOptions? = null) = navigate(AnimeActors(animeId), navOptions)

fun NavGraphBuilder.animeActorsScreen(
    onNavigateBack: () -> Unit,
    onNavigateToActorDetail: (Int) -> Unit,
) {
    composable<AnimeActors> { entry ->
        val route = entry.toRoute<AnimeActors>()

        AnimeActors(
            onNavigateBack = onNavigateBack,
            onNavigateToActorDetail = onNavigateToActorDetail,
            viewModel = hiltViewModel<AnimeActorsViewModel, AnimeActorsViewModel.Factory>(
                key = "${route.animeId}"
            ) { factory ->
                factory.create(route.animeId)
            }
        )
    }
}

fun NavController.navigateToActorDetail(actorId: Int, navOptions: NavOptions? = null) = navigate(ActorDetail(actorId), navOptions)

fun NavGraphBuilder.actorDetailScreen(
    onNavigateBack: () -> Unit,
    onNavigateToAnimeDetail: (Int) -> Unit,
) {
    composable<ActorDetail> { entry ->
        val route = entry.toRoute<ActorDetail>()

        ActorDetail(
            onNavigateBack = onNavigateBack,
            onNavigateToAnimeDetail = onNavigateToAnimeDetail,
            viewModel = hiltViewModel<ActorDetailViewModel, ActorDetailViewModel.Factory>(
                key = "${route.actorId}"
            ) { factory ->
                factory.create(route.actorId)
            }
        )
    }
}