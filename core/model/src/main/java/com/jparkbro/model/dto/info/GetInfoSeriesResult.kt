package com.jparkbro.model.dto.info

import com.jparkbro.model.common.Cursor
import com.jparkbro.model.common.anime.Anime

data class GetInfoSeriesResult(
    val count: Int,
    val cursor: Cursor,
    val animes: List<Anime>
)
