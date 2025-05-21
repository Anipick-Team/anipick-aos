package jpark.bro.preferencesetup.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import jpark.bro.preferencesetup.PreferenceSetup
import kotlinx.serialization.Serializable

@Serializable data object PreferenceSetup

fun NavController.navigateToPreferenceSetup(navOptions: NavOptions? = null) = navigate(route = PreferenceSetup, navOptions)

fun NavGraphBuilder.preferenceSetupScreen(
    onNavigateToHome: () -> Unit,
) {
    composable<PreferenceSetup> {
        PreferenceSetup(
            onNavigateToHome = onNavigateToHome,
        )
    }
}