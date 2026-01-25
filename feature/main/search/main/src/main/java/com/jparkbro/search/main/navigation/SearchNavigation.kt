package com.jparkbro.search.main.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.jparkbro.search.main.SearchRoot
import kotlinx.serialization.Serializable

@Serializable data object Search

fun NavHostController.navigateToSearch(
    navOptions: NavOptions? = null
) = navigate(Search, navOptions)

fun NavGraphBuilder.searchScreen(
    onNavigateBack: () -> Unit,
    onNavigateToInfoAnime: (Long) -> Unit,
    onNavigateToSearchResult: (String) -> Unit,
) {
    composable<Search> {
        SearchRoot(
            onNavigateBack = onNavigateBack,
            onNavigateToInfoAnime = onNavigateToInfoAnime,
            onNavigateToSearchResult = onNavigateToSearchResult,
        )
    }
}