package com.jparkbro.info.character.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.jparkbro.info.character.InfoCharacterRoot
import kotlinx.serialization.Serializable

@Serializable data class InfoCharacter(val animeId: Long)

fun NavHostController.navigateToInfoCharacter(
    animeId: Long, navOptions: NavOptions? = null
) = navigate(InfoCharacter(animeId), navOptions)

fun NavGraphBuilder.infoCharacterScreen(
    onNavigateBack: () -> Unit,
    onNavigateToActor: (Long) -> Unit,
) {
    composable<InfoCharacter> {
        InfoCharacterRoot(
            onNavigateBack = onNavigateBack,
            onNavigateToActor = onNavigateToActor
        )
    }
}