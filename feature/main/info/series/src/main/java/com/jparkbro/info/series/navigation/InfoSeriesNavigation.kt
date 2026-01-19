package com.jparkbro.info.series.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.jparkbro.info.series.InfoSeriesRoot
import kotlinx.serialization.Serializable

@Serializable data class InfoSeries(val animeId: Long, val title: String)

fun NavHostController.navigateToInfoSeries(
    animeId: Long, title: String, navOptions: NavOptions? = null
) = navigate(InfoSeries(animeId, title), navOptions)

fun NavGraphBuilder.infoSeriesScreen(
    onNavigateBack: () -> Unit,
    onNavigateToInfoAnime: (Long) -> Unit
) {
    composable<InfoSeries> {
        InfoSeriesRoot(
            onNavigateBack = onNavigateBack,
            onNavigateToInfoAnime = onNavigateToInfoAnime
        )
    }
}