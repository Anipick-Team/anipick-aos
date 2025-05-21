package jpark.bro.anipick.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import jpark.bro.email.navigation.emailLoginScreen
import jpark.bro.email.navigation.emailSignupScreen
import jpark.bro.email.navigation.navigateToEmailLogin
import jpark.bro.email.navigation.navigateToEmailSignup
import jpark.bro.findpassword.navigation.PasswordVerification
import jpark.bro.findpassword.navigation.navigateToPasswordReset
import jpark.bro.findpassword.navigation.navigateToPasswordVerification
import jpark.bro.findpassword.navigation.passwordResetScreen
import jpark.bro.findpassword.navigation.passwordVerificationScreen
import jpark.bro.login.navigation.Login
import jpark.bro.login.navigation.loginScreen
import jpark.bro.preferencesetup.navigation.PreferenceSetup
import jpark.bro.preferencesetup.navigation.navigateToPreferenceSetup
import jpark.bro.preferencesetup.navigation.preferenceSetupScreen

@Composable
fun APNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
//        startDestination = Login,
        startDestination = PasswordVerification,
        modifier = modifier
    ) {
        loginScreen(
            onNavigateToHome = {  },
            onNavigateToPreferenceSetup = navController::navigateToPreferenceSetup,
            onNavigateToEmailLogin = navController::navigateToEmailLogin,
            onNavigateToEmailSignup = navController::navigateToEmailSignup,
        )
        emailLoginScreen(
            onNavigateBack = navController::navigateUp,
            onNavigateToEmailSignup = navController::navigateToEmailSignup,
            onNavigateToFindPassword = navController::navigateToPasswordVerification,
        )
        emailSignupScreen(
            onNavigateBack = navController::navigateUp,
            onNavigateToPreferenceSetup = navController::navigateToPreferenceSetup,
        )
        passwordVerificationScreen(
            onNavigateBack = navController::navigateUp,
            onNavigateToPasswordReset = navController::navigateToPasswordReset
        )
        passwordResetScreen(
            onNavigateBack = navController::navigateUp,
        )
        preferenceSetupScreen(
            onNavigateToHome = {},
        )
    }
}