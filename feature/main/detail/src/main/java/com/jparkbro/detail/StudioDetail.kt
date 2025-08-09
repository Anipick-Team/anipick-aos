package com.jparkbro.detail

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.jparkbro.ui.APTitledBackTopAppBar

@Composable
internal fun StudioDetail(
    onNavigateBack: () -> Unit,
    viewModel: StudioDetailViewModel = hiltViewModel()
) {
    val studioName = viewModel.initData.studioName

    StudioDetail(
        studioName = studioName,
        onNavigateBack = onNavigateBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StudioDetail(
    studioName: String,
    onNavigateBack: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            APTitledBackTopAppBar(
                title = studioName,
                handleBackNavigation = { onNavigateBack() },
            )
        },
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

        }
    }
}