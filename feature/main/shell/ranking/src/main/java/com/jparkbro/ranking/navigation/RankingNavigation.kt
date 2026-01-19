package com.jparkbro.ranking.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import com.jparkbro.model.common.MetaData
import com.jparkbro.ranking.RankingRoot
import com.jparkbro.ui.util.extension.clearAllBackStack
import kotlinx.serialization.Serializable

@Serializable data object Ranking

fun NavHostController.navigateToRanking(
    navOptions: NavOptions = navOptions { clearAllBackStack() }
) = navigate(Ranking, navOptions)

fun NavGraphBuilder.rankingScreen(
    metaData: MetaData,
    bottomNav: @Composable () -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToInfoAnime: (Long) -> Unit,
) {
    composable<Ranking> {
        RankingRoot(
            metaData = metaData,
            bottomNav = bottomNav,
            onNavigateToSearch = onNavigateToSearch,
            onNavigateToInfoAnime = onNavigateToInfoAnime,
        )
    }
}