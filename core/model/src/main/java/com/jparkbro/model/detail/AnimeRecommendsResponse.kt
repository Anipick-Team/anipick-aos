package com.jparkbro.model.detail

import com.jparkbro.model.common.Cursor
import com.jparkbro.model.common.DefaultAnime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AnimeRecommendsResponse(
    @SerialName("referenceAnimeTitle")
    val referenceAnimeTitle: String,
    @SerialName("cursor")
    val cursor: Cursor,
    @SerialName("animes")
    val animes: List<DefaultAnime>
)
