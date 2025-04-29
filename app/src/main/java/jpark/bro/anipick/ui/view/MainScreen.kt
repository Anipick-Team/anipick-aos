package jpark.bro.anipick.ui.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import jpark.bro.anipick.ui.navigation.APBottomBar
import jpark.bro.anipick.ui.viewmodel.MainScreenViewModel

@Composable
fun MainScreen(
    viewModel: MainScreenViewModel = hiltViewModel()
) {
    val selectedTab = viewModel.selectedTab.collectAsState()

    Scaffold(
        topBar = {},
        bottomBar = {
            APBottomBar(
                selectedTab = selectedTab.value
            ) { viewModel.selectTab(it) }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedTab.value) {
                "home" -> Home()
                "ranking" -> Ranking()
                "explore" -> Explore()
                "profile" -> Profile()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MainScreen()
}