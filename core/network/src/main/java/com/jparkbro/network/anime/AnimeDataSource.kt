package com.jparkbro.network.anime

import com.jparkbro.model.common.Cursor
import com.jparkbro.model.common.anime.InfoSeriesAnimeDto
import com.jparkbro.model.common.anime.SimpleAnimeDto
import com.jparkbro.model.dto.home.main.RecommendedAnimesResponse
import com.jparkbro.model.dto.info.AnimeInfoResponse
import com.jparkbro.model.dto.info.GetInfoRecommendResponse
import com.jparkbro.model.dto.info.GetInfoSeriesResponse
import com.jparkbro.model.enum.WatchStatus

interface AnimeDataSource {
    /** Home */
    suspend fun getRecentRecommendItems(animeId: Long): Result<RecommendedAnimesResponse>

    /** Anime Detail */
    suspend fun getDetailInfo(animeId: Long): Result<AnimeInfoResponse>
    suspend fun getDetailSeries(animeId: Long): Result<List<InfoSeriesAnimeDto>>
    suspend fun getDetailRecommendation(animeId: Long): Result<List<SimpleAnimeDto>>
    suspend fun setLikeAnime(animeId: Long): Result<Unit>
    suspend fun setUnlikeAnime(animeId: Long): Result<Unit>
    suspend fun createWatchStatus(animeId: Long, status: WatchStatus): Result<Unit>
    suspend fun updateWatchStatus(animeId: Long, status: WatchStatus): Result<Unit>
    suspend fun deleteWatchStatus(animeId: Long): Result<Unit>

    /** Info Series */
    suspend fun getAnimeSeries(animeId: Long, cursor: Cursor?): Result<GetInfoSeriesResponse>
    /** Info Recommends */
    suspend fun getAnimeRecommends(animeId: Long, cursor: Cursor?): Result<GetInfoRecommendResponse>
}