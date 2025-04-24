package jpark.bro.anipick

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import jpark.bro.anipick.ui.navigation.APNavHost
import jpark.bro.anipick.ui.theme.AniPickTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
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