package com.jparkbro.data.anime

import com.jparkbro.model.common.Cursor
import com.jparkbro.model.common.anime.Anime
import com.jparkbro.model.dto.explore.GetAnimeExploreRequest
import com.jparkbro.model.dto.explore.GetAnimeExploreResult
import com.jparkbro.model.dto.home.main.RecommendedAnimesResult
import com.jparkbro.model.dto.info.AnimeInfoResponse
import com.jparkbro.model.dto.info.GetInfoRecommendResult
import com.jparkbro.model.dto.info.GetInfoSeriesResult
import com.jparkbro.model.dto.mypage.usercontent.GetUserContentRequest
import com.jparkbro.model.dto.ranking.GetAnimeRankingRequest
import com.jparkbro.model.dto.ranking.GetAnimeRankingResult
import com.jparkbro.model.dto.search.GetSearchResultRequest
import com.jparkbro.model.dto.search.GetSearchResultResult
import com.jparkbro.model.enum.WatchStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface AnimeRepository {
    suspend fun saveRecentAnime(animeId: Long): Result<Unit>
    suspend fun loadRecentAnime(): Result<Long>

    /** Home */
    val recentRecommendAnime: StateFlow<RecommendedAnimesResult?>
    suspend fun getRecentRecommendItems(animeId: Long): Result<Unit>

    /** Anime Detail */
    fun getDetailInfo(animeId: Long): Flow<AnimeInfoResponse?>
    suspend fun loadDetailInfo(animeId: Long): Result<Unit>
    suspend fun getDetailSeries(animeId: Long): Result<List<Anime>>
    suspend fun getDetailRecommendation(animeId: Long): Result<List<Anime>>

    suspend fun updateAnimeLike(animeId: Long, isLiked: Boolean): Result<Unit>
    suspend fun updateWatchStatus(animeId: Long, watchStatus: WatchStatus): Result<Unit>

    /** Info Series */
    suspend fun getAnimeSeries(animeId: Long, cursor: Cursor?): Result<GetInfoSeriesResult>
    /** Info Recommends */
    suspend fun getAnimeRecommends(animeId: Long, cursor: Cursor?): Result<GetInfoRecommendResult>

    /** User Content */
    suspend fun loadUserContentAnimes(request: GetUserContentRequest): Result<Unit>
    suspend fun invalidateUserContent()

    /** Ranking */
    suspend fun getAnimeRanking(request: GetAnimeRankingRequest): Result<GetAnimeRankingResult>

    /** Explore */
    suspend fun getAnimeExplore(request: GetAnimeExploreRequest): Result<GetAnimeExploreResult>

    /** Search */
    suspend fun getPopularAnimes(): Result<List<Anime>>
    suspend fun getSearchResult(request: GetSearchResultRequest): Result<GetSearchResultResult>
    suspend fun submitAnimeLog(logUrl: String): Result<Unit>
}