package com.jparkbro.explore.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.jparkbro.explore.Explore
import com.jparkbro.explore.ExploreViewModel
import com.jparkbro.model.common.MetaData
import com.jparkbro.model.explore.InitDate
import kotlinx.serialization.Serializable

@Serializable data class Explore(val year: String?, val quarter: String?)

fun NavController.navigateToExplore(year: String? = null, quarter: String? = null, navOptions: NavOptions? = null) = navigate(Explore(year, quarter), navOptions)

fun NavGraphBuilder.exploreScreen(
    metaData: MetaData,
    bottomNav: @Composable () -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToAnimeDetail: (Int) -> Unit,
) {
    composable<Explore> { entry ->
        val route = entry.toRoute<Explore>()
        Explore(
            metaData = metaData,
            bottomNav = bottomNav,
            onNavigateToSearch = onNavigateToSearch,
            onNavigateToAnimeDetail = onNavigateToAnimeDetail,
            viewModel = hiltViewModel<ExploreViewModel, ExploreViewModel.Factory>(
                key = "${route.year} ${route.quarter}"
            ) { factory ->
                factory.create(initDate = InitDate(route.year, route.quarter))
            }
        )
    }
}