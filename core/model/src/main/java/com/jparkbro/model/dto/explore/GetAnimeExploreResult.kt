package com.jparkbro.model.dto.explore

import com.jparkbro.model.common.Cursor
import com.jparkbro.model.common.anime.Anime

data class GetAnimeExploreResult(
    val count: Int? = null,
    val cursor: Cursor? = null,
    val animes: List<Anime> = emptyList()
)
