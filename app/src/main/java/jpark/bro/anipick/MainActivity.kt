package jpark.bro.anipick

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import jpark.bro.anipick.navigation.APNavHost
import jpark.bro.ui.theme.AniPickTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        // TODO splash screen 실행

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // TODO splash screen 종료
        // splashScreen.setKeepOnScreenCondition { viewModel... > false 반환 }

        setContent {
            AniPickTheme {
                val navController = rememberNavController()
                APNavHost(
                    navController = navController
                )
            }
        }
    }
}