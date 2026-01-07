package com.jparkbro.info.character.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.jparkbro.info.character.InfoCharacterRoot
import kotlinx.serialization.Serializable

@Serializable data class InfoCharacter(val animeId: Int)

fun NavHostController.navigateToInfoCharacter(
    animeId: Int, navOptions: NavOptions? = null
) = navigate(InfoCharacter(animeId), navOptions)

fun NavGraphBuilder.infoCharacterScreen(
    onNavigateBack: () -> Unit,
) {
    composable<InfoCharacter> {
        InfoCharacterRoot(
            onNavigateBack = onNavigateBack
        )
    }
}