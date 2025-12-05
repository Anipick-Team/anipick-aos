package com.jparkbro.login.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.jparkbro.login.LoginRoot
import kotlinx.serialization.Serializable

/** Route to LoginScreen */
@Serializable data object Login

fun NavController.navigateToLogin(navOptions: NavOptions? = null) = navigate(route = Login, navOptions)

fun NavGraphBuilder.loginScreen(
    onNavigateToHome: () -> Unit,
    onNavigateToPreferenceSetup: () -> Unit,
    onNavigateToEmailLogin: () -> Unit,
    onNavigateToEmailSignup: () -> Unit,
) {
    composable<Login> {
        LoginRoot(
            onNavigateToHome = onNavigateToHome,
            onNavigateToPreferenceSetup = onNavigateToPreferenceSetup,
            onNavigateToEmailLogin = onNavigateToEmailLogin,
            onNavigateToEmailSignup = onNavigateToEmailSignup,
        )
    }
}
