package com.jparkbro.model.dto.home.detail

import com.jparkbro.model.common.Cursor
import com.jparkbro.model.common.anime.UpcomingReleasesAnimeDto
import com.jparkbro.model.common.anime.toAnime
import com.jparkbro.model.common.review.HomeDetailReviewDto
import com.jparkbro.model.common.review.toReview
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ListDataResponse(
    @SerialName("referenceAnimeTitle")
    val referenceAnimeTitle: String? = null,
    @SerialName("count")
    val count: Int? = null,
    @SerialName("cursor")
    val cursor: Cursor? = null,
    @SerialName("animes")
    val animes: List<UpcomingReleasesAnimeDto> = emptyList(),
    @SerialName("reviews")
    val reviews: List<HomeDetailReviewDto> = emptyList(),
)

fun ListDataResponse.toResult(): ListDataResult = ListDataResult(
    referenceAnimeTitle = referenceAnimeTitle,
    count = count,
    cursor = cursor,
    animes = animes.map { it.toAnime() },
    reviews = reviews.map { it.toReview() }
)