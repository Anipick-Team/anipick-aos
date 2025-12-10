package com.jparkbro.anipick.firebase

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController

@Composable
fun TrackScreenNavigation(navController: NavController) {
    val context = LocalContext.current

    DisposableEffect(navController) {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            // 전체 route: com.jparkbro.login.navigation.Login
            // 원하는 결과: login
            val fullRoute = destination.route ?: "unknown"

            val screenName = fullRoute
                .substringAfterLast(".")  // Login

            val screenClass = fullRoute
                .substringBefore("?")     // 쿼리 파라미터 제거
                .substringBefore("/")     // 경로 파라미터 제거

            FirebaseManager.getInstance(context).logScreenView(
                screenName = screenName,
                screenClass = screenClass
            )
        }

        navController.addOnDestinationChangedListener(listener)

        onDispose {
            navController.removeOnDestinationChangedListener(listener)
        }
    }
}
