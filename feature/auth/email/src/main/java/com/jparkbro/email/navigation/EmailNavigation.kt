package com.jparkbro.email.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.jparkbro.email.EmailLogin
import com.jparkbro.email.EmailSignup
import kotlinx.serialization.Serializable

@Serializable data object EmailLogin // route to EmailLogin screen

@Serializable data object EmailSignup // route to EmailSignup screen

fun NavController.navigateToEmailLogin(navOptions: NavOptions? = null) = navigate(route = EmailLogin, navOptions)

fun NavGraphBuilder.emailLoginScreen(
    onNavigateBack: () -> Unit,
    onNavigateToEmailSignup: () -> Unit,
    onNavigateToFindPassword: () -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToPreferenceSetup: () -> Unit,
) {
    composable<EmailLogin> {
        EmailLogin(
            onNavigateBack = onNavigateBack,
            onNavigateToEmailSignup = onNavigateToEmailSignup,
            onNavigateToFindPassword = onNavigateToFindPassword,
            onNavigateToHome = onNavigateToHome,
            onNavigateToPreferenceSetup = onNavigateToPreferenceSetup,
        )
    }
}

fun NavController.navigateToEmailSignup(navOptions: NavOptions? = null) = navigate(route = EmailSignup, navOptions)

fun NavGraphBuilder.emailSignupScreen(
    onNavigateBack: () -> Unit,
    onNavigateToPreferenceSetup: () -> Unit,
) {
    composable<EmailSignup> {
        EmailSignup(
            onNavigateBack = onNavigateBack,
            onNavigateToPreferenceSetup = onNavigateToPreferenceSetup,
        )
    }
}

