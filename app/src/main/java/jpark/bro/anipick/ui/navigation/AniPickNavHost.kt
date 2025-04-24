package jpark.bro.anipick.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import jpark.bro.anipick.ui.view.EmailLogin
import jpark.bro.anipick.ui.view.EmailSignup
import jpark.bro.anipick.ui.view.Login

@Composable
fun APNavHost(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            Login(
                onNavigateToEmailLogin = { navController.navigate("email-login") },
                onNavigateToEmailSignUp = { navController.navigate("email-signup") }
            )
        }
        composable("email-login") { EmailLogin() }
        composable("email-signup") { EmailSignup() }
    }
}