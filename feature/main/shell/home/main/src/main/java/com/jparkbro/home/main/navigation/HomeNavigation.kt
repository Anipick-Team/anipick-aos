package com.jparkbro.home.main.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import com.jparkbro.home.main.HomeRoot
import com.jparkbro.model.enum.HomeDetailType
import com.jparkbro.model.home.ContentType
import com.jparkbro.ui.util.extension.clearAllBackStack
import kotlinx.serialization.Serializable

@Serializable data object Home

fun NavHostController.navigateToHome(
    navOptions: NavOptions = navOptions { clearAllBackStack() }
) = navigate(Home, navOptions)

fun NavGraphBuilder.homeScreen(
    bottomNav: @Composable () -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToAnimeDetail: (Int) -> Unit,
    onNavigateToRanking: () -> Unit,
    onNavigateToExplore: (year: String?, quarter: String?) -> Unit,
    onNavigateToHomeDetail: (HomeDetailType) -> Unit,
) {
    composable<Home> {
        HomeRoot(
            bottomNav = bottomNav,
            onNavigateToSearch = onNavigateToSearch,
            onNavigateToAnimeDetail = onNavigateToAnimeDetail,
            onNavigateToRanking = onNavigateToRanking,
            onNavigateToExplore = onNavigateToExplore,
            onNavigateToHomeDetail = onNavigateToHomeDetail,
        )
    }
}