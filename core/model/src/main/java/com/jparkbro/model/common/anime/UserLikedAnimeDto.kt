package com.jparkbro.model.common.anime

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserLikedAnimeDto(
    @SerialName("animeId")
    val animeId: Long,
    @SerialName("animeLikeId")
    val animeLikeId: Int,
    @SerialName("title")
    val title: String? = null,
    @SerialName("coverImageUrl")
    val coverImageUrl: String? = null,
)

fun UserLikedAnimeDto.toAnime() : Anime = Anime(
    animeId = animeId,
    animeLikeId = animeLikeId,
    title = title,
    coverImageUrl = coverImageUrl
)