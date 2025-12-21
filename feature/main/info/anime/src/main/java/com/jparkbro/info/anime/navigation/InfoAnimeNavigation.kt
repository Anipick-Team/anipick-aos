package com.jparkbro.info.anime.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.jparkbro.info.anime.InfoAnimeRoot
import kotlinx.serialization.Serializable

@Serializable data class InfoAnime(val animeId: Int)

fun NavHostController.navigateToInfoAnime(
    animeId: Int, navOptions: NavOptions? = null
) = navigate(InfoAnime(animeId), navOptions)

fun NavGraphBuilder.infoAnimeScreen(

) {
    composable<InfoAnime> {
        InfoAnimeRoot(

        )
    }
}