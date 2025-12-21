package com.jparkbro.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.jparkbro.model.common.review.Review
import com.jparkbro.model.enum.MyDropdownMenu
import com.jparkbro.model.enum.OtherDropdownMenu
import com.jparkbro.ui.R
import com.jparkbro.ui.theme.AniPick12Normal
import com.jparkbro.ui.theme.AniPick12Regular
import com.jparkbro.ui.theme.AniPick14Normal
import com.jparkbro.ui.theme.AniPick16Normal
import com.jparkbro.ui.theme.AniPickBlack
import com.jparkbro.ui.theme.AniPickGray100
import com.jparkbro.ui.theme.AniPickGray400
import com.jparkbro.ui.theme.AniPickPrimary
import com.jparkbro.ui.theme.AniPickSmallShape
import com.jparkbro.ui.theme.AniPickSurface
import com.jparkbro.ui.theme.AniPickWhite
import com.jparkbro.ui.theme.ChevronDownIcon
import com.jparkbro.ui.theme.ChevronUpIcon
import com.jparkbro.ui.theme.FavoriteOffIcon
import com.jparkbro.ui.theme.FavoriteOnIcon
import com.jparkbro.ui.theme.MoreVerticalIcon
import com.jparkbro.ui.theme.StarFillIcon
import com.jparkbro.ui.theme.StarOutlineIcon

