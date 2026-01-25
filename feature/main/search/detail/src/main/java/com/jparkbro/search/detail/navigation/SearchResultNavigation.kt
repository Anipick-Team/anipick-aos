package com.jparkbro.search.detail.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.jparkbro.search.detail.SearchResultRoot
import kotlinx.serialization.Serializable

@Serializable data class SearchResult(val keyword: String)

fun NavHostController.navigateToSearchResult(
    keyword: String,
    navOptions: NavOptions? = null
) = navigate(SearchResult(keyword), navOptions)

fun NavGraphBuilder.searchResultScreen(
    onNavigateBack: () -> Unit,
    onNavigateToInfoAnime: (Long) -> Unit,
    onNavigateToActor: (Long) -> Unit,
    onNavigateToStudio: (Long) -> Unit,
) {
    composable<SearchResult> {
        SearchResultRoot(
            onNavigateBack = onNavigateBack,
            onNavigateToInfoAnime = onNavigateToInfoAnime,
            onNavigateToActor = onNavigateToActor,
            onNavigateToStudio = onNavigateToStudio,
        )
    }
}