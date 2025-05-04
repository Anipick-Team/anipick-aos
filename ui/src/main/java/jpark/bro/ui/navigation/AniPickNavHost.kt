package jpark.bro.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import jpark.bro.ui.features.auth.email_login.EmailLogin
import jpark.bro.ui.features.auth.email_signup.EmailSignup
import jpark.bro.ui.features.auth.find_password.PasswordReset
import jpark.bro.ui.features.auth.find_password.PasswordVerification
import jpark.bro.ui.features.auth.login.Login
import jpark.bro.ui.features.main.container.MainScreen
import jpark.bro.ui.features.main.search.Search

@Composable
fun APNavHost(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = AniPickDestinations.Auth.Login.route
    ) {
        composable(AniPickDestinations.Auth.Login.route) {
            Login(
                onNavigateToEmailLogin = { navController.navigate(AniPickDestinations.Auth.EmailLogin.route) },
                onNavigateToEmailSignUp = { navController.navigate(AniPickDestinations.Auth.EmailSignup.route) },
                onNavigateMainScreen = { navController.navigate(AniPickDestinations.MainScreen.route) }
            )
        }
        composable(AniPickDestinations.Auth.EmailLogin.route) {
            EmailLogin(
                onNavigateToEmailSignUp = { navController.navigate(AniPickDestinations.Auth.EmailSignup.route) },
                onNavigateToFindPassword = { navController.navigate(AniPickDestinations.Auth.PasswordVerification.route) },
                handleBackNavigation = { navController.navigateUp() }
            )
        }
        composable(AniPickDestinations.Auth.EmailSignup.route) {
            EmailSignup(
                handleBackNavigation = { navController.navigateUp() }
            )
        }
        composable(AniPickDestinations.Auth.PasswordVerification.route) {
            PasswordVerification(
                onNavigateToPasswordReset = { navController.navigate(AniPickDestinations.Auth.PasswordReset.route) },
                handleBackNavigation = { navController.navigateUp() }
            )
        }
        composable(AniPickDestinations.Auth.PasswordReset.route) {
            PasswordReset(
                handleBackNavigation = { navController.navigateUp() }
            )
        }
        composable(AniPickDestinations.MainScreen.route) {
            MainScreen(
                onNavigateToSearch = { navController.navigate(AniPickDestinations.Search.route) }
            )
        }
        composable("search") {
            Search { navController.navigateUp() }
        }
    }
}