package com.jparkbro.info.anime

import com.jparkbro.model.common.Cursor
import com.jparkbro.model.common.UiState
import com.jparkbro.model.common.anime.Anime
import com.jparkbro.model.common.review.Review
import com.jparkbro.model.dto.info.AnimeInfoResponse
import com.jparkbro.model.dto.info.InfoActorResult
import com.jparkbro.model.enum.ReviewSortType

data class InfoAnimeState(
    val uiState: UiState = UiState.Loading,
    val reviewSort: ReviewSortType = ReviewSortType.LATEST,

    /* API 통신 데이터 */
    val animeInfo: AnimeInfoResponse? = null,
    val casts: List<InfoActorResult> = emptyList(),
    val series: List<Anime> = emptyList(),
    val recommendations: List<Anime> = emptyList(),
    val myReview: Review = Review(),
    val reviews: List<Review> = emptyList(),
    val reviewCount: Int = 0,
    val reviewCursor: Cursor? = null,
    val isLoadingMoreReviews: Boolean = false,
)
