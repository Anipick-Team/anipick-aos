package jpark.bro.ui.features.main.search

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import jpark.bro.ui.component.APSearchFieldBackTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Search(
    handleBackNavigation: () -> Unit
) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    var value by remember { mutableStateOf("") }
    Scaffold(
        topBar = {
            APSearchFieldBackTopAppBar(
                value = value,
                onValueChange = { value = it },
                keyboardActions =  KeyboardActions(
                    onDone = {
                        /* TODO
                         * 1. api 검색
                         * 2. 키보드 숨기기
                         * 3. 최근 검색 에 바로 추가
                         * 4. 필드 값은 유지
                         */
                        Toast.makeText(context, value, Toast.LENGTH_SHORT).show()
                        keyboardController?.hide()
                    }
                ),
            ) { handleBackNavigation() }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Search"
            )
        }
    }
}