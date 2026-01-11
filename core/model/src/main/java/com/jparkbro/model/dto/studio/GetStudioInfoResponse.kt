package com.jparkbro.model.dto.studio

import com.jparkbro.model.common.Cursor
import com.jparkbro.model.common.anime.StudioAnimeDto
import com.jparkbro.model.common.anime.toAnime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetStudioInfoResponse(
    @SerialName("studioName")
    val studioName: String? = null,
    @SerialName("cursor")
    val cursor: Cursor? = null,
    @SerialName("animes")
    val animes: List<StudioAnimeDto> = emptyList()
)

fun GetStudioInfoResponse.toResult() : GetStudioInfoResult = GetStudioInfoResult(
    studioName = studioName,
    cursor = cursor,
    animes = animes.map { it.toAnime() }
)

