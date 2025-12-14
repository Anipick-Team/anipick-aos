package com.jparkbro.verification.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.jparkbro.verification.PasswordVerificationRoot
import kotlinx.serialization.Serializable

/** Route to PasswordVerification screen */
@Serializable data object PasswordVerification

fun NavHostController.navigateToPasswordVerification(
    navOptions: NavOptions? = null
) = navigate(route = PasswordVerification, navOptions)

fun NavGraphBuilder.passwordVerificationScreen(
    onNavigateBack: () -> Unit,
    onNavigateToPasswordReset: (String) -> Unit,
    onNavigateToLogin: () -> Unit,
) {
    composable<PasswordVerification> {
        PasswordVerificationRoot(
            onNavigateBack = onNavigateBack,
            onNavigateToPasswordReset = onNavigateToPasswordReset,
            onNavigateToLogin = onNavigateToLogin,
        )
    }
}