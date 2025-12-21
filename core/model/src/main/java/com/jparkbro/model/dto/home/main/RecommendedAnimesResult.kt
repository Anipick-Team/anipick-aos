package com.jparkbro.model.dto.home.main

import com.jparkbro.model.common.anime.Anime

data class RecommendedAnimesResult(
    val referenceAnimeTitle: String? = null,
    val animes: List<Anime> = emptyList(),
)
