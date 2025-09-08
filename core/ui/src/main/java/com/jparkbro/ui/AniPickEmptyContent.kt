package com.jparkbro.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jparkbro.ui.theme.APColors
import kotlin.random.Random

@Composable
fun APEmptyContent(
    modifier: Modifier = Modifier,
    comment: String = "",
) {
    val randomImageResource = remember {
        val randomNumber = Random.nextInt(1, 4) // 1, 2, 3 중 랜덤
        when (randomNumber) {
            1 -> R.drawable.emptyimage1
            2 -> R.drawable.emptyimage2
            3 -> R.drawable.emptyimage3
            else -> R.drawable.emptyimage1
        }
    }
    
    Box(
        modifier = modifier
            .offset(y = (-80).dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(28.dp)
        ) {
            Image(
                painter = painterResource(randomImageResource),
                contentDescription = null
            )
            Text(
                text = comment,
                fontSize = 16.sp,
                fontWeight = FontWeight.W500,
                color = APColors.TextGray
            )
        }
    }
}