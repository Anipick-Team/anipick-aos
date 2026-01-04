package com.jparkbro.network.anime

import com.jparkbro.model.dto.info.AnimeInfoResponse
import com.jparkbro.model.common.anime.InfoSeriesAnimeDto
import com.jparkbro.model.common.anime.SimpleAnimeDto

interface AnimeDataSource {
    suspend fun getDetailInfo(animeId: Int): Result<AnimeInfoResponse>
    suspend fun getDetailSeries(animeId: Int): Result<List<InfoSeriesAnimeDto>>
    suspend fun getDetailRecommendation(animeId: Int): Result<List<SimpleAnimeDto>>
}