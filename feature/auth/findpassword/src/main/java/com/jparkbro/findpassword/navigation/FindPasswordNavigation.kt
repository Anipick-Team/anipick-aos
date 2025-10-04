package com.jparkbro.findpassword.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.jparkbro.findpassword.PasswordReset
import com.jparkbro.findpassword.PasswordResetViewModel
import com.jparkbro.findpassword.PasswordVerification
import kotlinx.serialization.Serializable

@Serializable data object PasswordVerification

@Serializable data class PasswordReset(val email: String)

fun NavController.navigateToPasswordVerification(navOptions: NavOptions? = null) = navigate(route = PasswordVerification, navOptions)

fun NavGraphBuilder.passwordVerificationScreen(
    onNavigateBack: () -> Unit,
    onNavigateToPasswordReset: (String) -> Unit,
    onNavigateToLogin: () -> Unit,
) {
    composable<PasswordVerification> {
        PasswordVerification(
            onNavigateBack = onNavigateBack,
            onNavigateToPasswordReset = onNavigateToPasswordReset,
            onNavigateToLogin = onNavigateToLogin,
        )
    }
}

fun NavController.navigateToPasswordReset(email: String, navOptions: NavOptions? = null) = navigate(route = PasswordReset(email), navOptions)

fun NavGraphBuilder.passwordResetScreen(
    onNavigateBack: () -> Unit,
) {
    composable<PasswordReset> { entry ->
        val email = entry.toRoute<PasswordReset>().email

        PasswordReset(
            onNavigateBack = onNavigateBack,
            viewModel = hiltViewModel<PasswordResetViewModel, PasswordResetViewModel.Factory>(
                key = email
            ) { factory ->
                factory.create(email)
            }
        )
    }
}