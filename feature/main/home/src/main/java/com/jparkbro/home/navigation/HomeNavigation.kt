package com.jparkbro.home.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import androidx.navigation.toRoute
import com.jparkbro.home.Home
import com.jparkbro.home.HomeDetail
import com.jparkbro.home.HomeDetailViewModel
import com.jparkbro.model.common.FormType
import com.jparkbro.model.home.ContentType
import com.jparkbro.ui.util.extension.clearAllBackStack
import kotlinx.serialization.Serializable

@Serializable data object Home

@Serializable data class HomeDetail(val type: ContentType)

fun NavHostController.navigateToHome(
    navOptions: NavOptions = navOptions { clearAllBackStack() }
) = navigate(Home, navOptions)

fun NavGraphBuilder.homeScreen(
    bottomNav: @Composable () -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToAnimeDetail: (Int) -> Unit,
    onNavigateToRanking: () -> Unit,
    onNavigateToExplore: (year: String?, quarter: String?) -> Unit,
    onNavigateToHomeDetail: (ContentType) -> Unit,
) {
    composable<Home> {
        Home(
            bottomNav = bottomNav,
            onNavigateToSearch = onNavigateToSearch,
            onNavigateToAnimeDetail = onNavigateToAnimeDetail,
            onNavigateToRanking = onNavigateToRanking,
            onNavigateToExplore = onNavigateToExplore,
            onNavigateToHomeDetail = onNavigateToHomeDetail,
        )
    }
}

fun NavController.navigateToHomeDetail(type: ContentType, navOptions: NavOptions? = null) = navigate(HomeDetail(type), navOptions)

fun NavGraphBuilder.homeDetailScreen(
    onNavigateBack: () -> Unit,
    onNavigateToAnimeDetail: (Int) -> Unit,
    onNavigateToReviewForm: (Int, Int, FormType) -> Unit,
) {
    composable<HomeDetail> { entry ->
        val route = entry.toRoute<HomeDetail>()

        HomeDetail(
            onNavigateBack = onNavigateBack,
            onNavigateToAnimeDetail = onNavigateToAnimeDetail,
            onNavigateToReviewForm = onNavigateToReviewForm,
            viewModel = hiltViewModel<HomeDetailViewModel, HomeDetailViewModel.Factory>(
                key = route.type.name
            ) { factory ->
                factory.create(route.type)
            }
        )
    }
}