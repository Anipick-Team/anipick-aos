package com.jparkbro.anipick

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.jparkbro.anipick.navigation.APBottomNavigation
import com.jparkbro.anipick.navigation.APNavHost
import com.jparkbro.anipick.navigation.BottomDestination
import com.jparkbro.home.navigation.Home
import com.jparkbro.login.navigation.Login
import com.jparkbro.ui.theme.AniPickTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        splashScreen.setKeepOnScreenCondition { viewModel.uiState.value.shouldKeepSplashScreen() }

        setContent {
            AniPickTheme {
                val metaData by viewModel.metaData.collectAsState()
                val uiState by viewModel.uiState.collectAsState()

                when (uiState) {
                    is MainActivityUiState.Loading -> { }
                    is MainActivityUiState.Success -> {
                        val startDestination = if ((uiState as MainActivityUiState.Success).isAutoLogin) Home else Login

                        val bottomDestinations: List<BottomDestination> = BottomDestination.entries
                        val navController = rememberNavController()
                        val navBackStackEntry by navController.currentBackStackEntryAsState()
                        val currentDestination = navBackStackEntry?.destination


                        APNavHost(
                            navController = navController,
                            startDestination = startDestination,
                            metaData = metaData,
                            bottomNav = {
                                APBottomNavigation(
                                    items = bottomDestinations,
                                    navController = navController,
                                    currentDestination = currentDestination
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}