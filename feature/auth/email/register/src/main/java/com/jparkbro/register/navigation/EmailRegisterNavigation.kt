package com.jparkbro.register.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.jparkbro.register.EmailRegisterRoot
import kotlinx.serialization.Serializable

/** Route to EmailRegister screen */
@Serializable data object EmailRegister

fun NavController.navigateToEmailRegister(navOptions: NavOptions? = null) = navigate(route = EmailRegister, navOptions)

fun NavGraphBuilder.emailRegisterScreen(
    onNavigateBack: () -> Unit,
    onNavigateToPreferenceSetup: () -> Unit,
) {
    composable<EmailRegister> {
        EmailRegisterRoot(
            onNavigateBack = onNavigateBack,
            onNavigateToPreferenceSetup = onNavigateToPreferenceSetup,
        )
    }
}