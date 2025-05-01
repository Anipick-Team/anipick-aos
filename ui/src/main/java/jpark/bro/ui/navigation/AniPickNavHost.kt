package jpark.bro.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import jpark.bro.ui.view.EmailLogin
import jpark.bro.ui.view.EmailSignup
import jpark.bro.ui.view.FindPassword
import jpark.bro.ui.view.Login
import jpark.bro.ui.view.MainScreen

@Composable
fun APNavHost(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = AniPickDestinations.Auth.Login
    ) {
        composable(AniPickDestinations.Auth.Login) {
            Login(
                onNavigateToEmailLogin = { navController.navigate(AniPickDestinations.Auth.EmailLogin) },
                onNavigateToEmailSignUp = { navController.navigate(AniPickDestinations.Auth.EmailSignup) },
                onNavigateMainScreen = { navController.navigate(AniPickDestinations.MainScreen) }
            )
        }
        composable(AniPickDestinations.Auth.EmailLogin) {
            EmailLogin(
                onNavigateToEmailSignUp = { navController.navigate(AniPickDestinations.Auth.EmailSignup) },
                onNavigateToFindPassword = { navController.navigate(AniPickDestinations.Auth.FindPassword) },
                handleBackNavigation = { navController.navigateUp() }
            )
        }
        composable(AniPickDestinations.Auth.EmailSignup) {
            EmailSignup(
                handleBackNavigation = { navController.navigateUp() }
            )
        }
        composable(AniPickDestinations.Auth.FindPassword) {
            FindPassword(
                handleBackNavigation = { navController.navigateUp() }
            )
        }
        composable(AniPickDestinations.MainScreen) {
            MainScreen()
        }
    }
}