package com.jparkbro.model.dto.preference

import com.jparkbro.model.common.anime.Anime
import com.jparkbro.model.common.Cursor

data class SearchResult(
    val count: Int? = null,
    val cursor: Cursor? = null,
    val animes: List<Anime> = emptyList()
)