package com.jparkbro.login.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.jparkbro.login.EmailLoginRoot
import kotlinx.serialization.Serializable

/** Route to EmailLogin screen */
@Serializable data object EmailLogin

fun NavController.navigateToEmailLogin(navOptions: NavOptions? = null) = navigate(route = EmailLogin, navOptions)

fun NavGraphBuilder.emailLoginScreen(
    onNavigateBack: () -> Unit,
    onNavigateToEmailRegister: () -> Unit,
    onNavigateToFindPassword: () -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToPreferenceSetup: () -> Unit,
) {
    composable<EmailLogin> {
        EmailLoginRoot(
            onNavigateBack = onNavigateBack,
            onNavigateToEmailRegister = onNavigateToEmailRegister,
            onNavigateToFindPassword = onNavigateToFindPassword,
            onNavigateToHome = onNavigateToHome,
            onNavigateToPreferenceSetup = onNavigateToPreferenceSetup,
        )
    }
}