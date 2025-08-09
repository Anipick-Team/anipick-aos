package com.jparkbro.search.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.jparkbro.search.Search
import com.jparkbro.search.SearchResult
import com.jparkbro.search.SearchResultViewModel
import kotlinx.serialization.Serializable

@Serializable data object Search

@Serializable data class SearchResult(val searchParam: String)

fun NavController.navigateToSearch(navOptions: NavOptions? = null) = navigate(Search, navOptions)

fun NavGraphBuilder.searchScreen(
    onNavigateBack: () -> Unit,
    onNavigateToAnimeDetail: (Int) -> Unit,
    onNavigateSearchResult: (String) -> Unit,
) {
    composable<Search> {
        Search(
            onNavigateBack = onNavigateBack,
            onNavigateToAnimeDetail = onNavigateToAnimeDetail,
            onNavigateSearchResult = onNavigateSearchResult,
        )
    }
}

fun NavController.navigateToSearchResult(searchParam: String, navOptions: NavOptions? = null) = navigate(SearchResult(searchParam), navOptions)

fun NavGraphBuilder.searchResultScreen(
    onNavigateBack: () -> Unit,
    onNavigateToAnimeDetail: (Int) -> Unit,
) {
    composable<SearchResult> { entry ->
        val searchParam = entry.toRoute<SearchResult>().searchParam

        SearchResult(
            onNavigateBack = onNavigateBack,
            onNavigateToAnimeDetail = onNavigateToAnimeDetail,
            viewModel = hiltViewModel<SearchResultViewModel, SearchResultViewModel.Factory>(
                key = searchParam
            ) { factory ->
                factory.create(searchParam)
            }
        )
    }
}