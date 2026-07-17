package com.jparkbro.model.dto.explore

import com.jparkbro.model.common.ResponseMap
import com.jparkbro.model.enum.ExploreSortType
import com.jparkbro.model.enum.GenreOp

data class GetAnimeExploreRequest(
    val year: String? = null,
    val season: String? = null,
    val genres: List<ResponseMap> = emptyList(),
    val type: String? = null,
    val sort: ExploreSortType = ExploreSortType.POPULARITY,
    val lastId: Long? = null,
    val size: Int = 18,
    val genreOp: GenreOp = GenreOp.OR,
    val lastValue: String? = null,
)
