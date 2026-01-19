package com.jparkbro.model.dto.studio

import com.jparkbro.model.common.Cursor
import com.jparkbro.model.common.anime.Anime

data class GetStudioInfoResult(
    val studioName: String? = null,
    val cursor: Cursor? = null,
    val animes: List<Anime> = emptyList()
)
