package com.jparkbro.reset.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.jparkbro.reset.PasswordResetRoot
import kotlinx.serialization.Serializable

/** Route to PasswordReset screen */
@Serializable data class PasswordReset(val email: String)

fun NavHostController.navigateToPasswordReset(
    email: String, navOptions: NavOptions? = null
) = navigate(PasswordReset(email), navOptions)

fun NavGraphBuilder.passwordResetScreen(
    onNavigateBack: () -> Unit,
    onNavigateToLogin: () -> Unit,
) {
    composable<PasswordReset> {
        PasswordResetRoot(
            onNavigateBack = onNavigateBack,
            onNavigateToLogin = onNavigateToLogin,
        )
    }
}