package com.jparkbro.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.jparkbro.model.common.MyDropdownMenu
import com.jparkbro.model.common.OtherDropdownMenu
import com.jparkbro.model.common.ReviewItem
import com.jparkbro.model.home.HomeReviewItem
import com.jparkbro.model.mypage.MyReviewItem
import com.jparkbro.ui.theme.APColors
import com.jparkbro.ui.util.extension.toImageModel

@Composable
fun <T : ReviewItem> APReviewItem(
    reviewItem: T,
    onClickUpdate: () -> Unit = {},
    onClickDelete: () -> Unit = {},
    onCLickReport: () -> Unit = {},
    onClickBlock: () -> Unit = {},
    isLikedLoading: Boolean = false,
    onClickLiked: (isLiked: Boolean, callback: (Boolean) -> Unit) -> Unit = { _, _ -> },
    onNavigateAnimeDetail: (Int) -> Unit = { },
) {
    var expanded by remember { mutableStateOf(false) }
    var isTextOverflowing by remember { mutableStateOf(false) }

    var showDropDown by remember { mutableStateOf(false) }

    var isLiked by rememberSaveable { mutableStateOf(reviewItem.isLiked) }
    var likeCount by rememberSaveable { mutableIntStateOf(reviewItem.likeCount) }

    Column(
        modifier = Modifier
            .background(Color.White, RoundedCornerShape(8.dp))
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        if (reviewItem is HomeReviewItem) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateAnimeDetail(reviewItem.animeId) },
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = reviewItem.animeCoverImageUrl?.toImageModel(), // TODO 사이즈 수정필요
                    contentDescription = null,
                    modifier = Modifier
                        .width(79.dp)
                        .height(63.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
                Text(
                    text = "${reviewItem.animeTitle}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W500,
                    color = APColors.Black
                )
            }
            HorizontalDivider(modifier = Modifier.fillMaxWidth(), thickness = 1.dp, color = APColors.Surface)
        }
        if (reviewItem is MyReviewItem) { // TODO 수정필요 ( DTO 통일 )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateAnimeDetail(reviewItem.animeId) },
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = reviewItem.coverImageUrl.toImageModel(),  // TODO 사이즈 수정필요
                    contentDescription = null,
                    modifier = Modifier
                        .width(79.dp)
                        .height(63.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
                Text(
                    text = "${reviewItem.title}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W500,
                    color = APColors.Black
                )
            }
            HorizontalDivider(modifier = Modifier.fillMaxWidth(), thickness = 1.dp, color = APColors.Surface)
        }
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    for (i in 0 until 5) {
                        Box(
                            modifier = Modifier
                                .size(20.dp)
                        ) {
                            Image(
                                painter = painterResource(R.drawable.ic_star_outline),
                                contentDescription = "별점",
                                modifier = Modifier.fillMaxSize(),
                                colorFilter = null
                            )
                            when {
                                i < reviewItem.rating.toInt() -> {
                                    Image(
                                        painter = painterResource(R.drawable.ic_star_fill),
                                        contentDescription = null,
                                        modifier = Modifier.fillMaxSize(),
                                        colorFilter = null,
                                    )
                                }

                                i == reviewItem.rating.toInt() && reviewItem.rating % 1 != 0f -> {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .clipToBounds()
                                            .drawWithContent {
                                                // 왼쪽 반쪽만 클리핑
                                                clipRect(left = 0f, top = 0f, right = size.width / 2, bottom = size.height) {
                                                    this@drawWithContent.drawContent()
                                                }
                                            }
                                    ) {
                                        Image(
                                            painter = painterResource(R.drawable.ic_star_fill),
                                            contentDescription = null,
                                            modifier = Modifier.fillMaxSize(),
                                            colorFilter = null,
                                        )
                                    }
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "${reviewItem.rating}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.W500,
                        color = APColors.TextGray
                    )
                }
                if (reviewItem !is MyReviewItem) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (reviewItem.profileImageUrl == null || reviewItem.profileImageUrl == "default.png") {
                            Image(
                                painter = painterResource(R.drawable.profile_default_img),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            AsyncImage(
                                model = reviewItem.profileImageUrl,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        }
                        Text(
                            text = reviewItem.nickname,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.W500,
                            color = APColors.Black
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = reviewItem.createdAt,
                fontSize = 12.sp,
                fontWeight = FontWeight.W400,
                color = APColors.Gray
            )
            if (reviewItem.content != null) {
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "${reviewItem.content}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W500,
                    color = APColors.Black,
                    lineHeight = (1.4).em,
                    maxLines = if (expanded) Int.MAX_VALUE else 2,
                    overflow = TextOverflow.Ellipsis,
                    onTextLayout = { textLayoutResult ->
                        isTextOverflowing = textLayoutResult.hasVisualOverflow
                    }
                )
            }
            if (isTextOverflowing || expanded) {
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier
                        .clickable { expanded = !expanded },
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (expanded) "접기" else "더보기",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.W500,
                        color = APColors.Primary
                    )
                    Icon(
                        painter = painterResource(if (expanded) R.drawable.ic_chevron_up else R.drawable.ic_chevron_down),
                        contentDescription = null,
                        tint = APColors.Primary,
                        modifier = Modifier
                            .size(15.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(if (isLiked) R.drawable.ic_favorite_on else R.drawable.ic_favorite_off),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier
                            .size(16.dp)
                            .clickable(enabled = !isLikedLoading) {
                                onClickLiked(!isLiked) { result ->
                                    if (result) {
                                        if (isLiked) likeCount -= 1 else likeCount += 1
                                        isLiked = !isLiked
                                    }
                                }
                            }
                    )
                    Text(
                        text = "$likeCount",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.W500,
                        color = if (isLiked) APColors.Primary else APColors.Gray
                    )
                }
                Box {
                    Icon(
                        painter = painterResource(R.drawable.ic_more_vertical),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier
                            .size(20.dp)
                            .clickable { showDropDown = !showDropDown }
                    )
                    DropdownMenu(
                        expanded = showDropDown,
                        onDismissRequest = { showDropDown = !showDropDown },
                        offset = DpOffset(x = 0.dp, y = 8.dp),
                        shape = RoundedCornerShape(8.dp),
                        containerColor = APColors.White,
                        shadowElevation = 2.dp,
                    ) {
                        Text(
                            text = if (reviewItem.isMine) MyDropdownMenu.DELETE.displayName else OtherDropdownMenu.REPORT.displayName,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.W500,
                            color = APColors.Black,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() }
                                ) {
                                    showDropDown = !showDropDown
                                    if (reviewItem.isMine) onClickDelete() else onCLickReport()
                                }
                                .padding(horizontal = 33.dp, vertical = 14.dp)
                        )
                        HorizontalDivider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 15.dp),
                            thickness = 1.dp,
                            color = APColors.Surface
                        )
                        Text(
                            text = if (reviewItem.isMine) MyDropdownMenu.UPDATE.displayName else OtherDropdownMenu.BLOCK.displayName,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.W500,
                            color = APColors.Black,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() }
                                ) {
                                    showDropDown = !showDropDown
                                    if (reviewItem.isMine) onClickUpdate() else onClickBlock()
                                }
                                .padding(horizontal = 33.dp, vertical = 14.dp)
                        )
                    }
                }
            }
        }
    }
}