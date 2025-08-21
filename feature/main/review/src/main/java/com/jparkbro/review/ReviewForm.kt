package com.jparkbro.review

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jparkbro.model.common.FormType
import com.jparkbro.model.review.MyReview
import com.jparkbro.review.navigation.ReviewForm
import com.jparkbro.ui.APTitledBackTopAppBar
import com.jparkbro.ui.APToggleSwitch
import com.jparkbro.ui.BulletPointText
import com.jparkbro.ui.R
import com.jparkbro.ui.SnackBarData
import com.jparkbro.ui.theme.APColors
import com.jparkbro.ui.updateRatingFromPosition
import com.jparkbro.ui.util.extension.advancedImePadding

@Composable
internal fun ReviewForm(
    onNavigateBack: () -> Unit,
    onPopBackWithRefresh: () -> Unit,
    viewModel: ReviewFormViewModel = hiltViewModel()
) {
    val infoData = viewModel.infoData

    val review by viewModel.review.collectAsState()
    val newRating by viewModel.newRating.collectAsState()
    val newReviewContent by viewModel.newReviewContent.collectAsState()
    val includeSpoiler by viewModel.includeSpoiler.collectAsState()

    ReviewForm(
        infoData = infoData,
        review = review,
        newRating = newRating,
        newReviewContent = newReviewContent,
        includeSpoiler = includeSpoiler,
        onChangeRating = viewModel::updateRating,
        onChangeReviewContent = viewModel::updateReviewContent,
        onChangeIncludeSpoiler = viewModel::toggleIncludeSpoiler,
        onEditMyReview = viewModel::editReview,
        onNavigateBack = onNavigateBack,
        onPopBackWithRefresh = onPopBackWithRefresh,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ReviewForm(
    infoData: ReviewForm,
    review: MyReview?,
    newRating: Float,
    newReviewContent: String? = null,
    includeSpoiler: Boolean = false,
    onChangeRating: (Float) -> Unit,
    onChangeReviewContent: (String) -> Unit,
    onChangeIncludeSpoiler: () -> Unit,
    onEditMyReview: ((Boolean) -> Unit) -> Unit,
    onNavigateBack: () -> Unit,
    onPopBackWithRefresh: () -> Unit,
) {
    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = {
            APTitledBackTopAppBar(
                title = "리뷰 ${infoData.type.title}",
                handleBackNavigation = { onNavigateBack() },
            )
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .advancedImePadding()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    focusManager.clearFocus()
                },
        ) {
            item {
                Spacer(modifier = Modifier.fillMaxWidth().height(12.dp).background(APColors.Surface))
                Column(
                    modifier = Modifier
                        .background(APColors.White)
                        .padding(horizontal = 20.dp, vertical = 24.dp),
                    verticalArrangement = Arrangement.SpaceBetween,
                ) {
                    Column {
                        RatingContainer(
                            initRating = review?.rating ?: 0f,
                            onChangeRating = onChangeRating,
                        )
                        Column(
                            modifier = Modifier
                                .padding(vertical = 36.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                contentAlignment = Alignment.CenterEnd
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(
                                        text = "스포일러",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.W600,
                                        color = if (includeSpoiler) APColors.Secondary else APColors.Gray
                                    )
                                    APToggleSwitch(
                                        checked = includeSpoiler,
                                        checkedColor = APColors.Secondary,
                                        unCheckedColor = APColors.Gray,
                                        onCheckedChange = { onChangeIncludeSpoiler() },
                                    )
                                }
                            }
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(235.dp)
                                    .background(APColors.LightGray, RoundedCornerShape(8.dp))
                            ) {
                                TextField(
                                    value = "$newReviewContent",
                                    onValueChange = { onChangeReviewContent(it) },
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(bottom = 20.dp),
                                    textStyle = TextStyle(
                                        color = APColors.Black,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.W500,
                                        lineHeight = (1.4).em,
                                    ),
                                    placeholder = {
                                        Text(
                                            text = "리뷰 내용을 입력해주세요.",
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.W500,
                                            color = APColors.TextGray
                                        )
                                    },
                                    colors = TextFieldDefaults.colors(
                                        focusedIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent,
                                        focusedContainerColor = Color.Transparent,
                                        unfocusedContainerColor = Color.Transparent
                                    )
                                )
                                Text(
                                    text = "${newReviewContent?.length}/200",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.W500,
                                    color = APColors.TextGray,
                                    modifier = Modifier
                                        .align(Alignment.BottomEnd)
                                        .padding(20.dp)
                                )
                            }
                            Column(
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = "주의사항",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.W500,
                                    color = APColors.TextGray,
                                    lineHeight = (1.4).em
                                )
                                val guideTextStyle = TextStyle(
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.W500,
                                    color = APColors.TextGray,
                                    lineHeight = (1.4).em
                                )
                                Column {
                                    Text(
                                        text = "커뮤니티 가이드라인 위반 시 게시물이 삭제되며 서비스 이용이 일정기간 제한되거나 영구적으로 제한될 수 있습니다.",
                                        style = guideTextStyle
                                    )
                                    BulletPointText(
                                        text = "악의적인 욕설, 비방, 혐오 표현 등 타인에게 불쾌감을 줄 수 있는 내용",
                                        textStyle = guideTextStyle
                                    )
                                    BulletPointText(
                                        text = "스포일러 체크 없이 스포일러를 포함한 리뷰\n(※ 에피소드 내용 요약, 결말 노출 등)",
                                        textStyle = guideTextStyle
                                    )
                                    BulletPointText(
                                        text = "광고, 홍보, 도배 등 리뷰 목적과 무관한 내용",
                                        textStyle = guideTextStyle
                                    )
                                    BulletPointText(
                                        text = "음란물, 성적 수치심을 유발하는 내용",
                                        textStyle = guideTextStyle
                                    )
                                    BulletPointText(
                                        text = "기타 커뮤니티 가이드라인 운영 정책에 위반되는 내용",
                                        textStyle = guideTextStyle
                                    )
                                }
                                Text(
                                    text = "커뮤니티 가이드라인 전체보기",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.W500,
                                    color = APColors.White,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .clickable {}
                                        .background(APColors.TextGray, RoundedCornerShape(8.dp))
                                        .padding(8.dp)
                                )
                            }
                        }
                    }
                    TextButton(
                        onClick = { onEditMyReview { result ->
                            if (result) onPopBackWithRefresh()
                        } },
                        modifier = Modifier
                            .fillMaxWidth(),
                        enabled = (newRating != 0f) && (newReviewContent?.isNotBlank() == true) && (newReviewContent.length <= 200),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.textButtonColors(
                            containerColor = APColors.Primary,
                            disabledContainerColor = APColors.Gray,
                            contentColor = APColors.White
                        ),
                        contentPadding = PaddingValues(vertical = 15.dp),
                    ) {
                        Text(
                            text = when (infoData.type) {
                                FormType.CREATE -> "리뷰 작성하기"
                                FormType.EDIT -> "리뷰 수정하기"
                            },
                            fontSize = 16.sp,
                            fontWeight = FontWeight.W500
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RatingContainer(
    initRating: Float,
    onChangeRating: (Float) -> Unit
) {
    var rating by remember(initRating) { mutableFloatStateOf(initRating) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(APColors.Surface, RoundedCornerShape(8.dp))
            .padding(vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier
                .pointerInput(Unit) {
                    /* 별 점 드래그 */
                    detectDragGestures(
                        onDragStart = { offset ->
                            updateRatingFromPosition(offset.x, size.width, 5).let {
                                rating = it
                            }
                        },
                        onDragEnd = { onChangeRating(rating) },
                        onDragCancel = { rating = initRating },
                        onDrag = { change, _ ->
                            updateRatingFromPosition(change.position.x, size.width, 5).let {
                                rating = it
                            }
                        }
                    )
                }
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            for (i in 0 until 5) {
                Box(
                    modifier = Modifier
                        .size(31.dp)
                        .pointerInput(Unit) {
                            /* 별 점 클릭 */
                            detectTapGestures { offset ->
                                val position = offset.x / size.width
                                val newRating = if (position < 0.5f) {
                                    // 왼쪽 절반 (0.5점)
                                    i + 0.5f
                                } else {
                                    // 오른쪽 절반 (1.0점)
                                    i + 1.0f
                                }
                                rating = newRating
                                onChangeRating(rating)
                            }
                        }
                ) {
                    /* 빈 별 */
                    Image(
                        painter = painterResource(R.drawable.ic_star_outline),
                        contentDescription = "별점",
                        modifier = Modifier.fillMaxSize(),
                        colorFilter = null
                    )

                    /* 별 채우기 */
                    when {
                        i < rating.toInt() -> {
                            Image(
                                painter = painterResource(R.drawable.ic_star_fill),
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                colorFilter = null,
                            )
                        }

                        i == rating.toInt() && rating % 1 != 0f -> {
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
        }
        Text(
            text = "(${rating}/5.0)",
            fontSize = 20.sp,
            fontWeight = FontWeight.W600,
            color = if (rating != 0f) APColors.Point else APColors.Gray
        )
    }
}