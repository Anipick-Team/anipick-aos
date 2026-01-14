package com.jparkbro.info.anime.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.jparkbro.info.anime.InfoAnimeRoot
import com.jparkbro.model.common.FormType
import kotlinx.serialization.Serializable

@Serializable data class InfoAnime(val animeId: Long)

fun NavHostController.navigateToInfoAnime(
    animeId: Long, navOptions: NavOptions? = null
) = navigate(InfoAnime(animeId), navOptions)

fun NavGraphBuilder.infoAnimeScreen(
    onNavigateBack: () -> Unit,
    onNavigateToReviewForm: (Long, FormType) -> Unit,
    onNavigateToInfoAnime: (Long) -> Unit,
    onNavigateToInfoSeries: (Long, String) -> Unit,
    onNavigateToInfoRecommend: (Long) -> Unit,
    onNavigateToInfoCharacter: (Long) -> Unit,
    onNavigateToActor: (Long) -> Unit,
    onNavigateToStudio: (Long) -> Unit,
) {
    composable<InfoAnime> {
        InfoAnimeRoot(
            onNavigateBack = onNavigateBack,
            onNavigateToReviewForm = onNavigateToReviewForm,
            onNavigateToInfoAnime = onNavigateToInfoAnime,
            onNavigateToInfoSeries = onNavigateToInfoSeries,
            onNavigateToInfoRecommend = onNavigateToInfoRecommend,
            onNavigateToInfoCharacter = onNavigateToInfoCharacter,
            onNavigateToActor = onNavigateToActor,
            onNavigateToStudio = onNavigateToStudio,
        )
    }
}