@Composable
fun APReviewCard(
    review: Review,
    onClickEdit: (Int, Int) -> Unit = {_, _ ->},
    onClickDelete: (Int) -> Unit = {},
    onCLickReport: (Int) -> Unit = {},
    onClickBlock: (Int) -> Unit = {},
    onClickLiked: (reviewId: Int, isLiked: Boolean, callback: (Boolean) -> Unit) -> Unit = {_, _, _ ->},
    onNavigateAnimeDetail: (Int) -> Unit = {},
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    var isTextOverflowing by rememberSaveable { mutableStateOf(false) }

    var showDropDown by rememberSaveable { mutableStateOf(false) }
    var isLikedLoading by rememberSaveable { mutableStateOf(false) }

    var isLiked by rememberSaveable { mutableStateOf(review.likedByCurrentUser) }
    var likeCount by rememberSaveable { mutableIntStateOf(review.likeCount ?: 0) }

    Column(
        modifier = Modifier
            .background(AniPickWhite, AniPickSmallShape)
            .padding(dimensionResource(R.dimen.padding_default)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_large))
    ) {
        if (review.animeCoverImageUrl != null || review.animeTitle != null) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateAnimeDetail(review.animeId ?: -1) },
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_default)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = review.animeCoverImageUrl,
                    contentDescription = stringResource(R.string.anime_cover_img),
                    error = painterResource(R.drawable.thumbnail_img),
                    modifier = Modifier
                        .width(132.dp)
                        .aspectRatio(2f/3f)
                        .clip(AniPickSmallShape),
                    contentScale = ContentScale.Crop,
                )
                review.animeTitle?.let {
                    Text(
                        text = it,
                        style = AniPick16Normal.copy(color = AniPickBlack)
                    )
                }
            }
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = dimensionResource(R.dimen.border_width_default),
                color = AniPickSurface
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_medium))
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_extra_small))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val rating = review.rating ?: 0f
                        val ratingInt = rating.toInt()
                        val hasHalfStar = rating.rem(1) != 0f

                        for (i in 0 until 5) {
                            Box(
                                modifier = Modifier
                                    .size(dimensionResource(R.dimen.icon_size_medium))
                            ) {
                                Image(
                                    imageVector = StarOutlineIcon,
                                    contentDescription = stringResource(R.string.outline_star_icon),
                                    modifier = Modifier.fillMaxSize(),
                                )
                                when {
                                    i < ratingInt -> {
                                        Image(
                                            imageVector = StarFillIcon,
                                            contentDescription = stringResource(R.string.fill_star_icon),
                                            modifier = Modifier.fillMaxSize(),
                                        )
                                    }

                                    i == ratingInt && hasHalfStar -> {
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
                                                imageVector = StarFillIcon,
                                                contentDescription = stringResource(R.string.fill_star_icon),
                                                modifier = Modifier.fillMaxSize(),
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.width(dimensionResource(R.dimen.spacing_medium)))
                        Text(
                            text = review.rating.toString(),
                            style = AniPick14Normal.copy(color = AniPickGray400)
                        )
                    }
                    if (review.nickname != null || review.profileImageUrl != null) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_small)),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AsyncImage(
                                model = review.profileImageUrl,
                                contentDescription = stringResource(R.string.profile_img),
                                error = painterResource(R.drawable.ic_mascot),
                                modifier = Modifier
                                    .size(30.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop,
                            )
                            review.nickname?.let {
                                Text(
                                    text = it,
                                    style = AniPick12Normal.copy(color = AniPickBlack)
                                )
                            }
                        }
                    }
                }
                review.createdAt?.let {
                    Text(
                        text = it,
                        style = AniPick12Regular.copy(color = AniPickGray100)
                    )
                }
            }
            Column(
                modifier = Modifier.animateContentSize(),
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_extra_small))
            ) {
                review.content?.let {
                    Text(
                        text = it,
                        style = AniPick14Normal.copy(color = AniPickBlack),
                        maxLines = if (expanded) Int.MAX_VALUE else 2,
                        overflow = TextOverflow.Ellipsis,
                        onTextLayout = { textLayoutResult ->
                            isTextOverflowing = textLayoutResult.hasVisualOverflow
                        }
                    )
                }
                if (isTextOverflowing || expanded) {
                    Row(
                        modifier = Modifier
                            .clickable { expanded = !expanded },
                        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_extra_small)),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(if (expanded) R.string.collapse else R.string.expand),
                            style = AniPick14Normal.copy(color = AniPickPrimary)
                        )
                        Icon(
                            imageVector = if (expanded) ChevronUpIcon else ChevronDownIcon,
                            contentDescription = stringResource(R.string.chevron_up_down_icon),
                            tint = AniPickPrimary,
                            modifier = Modifier
                                .size(dimensionResource(R.dimen.icon_size_small))
                        )
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_extra_small)),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (isLiked) FavoriteOnIcon else FavoriteOffIcon,
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier
                            .size(dimensionResource(R.dimen.icon_size_small))
                            .clickable(enabled = !isLikedLoading) {
                                isLikedLoading = true

                                onClickLiked(review.reviewId ?: 0, !isLiked) { result ->
                                    if (result) {
                                        if (isLiked) likeCount -= 1 else likeCount += 1
                                        isLiked = !isLiked
                                    }
                                    isLikedLoading = false
                                }
                            }
                    )
                    Text(
                        text = likeCount.toString(),
                        style = AniPick14Normal.copy(
                            color = if (isLiked) AniPickPrimary else AniPickGray100
                        )
                    )
                }
                Box {
                    Icon(
                        imageVector = MoreVerticalIcon,
                        contentDescription = stringResource(R.string.more_vert_icon),
                        tint = Color.Unspecified,
                        modifier = Modifier
                            .size(dimensionResource(R.dimen.icon_size_medium))
                            .clickable { showDropDown = !showDropDown }
                    )
                    DropdownMenu(
                        expanded = showDropDown,
                        onDismissRequest = { showDropDown = false },
                        offset = DpOffset(x = 0.dp, y = 8.dp),
                        shape = AniPickSmallShape,
                        containerColor = AniPickWhite,
                        shadowElevation = 2.dp,
                    ) {
                        Text(
                            text = if (review.isMine) MyDropdownMenu.DELETE.displayName else OtherDropdownMenu.REPORT.displayName,
                            style = AniPick14Normal.copy(color = AniPickBlack),
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    showDropDown = !showDropDown
                                    if (review.isMine) onClickDelete(review.reviewId ?: 0) else onCLickReport(review.reviewId ?: 0)
                                }
                                .padding(horizontal = dimensionResource(R.dimen.padding_huge), vertical = dimensionResource(R.dimen.padding_medium))
                        )
                        HorizontalDivider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = dimensionResource(R.dimen.padding_default)),
                            thickness = dimensionResource(R.dimen.border_width_default),
                            color = AniPickSurface
                        )
                        Text(
                            text = if (review.isMine) MyDropdownMenu.UPDATE.displayName else OtherDropdownMenu.BLOCK.displayName,
                            style = AniPick14Normal.copy(color = AniPickBlack),
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    showDropDown = !showDropDown
                                    if (review.isMine) onClickEdit(review.animeId ?: -1, review.reviewId ?: -1) else onClickBlock(review.userId ?: -1)
                                }
                                .padding(horizontal = dimensionResource(R.dimen.padding_huge), vertical = dimensionResource(R.dimen.padding_medium))
                        )
                    }
                }
            }
        }
    }
}