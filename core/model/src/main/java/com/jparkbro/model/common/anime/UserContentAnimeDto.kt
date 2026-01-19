package com.jparkbro.model.common.anime

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserContentAnimeDto(
    @SerialName("animeId")
    val animeId: Long? = null,
    @SerialName("title")
    val title: String? = null,
    @SerialName("coverImageUrl")
    val coverImageUrl: String? = null,
    @SerialName("animeLikeId")
    val animeLikeId: Int? = null,
    @SerialName("userAnimeStatusId")
    val userAnimeStatusId: Int? = null,
    @SerialName("myRating")
    val myRating: Float? = null
)

fun UserContentAnimeDto.toAnime() : Anime = Anime(
    animeId = animeId,
    title = title,
    coverImageUrl = coverImageUrl,
    animeLikeId = animeLikeId,
    userAnimeStatusId = userAnimeStatusId,
    myRating = myRating
)
