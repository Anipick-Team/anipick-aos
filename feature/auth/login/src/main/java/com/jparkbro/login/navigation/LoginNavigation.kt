package com.jparkbro.login.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import com.jparkbro.login.LoginRoot
import com.jparkbro.ui.util.extension.clearAllBackStack
import kotlinx.serialization.Serializable

/** Route to LoginScreen */
@Serializable data object Login

fun NavHostController.navigateToLogin(
    navOptions: NavOptions = navOptions { clearAllBackStack() }
) = navigate(Login, navOptions)

fun NavGraphBuilder.loginScreen(
    onNavigateToHome: () -> Unit,
    onNavigateToPreferenceSetup: () -> Unit,
    onNavigateToEmailLogin: () -> Unit,
    onNavigateToEmailRegister: () -> Unit,
) {
    composable<Login> {
        LoginRoot(
            onNavigateToHome = onNavigateToHome,
            onNavigateToPreferenceSetup = onNavigateToPreferenceSetup,
            onNavigateToEmailLogin = onNavigateToEmailLogin,
            onNavigateToEmailRegister = onNavigateToEmailRegister,
        )
    }
}
