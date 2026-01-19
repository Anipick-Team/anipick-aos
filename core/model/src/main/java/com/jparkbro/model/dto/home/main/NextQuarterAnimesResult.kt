package com.jparkbro.model.dto.home.main

import com.jparkbro.model.common.anime.Anime

data class NextQuarterAnimesResult(
    val season: Int? = null,
    val seasonYear: Int? = null,
    val animes: List<Anime> = emptyList(),
)

