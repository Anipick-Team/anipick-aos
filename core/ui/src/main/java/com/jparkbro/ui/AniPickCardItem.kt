package com.jparkbro.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil3.compose.AsyncImage
import com.jparkbro.ui.theme.APColors
import com.jparkbro.ui.util.extension.toImageModel

@Composable
fun APCardItem(
    title: String = "",
    imageUrl: String? = null,
    description: @Composable (() -> Unit)? = null,
    rank: String? = null,
    rating: String? = null,
    cardWidth: Dp = 128.dp,
    cardHeight: Dp = 182.dp,
    fontSize: TextUnit = 16.sp,
    maxLine: Int = 2,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .width(cardWidth)
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box {
            if (rank != null) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(APColors.Primary, RoundedCornerShape(topStart = 8.dp))
                        .zIndex(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = rank,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.W900,
                        color = Color.White
                    )
                }
            }
            AsyncImage(
                model = imageUrl?.toImageModel() ?: R.drawable.thumbnail_img,
                contentDescription = null,
                modifier = Modifier
                    .size(width = cardWidth, height = cardHeight)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
        }
        Text(
            text = title,
            fontSize = fontSize,
            fontWeight = FontWeight.W500,
            color = APColors.Black,
            maxLines = maxLine,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .width(cardWidth)
        )
        if (description != null) {
            Box(
                modifier = Modifier
                    .width(cardWidth),

            ) {
                description()
            }
        }
        if (rating != null) {
            Row(
                modifier = Modifier
                    .width(cardWidth),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "내 평가",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.W500,
                    color = APColors.TextGray,
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    painter = painterResource(R.drawable.ic_star_fill),
                    contentDescription = null,
                    tint = APColors.Point,
                    modifier = Modifier
                        .size(15.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = rating,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W600,
                    color = APColors.Point,
                )
            }
        }
    }
}

