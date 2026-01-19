package com.jparkbro.actor.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.jparkbro.actor.ActorRoot
import kotlinx.serialization.Serializable

@Serializable data class Actor(val actorId: Long)

fun NavHostController.navigateToActor(
    actorId: Long, navOptions: NavOptions? = null
) = navigate(Actor(actorId), navOptions)

fun NavGraphBuilder.actorScreen(
    onNavigateBack: () -> Unit,
    onNavigateToInfoAnime: (Long) -> Unit,
) {
    composable<Actor> {
        ActorRoot(
            onNavigateBack = onNavigateBack,
            onNavigateToInfoAnime = onNavigateToInfoAnime,
        )
    }
}