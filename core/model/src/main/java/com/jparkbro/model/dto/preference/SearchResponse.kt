package com.jparkbro.model.dto.preference

import com.jparkbro.model.common.Anime
import com.jparkbro.model.common.Cursor
import com.jparkbro.model.common.ResponseMap
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchResponse(
    @SerialName("count")
    val count: Int,
    @SerialName("cursor")
    val cursor: Cursor,
    @SerialName("animes")
    val animes: List<PreferenceAnime>
)

@Serializable
data class PreferenceAnime(
    override val animeId: Int = -1,
    override val title: String = "애니메이션 제목",
    override val coverImageUrl: String? = null,
    val genres: List<String?> = emptyList()
) : Anime