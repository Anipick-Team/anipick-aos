package com.jparkbro.preferencesetup.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.jparkbro.model.common.MetaData
import com.jparkbro.preferencesetup.PreferenceSetup
import kotlinx.serialization.Serializable

@Serializable data object PreferenceSetup

fun NavController.navigateToPreferenceSetup(navOptions: NavOptions? = null) = navigate(route = PreferenceSetup, navOptions)

fun NavGraphBuilder.preferenceSetupScreen(
    metaData: MetaData,
    onNavigateToHome: () -> Unit,
) {
    composable<PreferenceSetup> {
        PreferenceSetup(
            metaData = metaData,
            onNavigateToHome = onNavigateToHome,
        )
    }
}