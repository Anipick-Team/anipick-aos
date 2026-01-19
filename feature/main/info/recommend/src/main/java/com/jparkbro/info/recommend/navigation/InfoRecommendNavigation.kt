package com.jparkbro.info.recommend.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.jparkbro.info.recommend.InfoRecommendRoot
import kotlinx.serialization.Serializable

@Serializable data class InfoRecommend(val animeId: Long)

fun NavHostController.navigateToInfoRecommend(
    animeId: Long, navOptions: NavOptions? = null
) = navigate(InfoRecommend(animeId), navOptions)

fun NavGraphBuilder.infoRecommendScreen(
    onNavigateBack: () -> Unit,
    onNavigateToInfoAnime: (Long) -> Unit,
) {
    composable<InfoRecommend> {
        InfoRecommendRoot(
            onNavigateBack = onNavigateBack,
            onNavigateToInfoAnime = onNavigateToInfoAnime
        )
    }
}