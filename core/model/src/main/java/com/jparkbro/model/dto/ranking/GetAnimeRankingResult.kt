package com.jparkbro.model.dto.ranking

import com.jparkbro.model.common.Cursor
import com.jparkbro.model.common.anime.Anime

data class GetAnimeRankingResult(
    val cursor: Cursor,
    val animes: List<Anime>
)
