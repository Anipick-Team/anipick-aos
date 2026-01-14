package com.jparkbro.home.detail.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.jparkbro.home.detail.HomeDetailRoot
import com.jparkbro.model.common.FormType
import com.jparkbro.model.enum.HomeDetailType
import kotlinx.serialization.Serializable

@Serializable data class HomeDetail(val type: HomeDetailType)

fun NavHostController.navigateToHomeDetail(
    type: HomeDetailType, navOptions: NavOptions? = null
) = navigate(HomeDetail(type), navOptions)

fun NavGraphBuilder.homeDetailScreen(
    onNavigateBack: () -> Unit,
    onNavigateToInfoAnime: (Long) -> Unit,
    onNavigateToReviewForm: (Long, FormType) -> Unit,
) {
    composable<HomeDetail> {
        HomeDetailRoot(
            onNavigateBack = onNavigateBack,
            onNavigateToInfoAnime = onNavigateToInfoAnime,
            onNavigateToReviewForm = onNavigateToReviewForm,
        )
    }
}