package jpark.bro.login.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import jpark.bro.login.Login
import kotlinx.serialization.Serializable

@Serializable data object Login // route to Login screen

fun NavController.navigateToLogin(navOptions: NavOptions? = null) = navigate(route = Login, navOptions)

fun NavGraphBuilder.loginScreen(
    onNavigateToHome: () -> Unit,
    onNavigateToPreferenceSetup: () -> Unit,
    onNavigateToEmailLogin: () -> Unit,
    onNavigateToEmailSignup: () -> Unit,
) {
    composable<Login> {
        Login(
            onNavigateToHome = onNavigateToHome,
            onNavigateToPreferenceSetup = onNavigateToPreferenceSetup,
            onNavigateToEmailLogin = onNavigateToEmailLogin,
            onNavigateToEmailSignup = onNavigateToEmailSignup,
        )
    }
}
