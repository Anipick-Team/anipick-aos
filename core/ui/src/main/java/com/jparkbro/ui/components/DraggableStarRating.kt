package com.jparkbro.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jparkbro.ui.R
import kotlin.math.roundToInt

@Composable
fun DraggableStarRating(
    modifier: Modifier = Modifier,
    maxRating: Int = 5,
    initialRating: Float = 0f,
    starSize: Dp = 48.dp,
    starSpacing: Dp = 4.dp,
    onRatingChanged: (Float) -> Unit
) {
    var rating by remember(initialRating) { mutableFloatStateOf(initialRating) }
    var isDragging by remember { mutableStateOf(false) }

    Row(
        modifier = modifier
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { offset ->
                        isDragging = true
                        // 드래그 시작 위치에서 별점 계산
                        updateRatingFromPosition(offset.x, size.width, maxRating).let {
                            rating = it
                            onRatingChanged(it)
                        }
                    },
                    onDragEnd = {
                        isDragging = false
                    },
                    onDragCancel = {
                        isDragging = false
                    },
                    onDrag = { change, _ ->
                        // 드래그 위치에서 별점 계산
                        updateRatingFromPosition(change.position.x, size.width, maxRating).let {
                            rating = it
                            onRatingChanged(it)
                        }
                    }
                )
            }
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(starSpacing)
    ) {
        for (i in 0 until maxRating) {
            Box(
                modifier = Modifier
                    .size(starSize)
                    .pointerInput(Unit) {
                        detectTapGestures { offset ->
                            // 별의 왼쪽 절반 또는 오른쪽 절반인지 확인
                            val position = offset.x / size.width
                            val newRating = if (position < 0.5f) {
                                // 왼쪽 절반 (0.5점)
                                i + 0.5f
                            } else {
                                // 오른쪽 절반 (1.0점)
                                i + 1.0f
                            }
                            rating = newRating
                            onRatingChanged(newRating)
                        }
                    }
            ) {
                // 빈 별 (배경)
                Image(
                    painter = painterResource(R.drawable.ic_star_outline),
                    contentDescription = "별점",
                    modifier = Modifier.fillMaxSize(),
                    colorFilter = null
                )

                // 별 채우기 (전체, 반쪽)
                when {
                    i < rating.toInt() -> {
                        // 전체 별
                        Image(
                            painter = painterResource(R.drawable.ic_star_fill),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            colorFilter = null,
                        )
                    }
                    i == rating.toInt() && rating % 1 != 0f -> {
                        // 반쪽 별 (클리핑을 사용하여 구현)
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
}

// 드래그 위치에 따라 별점 계산하는 함수 (일반 함수로 변경)
// 함수 매개변수 타입을 Int로 변경
fun updateRatingFromPosition(x: Float, totalWidth: Int, maxRating: Int): Float {
    if (x < 0f) return 0f
    if (x > totalWidth) return maxRating.toFloat()

    // 전체 너비에서의 비율 계산 (Int를 Float로 변환)
    val positionRatio = x / totalWidth.toFloat()

    // 0.5 단위로 별점 계산
    val rawRating = positionRatio * maxRating
    val halfStepRating = (rawRating * 2).roundToInt() / 2f

    return halfStepRating
}