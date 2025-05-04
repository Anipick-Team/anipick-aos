package jpark.bro.ui.features.main.container

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import jpark.bro.ui.component.APLogoSearchTopAppBar
import jpark.bro.ui.features.main.container.MainScreenViewModel
import jpark.bro.ui.navigation.APBottomBar
import jpark.bro.ui.features.main.explore.Explore
import jpark.bro.ui.features.main.home.Home
import jpark.bro.ui.features.main.profile.Profile
import jpark.bro.ui.features.main.ranking.Ranking

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MainScreenViewModel = hiltViewModel(),
    onNavigateToSearch: () -> Unit,
) {
    val selectedTab = viewModel.selectedTab.collectAsState()

    Scaffold(
        topBar = {
            APLogoSearchTopAppBar { onNavigateToSearch() }
        },
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
    MainScreen() {}
}