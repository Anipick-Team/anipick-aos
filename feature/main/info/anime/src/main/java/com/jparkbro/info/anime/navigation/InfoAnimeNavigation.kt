package com.jparkbro.info.anime.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.jparkbro.info.anime.InfoAnimeRoot
import com.jparkbro.model.common.FormType
import kotlinx.serialization.Serializable

@Serializable data class InfoAnime(val animeId: Int)

fun NavHostController.navigateToInfoAnime(
    animeId: Int, navOptions: NavOptions? = null
) = navigate(InfoAnime(animeId), navOptions)

fun NavGraphBuilder.infoAnimeScreen(
    onNavigateBack: () -> Unit,
    onNavigateToReviewForm: (Int, Int?, FormType) -> Unit,
    onNavigateToStudioDetail: (Int) -> Unit,
    onNavigateToAnimeActors: (Int) -> Unit,
    onNavigateToActorDetail: (Int) -> Unit,
    onNavigateToInfoAnime: (Int) -> Unit,
    onNavigateToAnimeSeries: (Int, String) -> Unit,
    onNavigateToAnimeRecommends: (Int) -> Unit,
) {
    composable<InfoAnime> {
        InfoAnimeRoot(
            onNavigateBack = onNavigateBack,
            onNavigateToReviewForm = onNavigateToReviewForm,
            onNavigateToStudioDetail = onNavigateToStudioDetail,
            onNavigateToAnimeActors = onNavigateToAnimeActors,
            onNavigateToActorDetail = onNavigateToActorDetail,
            onNavigateToInfoAnime = onNavigateToInfoAnime,
            onNavigateToAnimeSeries = onNavigateToAnimeSeries,
            onNavigateToAnimeRecommends = onNavigateToAnimeRecommends,
        )
    }
}