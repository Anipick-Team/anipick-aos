package jpark.bro.ui.features.main.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import jpark.bro.ui.component.DraggableStarRating

@Composable
fun Home(

) {
    var currentRating by remember { mutableStateOf(0f) }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        DraggableStarRating(
            initialRating = currentRating,
            onRatingChanged = { newRating ->
                currentRating = newRating
                // 여기서 별점 변경에 따른 추가 작업 수행
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    Home()
}