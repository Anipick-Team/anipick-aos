package com.jparkbro.model.dto.ranking

import com.jparkbro.model.common.Cursor
import com.jparkbro.model.common.anime.RankingAnimeDto
import com.jparkbro.model.common.anime.toAnime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetAnimeRankingResponse(
    @SerialName("cursor")
    val cursor: Cursor,
    @SerialName("animes")
    val animes: List<RankingAnimeDto>
)

fun GetAnimeRankingResponse.toResult() : GetAnimeRankingResult = GetAnimeRankingResult(
    cursor = cursor,
    animes = animes.map { it.toAnime() }
)
