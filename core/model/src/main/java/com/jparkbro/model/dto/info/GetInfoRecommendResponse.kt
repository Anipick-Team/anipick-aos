package com.jparkbro.model.dto.info

import com.jparkbro.model.common.Cursor
import com.jparkbro.model.common.anime.SimpleAnimeDto
import com.jparkbro.model.common.anime.toAnime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetInfoRecommendResponse(
    @SerialName("referenceAnimeTitle")
    val referenceAnimeTitle: String?,
    @SerialName("cursor")
    val cursor: Cursor?,
    @SerialName("animes")
    val animes: List<SimpleAnimeDto> = emptyList()
)

fun GetInfoRecommendResponse.toResult() : GetInfoRecommendResult = GetInfoRecommendResult(
    referenceAnimeTitle = referenceAnimeTitle ?: "",
    cursor = cursor ?: Cursor(),
    animes = animes.map { it.toAnime() }
)
