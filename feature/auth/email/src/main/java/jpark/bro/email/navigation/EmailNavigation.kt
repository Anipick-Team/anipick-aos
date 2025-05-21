package jpark.bro.email.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import jpark.bro.email.EmailLogin
import jpark.bro.email.EmailSignup
import kotlinx.serialization.Serializable

@Serializable data object EmailLogin // route to EmailLogin screen

@Serializable data object EmailSignup // route to EmailSignup screen

fun NavController.navigateToEmailLogin(navOptions: NavOptions? = null) = navigate(route = EmailLogin, navOptions)

fun NavGraphBuilder.emailLoginScreen(
    onNavigateBack: () -> Unit,
    onNavigateToEmailSignup: () -> Unit,
    onNavigateToFindPassword: () -> Unit,
) {
    composable<EmailLogin> {
        EmailLogin(
            onNavigateBack = onNavigateBack,
            onNavigateToEmailSignup = onNavigateToEmailSignup,
            onNavigateToFindPassword = onNavigateToFindPassword,
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

