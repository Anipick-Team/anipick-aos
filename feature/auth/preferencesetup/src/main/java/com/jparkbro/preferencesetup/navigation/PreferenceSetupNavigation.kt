package com.jparkbro.preferencesetup.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import com.jparkbro.model.common.MetaData
import com.jparkbro.preferencesetup.PreferenceSetupRoot
import com.jparkbro.ui.util.extension.clearAllBackStack
import kotlinx.serialization.Serializable

@Serializable data object PreferenceSetup

fun NavHostController.navigateToPreferenceSetup(
    navOptions: NavOptions = navOptions { clearAllBackStack() }
) = navigate(route = PreferenceSetup, navOptions)

fun NavGraphBuilder.preferenceSetupScreen(
    metaData: MetaData,
    onNavigateToHome: () -> Unit,
) {
    composable<PreferenceSetup> {
        PreferenceSetupRoot(
            metaData = metaData,
            onNavigateToHome = onNavigateToHome,
        )
    }
}