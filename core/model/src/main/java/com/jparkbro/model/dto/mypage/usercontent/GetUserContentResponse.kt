package com.jparkbro.model.dto.mypage.usercontent

import com.jparkbro.model.common.Cursor
import com.jparkbro.model.common.actor.UserLikedActorDto
import com.jparkbro.model.common.actor.toPerson
import com.jparkbro.model.common.anime.UserContentAnimeDto
import com.jparkbro.model.common.anime.toAnime
import com.jparkbro.model.common.review.UserContentReviewDto
import com.jparkbro.model.common.review.toReview
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetUserContentResponse(
    @SerialName("count")
    val count: Int? = null,
    @SerialName("cursor")
    val cursor: Cursor? = null,
    @SerialName("animes")
    val animes: List<UserContentAnimeDto>? = emptyList(),
    @SerialName("reviews")
    val reviews: List<UserContentReviewDto>? = emptyList(),
    @SerialName("persons")
    val actors: List<UserLikedActorDto>? = emptyList()
)

fun GetUserContentResponse.toResult() : GetUserContentResult = GetUserContentResult(
    count = count,
    cursor = cursor,
    animes = animes?.map { it.toAnime() } ?: emptyList(),
    reviews = reviews?.map { it.toReview() } ?: emptyList(),
    actors = actors?.map { it.toPerson() } ?: emptyList()
)
