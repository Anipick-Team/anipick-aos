package com.jparkbro.model.dto.info

import com.jparkbro.model.common.Cursor
import com.jparkbro.model.common.anime.Anime

data class GetInfoRecommendResult(
    val referenceAnimeTitle: String? = null,
    val cursor: Cursor? = null,
    val animes: List<Anime> = emptyList()
)
