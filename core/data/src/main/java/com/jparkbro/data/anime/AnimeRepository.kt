package com.jparkbro.data.anime

import com.jparkbro.model.common.anime.Anime
import com.jparkbro.model.dto.info.AnimeInfoResponse
import kotlinx.coroutines.flow.StateFlow

interface AnimeRepository {

    /** Anime Detail */
    val detailInfo: StateFlow<AnimeInfoResponse?>
    suspend fun getDetailInfo(animeId: Int): Result<Unit>
    suspend fun getDetailSeries(animeId: Int): Result<List<Anime>>
    suspend fun getDetailRecommendation(animeId: Int): Result<List<Anime>>
}