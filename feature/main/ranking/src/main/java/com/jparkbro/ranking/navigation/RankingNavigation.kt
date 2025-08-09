package com.jparkbro.ranking.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.jparkbro.model.common.MetaData
import com.jparkbro.ranking.Ranking
import kotlinx.serialization.Serializable

@Serializable data object Ranking

fun NavController.navigateToRanking(navOptions: NavOptions? = null) = navigate(Ranking, navOptions)

fun NavGraphBuilder.rankingScreen(
    metaData: MetaData,
    bottomNav: @Composable () -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToAnimeDetail: (Int) -> Unit,
) {
    composable<Ranking> {
        Ranking(
            metaData = metaData,
            bottomNav = bottomNav,
            onNavigateToSearch = onNavigateToSearch,
            onNavigateToAnimeDetail = onNavigateToAnimeDetail,
        )
    }
}