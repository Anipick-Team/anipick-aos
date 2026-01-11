package com.jparkbro.studio.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.jparkbro.studio.StudioRoot
import kotlinx.serialization.Serializable

@Serializable data class Studio(val studioId: Long)

fun NavHostController.navigateToStudio(
    studioId: Long, navOptions: NavOptions? = null
) = navigate(Studio(studioId), navOptions)

fun NavGraphBuilder.studioScreen(
    onNavigateBack: () -> Unit,
    onNavigateToInfoAnime: (Long) -> Unit,
) {
    composable<Studio> {
        StudioRoot(
            onNavigateBack = onNavigateBack,
            onNavigateToInfoAnime = onNavigateToInfoAnime
        )
    }
}