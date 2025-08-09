package com.jparkbro.model.mypage

import com.jparkbro.model.common.Cursor
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserContentResponse(
    @SerialName("count")
    val count: Int,
    @SerialName("cursor")
    val cursor: Cursor,
    @SerialName("animes")
    val animes: List<UserContentAnime> = emptyList(),
    @SerialName("persons")
    val persons: List<LikedPerson> = emptyList(),
)