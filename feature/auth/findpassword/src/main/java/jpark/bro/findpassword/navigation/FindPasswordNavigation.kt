package jpark.bro.findpassword.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import jpark.bro.findpassword.PasswordReset
import jpark.bro.findpassword.PasswordVerification
import kotlinx.serialization.Serializable

@Serializable data object PasswordVerification

@Serializable data object PasswordReset

fun NavController.navigateToPasswordVerification(navOptions: NavOptions? = null) = navigate(route = PasswordVerification, navOptions)

fun NavGraphBuilder.passwordVerificationScreen(
    onNavigateBack: () -> Unit,
    onNavigateToPasswordReset: () -> Unit,
) {
    composable<PasswordVerification> {
        PasswordVerification(
            onNavigateBack = onNavigateBack,
            onNavigateToPasswordReset = onNavigateToPasswordReset,
        )
    }
}

fun NavController.navigateToPasswordReset(navOptions: NavOptions? = null) = navigate(route = PasswordReset, navOptions)

fun NavGraphBuilder.passwordResetScreen(
    onNavigateBack: () -> Unit,
) {
    composable<PasswordReset> {
        PasswordReset(
            onNavigateBack = onNavigateBack,
        )
    }
}