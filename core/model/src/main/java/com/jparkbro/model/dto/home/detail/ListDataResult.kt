package com.jparkbro.model.dto.home.detail

import com.jparkbro.model.common.Cursor
import com.jparkbro.model.common.anime.Anime
import com.jparkbro.model.common.review.Review

data class ListDataResult(
    val referenceAnimeTitle: String? = null,
    val count: Int? = null,
    val cursor: Cursor? = null,
    val animes: List<Anime> = emptyList(),
    val reviews: List<Review> = emptyList(),
)
