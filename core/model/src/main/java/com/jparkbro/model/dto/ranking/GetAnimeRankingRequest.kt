package com.jparkbro.model.dto.ranking

import com.jparkbro.model.common.ResponseMap
import com.jparkbro.model.enum.RankingType

data class GetAnimeRankingRequest(
    val type: RankingType,
    val year: String? = null,
    val season: String? = null,
    val genre: ResponseMap? = null,
    val lastId: Long? = null,
    val lastValue: Long? = null,
    val lastRank: Int? = null,
    val size: Int? = null,
)
