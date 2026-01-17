package com.jparkbro.model.detail

import com.jparkbro.model.common.Cursor
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ActorDetailResponse(
    @SerialName("personId")
    val personId: Long,
    @SerialName("name")
    val name: String? = null,
    @SerialName("profileImageUrl")
    val profileImageUrl: String? = null,
    @SerialName("isLiked")
    val isLiked: Boolean = false,
    @SerialName("count")
    val count: Int = 0,
    @SerialName("cursor")
    val cursor: Cursor? = null,
    @SerialName("works")
    val works: List<ActorFilmography> = emptyList(),
)

@Serializable
data class ActorFilmography(
    @SerialName("animeId")
    val animeId: Long,
    @SerialName("animeTitle")
    val animeTitle: String? = null,
    @SerialName("characterId")
    val characterId: Long,
    @SerialName("characterName")
    val characterName: String? = null,
    @SerialName("characterImageUrl")
    val characterImageUrl: String? = null,
)
