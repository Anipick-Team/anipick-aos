package com.jparkbro.mypage.detail

import com.jparkbro.model.common.Cursor
import com.jparkbro.model.common.UiState
import com.jparkbro.model.common.actor.Person
import com.jparkbro.model.common.anime.Anime
import com.jparkbro.model.common.review.Review
import com.jparkbro.model.enum.ReviewSortType
import com.jparkbro.model.enum.UserContentType

data class UserContentState(
    val uiState: UiState = UiState.Loading,
    val contentType: UserContentType? = null,
    val reviewSort: ReviewSortType = ReviewSortType.LATEST,

    /* API 통신 로딩 */
    val isMoreDataLoading: Boolean = false,
    val hasMoreData: Boolean = true,

    /* API 통신 데이터 */
    val count: Int? = 0,
    val cursor: Cursor? = null,
    val animes: List<Anime> = emptyList(),
    val reviews: List<Review> = emptyList(),
    val actors: List<Person> = emptyList()
)