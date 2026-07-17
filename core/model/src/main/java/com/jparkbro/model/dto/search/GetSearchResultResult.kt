package com.jparkbro.model.dto.search

import com.jparkbro.model.common.Cursor
import com.jparkbro.model.common.Studio
import com.jparkbro.model.common.actor.Person
import com.jparkbro.model.common.anime.Anime

data class GetSearchResultResult(
    val count: Int,
    val animeCount: Int? = null,
    val personCount: Int? = null,
    val studioCount: Int? = null,
    val nextPage: Int? = null,
    val cursor: Cursor? = null,
    val animes: List<Anime> = emptyList(),
    val persons: List<Person> = emptyList(),
    val studios: List<Studio> = emptyList(),
)


