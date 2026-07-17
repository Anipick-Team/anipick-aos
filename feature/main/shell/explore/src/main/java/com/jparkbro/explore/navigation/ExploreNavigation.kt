package com.jparkbro.explore.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.jparkbro.explore.ExploreRoot
import com.jparkbro.model.common.MetaData
import kotlinx.serialization.Serializable

@Serializable data class Explore(val year: String?, val quarter: String?)

fun NavHostController.navigateToExplore(
    year: String? = null,
    quarter: String? = null,
    navOptions: NavOptions? = null
) = navigate(Explore(year, quarter), navOptions)

fun NavGraphBuilder.exploreScreen(
    metaData: MetaData,
    bottomNav: @Composable () -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToInfoAnime: (Long) -> Unit,
) {
    composable<Explore> {
        ExploreRoot(
            metaData = metaData,
            bottomNav = bottomNav,
            onNavigateToSearch = onNavigateToSearch,
            onNavigateToInfoAnime = onNavigateToInfoAnime,
        )
    }
}