package com.jparkbro.model.detail

import com.jparkbro.model.common.Cursor
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DetailStudio(
    @SerialName("studioName")
    val studioName: String? = null,
    @SerialName("cursor")
    val cursor: Cursor,
    @SerialName("animes")
    val animes: List<StudioAnime>
)

@Serializable
data class StudioAnime(
    @SerialName("animeId")
    val animeId: Int,
    @SerialName("title")
    val title: String? = null,
    @SerialName("coverImageUrl")
    val coverImageUrl: String? = null,
    @SerialName("seasonYear")
    val seasonYear: String = "",
)