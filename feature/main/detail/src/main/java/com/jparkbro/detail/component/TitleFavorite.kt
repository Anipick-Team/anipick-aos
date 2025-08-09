package com.jparkbro.detail.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jparkbro.ui.R
import com.jparkbro.ui.theme.APColors

@Composable
fun TitleFavorite(
    modifier: Modifier = Modifier,
    title: String,
    isLiked: Boolean,
    onChangeLikeState: (Boolean) -> Unit,
    enabled: Boolean = true
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 20.sp,
            fontWeight = FontWeight.W600,
            color = APColors.Black
        )
        Icon(
            painter = painterResource(if (isLiked) R.drawable.ic_favorite_on else R.drawable.ic_favorite_off),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier
                .size(19.dp)
                .clickable(
                    enabled = enabled
                ) { onChangeLikeState(!isLiked) }
        )
    }
}