package com.jparkbro.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

@Composable
fun BulletPointText(
    text: String,
    textStyle: TextStyle
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "•",
            style = textStyle,
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .alignByBaseline()
        )
        Text(
            text = text,
            style = textStyle,
            modifier = Modifier
                .alignByBaseline()
        )
    }
}