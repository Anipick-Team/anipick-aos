package com.jparkbro.network.anime

import com.jparkbro.model.common.Cursor
import com.jparkbro.model.common.anime.InfoSeriesAnimeDto
import com.jparkbro.model.common.anime.SimpleAnimeDto
import com.jparkbro.model.dto.explore.GetAnimeExploreRequest
import com.jparkbro.model.dto.explore.GetAnimeExploreResponse
import com.jparkbro.model.dto.info.WatchStatusRequest
import com.jparkbro.model.dto.home.main.RecommendedAnimesResponse
import com.jparkbro.model.dto.info.AnimeInfoResponse
import com.jparkbro.model.dto.info.GetInfoRecommendResponse
import com.jparkbro.model.dto.info.GetInfoSeriesResponse
import com.jparkbro.model.dto.mypage.usercontent.GetUserContentRequest
import com.jparkbro.model.dto.mypage.usercontent.GetUserContentResponse
import com.jparkbro.model.dto.ranking.GetAnimeRankingRequest
import com.jparkbro.model.dto.ranking.GetAnimeRankingResponse
import com.jparkbro.model.dto.search.GetPopularAnimeResponse
import com.jparkbro.model.dto.search.GetSearchResultRequest
import com.jparkbro.model.dto.search.GetSearchResultResponse
import com.jparkbro.model.enum.WatchStatus
import com.jparkbro.network.ranking.RetrofitRankingDataSource
import com.jparkbro.network.search.RetrofitSearchDataSource
import com.jparkbro.network.util.toResult
import com.jparkbro.network.util.toUnitResult
import javax.inject.Inject

class RetrofitAnimeDataSource @Inject constructor(
    private val animeApi: AnimeApi
) : AnimeDataSource {
    companion object {
        private const val TAG = "RetrofitAnimeDataSource"
    }

    override suspend fun getRecentRecommendItems(animeId: Long): Result<RecommendedAnimesResponse> {
        return animeApi.getRecentRecommendItems(animeId).toResult(TAG, "getRecentRecommendItems")
    }

    override suspend fun getDetailInfo(animeId: Long): Result<AnimeInfoResponse> {
        return animeApi.getDetailInfo(animeId).toResult(TAG, "getDetailInfo")
    }

    override suspend fun getDetailSeries(animeId: Long): Result<List<InfoSeriesAnimeDto>> {
        return animeApi.getDetailSeries(animeId).toResult(TAG, "getDetailSeries")
    }

    override suspend fun getDetailRecommendation(animeId: Long): Result<List<SimpleAnimeDto>> {
        return animeApi.getDetailRecommendation(animeId).toResult(TAG, "getDetailRecommendation")
    }

    override suspend fun setLikeAnime(animeId: Long): Result<Unit> {
        return animeApi.setLikeAnime(animeId).toUnitResult(TAG, "setLikeAnime")
    }

    override suspend fun setUnlikeAnime(animeId: Long): Result<Unit> {
        return animeApi.setUnlikeAnime(animeId).toUnitResult(TAG, "setUnlikeAnime")
    }

    override suspend fun createWatchStatus(animeId: Long, status: WatchStatus): Result<Unit> {
        return animeApi.createWatchStatus(
            animeId = animeId,
            request = WatchStatusRequest(status = status.name)
        ).toUnitResult(TAG, "createWatchStatus")
    }

    override suspend fun updateWatchStatus(animeId: Long, status: WatchStatus): Result<Unit> {
        return animeApi.updateWatchStatus(
            animeId = animeId,
            request = WatchStatusRequest(status = status.name)
        ).toUnitResult(TAG, "updateWatchStatus")
    }

    override suspend fun deleteWatchStatus(animeId: Long): Result<Unit> {
        return animeApi.deleteWatchStatus(animeId).toUnitResult(TAG, "deleteWatchStatus")
    }

    override suspend fun getAnimeSeries(animeId: Long, cursor: Cursor?): Result<GetInfoSeriesResponse> {
        return animeApi.getAnimeSeries(
            animeId = animeId,
            lastId = cursor?.lastId
        ).toResult(TAG, "getAnimeSeries")
    }

    override suspend fun getAnimeRecommends(animeId: Long, cursor: Cursor?): Result<GetInfoRecommendResponse> {
        return animeApi.getAnimeRecommends(
            animeId = animeId,
            lastId = cursor?.lastId
        ).toResult(TAG, "getAnimeRecommends")
    }

    override suspend fun loadWatchListAnimes(request: GetUserContentRequest): Result<GetUserContentResponse> {
        return animeApi.loadWatchListAnimes(
            status = "WATCHLIST",
            lastId = request.lastId
        ).toResult(TAG, "loadWatchListAnimes")
    }

    override suspend fun loadWatchingAnimes(request: GetUserContentRequest): Result<GetUserContentResponse> {
        return animeApi.loadWatchingAnimes(
            status = "WATCHING",
            lastId = request.lastId
        ).toResult(TAG, "loadWatchingAnimes")
    }

    override suspend fun loadFinishedAnimes(request: GetUserContentRequest): Result<GetUserContentResponse> {
        return animeApi.loadFinishedAnimes(
            status = "FINISHED",
            lastId = request.lastId
        ).toResult(TAG, "loadFinishedAnimes")
    }

    override suspend fun loadLikedAnimes(request: GetUserContentRequest): Result<GetUserContentResponse> {
        return animeApi.loadLikedAnimes(
            lastId = request.lastId
        ).toResult(TAG, "loadLikedAnimes")
    }

    override suspend fun getRealTimeRanking(request: GetAnimeRankingRequest): Result<GetAnimeRankingResponse> {
        return animeApi.getRealTimeRanking(
            genre = if (request.genre?.id != -1) request.genre?.name else null,
            lastId = request.lastId,
            lastValue = request.lastValue,
            size = request.size,
        ).toResult(TAG, "getRealTimeRanking")
    }

    override suspend fun getYearSeasonRanking(request: GetAnimeRankingRequest): Result<GetAnimeRankingResponse> {
        return animeApi.getYearSeasonRanking(
            year = if (request.year == "전체년도") null else request.year,
            season = when (request.season) {
                "1분기" -> 1
                "2분기" -> 2
                "3분기" -> 3
                "4분기" -> 4
                else -> null
            },
            genre = if (request.genre?.id != -1) request.genre?.name else null,
            lastId = request.lastId,
            lastRank = request.lastRank,
            size = request.size,
        ).toResult(TAG, "getYearSeasonRanking")
    }

    override suspend fun getAllTimeRanking(request: GetAnimeRankingRequest): Result<GetAnimeRankingResponse> {
        return animeApi.getAllTimeRanking(
            genre = if (request.genre?.id != -1) request.genre?.name else null,
            lastId = request.lastId,
            lastRank = request.lastRank,
            size = request.size,
        ).toResult(TAG, "getAllTimeRanking")
    }

    override suspend fun getAnimeExplore(request: GetAnimeExploreRequest): Result<GetAnimeExploreResponse> {
        return animeApi.getAnimeExplore(
            year = if (request.year == "전체년도") null else request.year,
            season = when (request.season) {
                "1분기" -> 1
                "2분기" -> 2
                "3분기" -> 3
                "4분기" -> 4
                else -> null
            },
            genres = if (request.genres.isNotEmpty()) request.genres.map { it.id.toLong() } else emptyList(),
            type = request.type,
            sort = request.sort.param,
            lastId = request.lastId,
            size = request.size,
            genreOp = request.genreOp.name,
            lastValue = request.lastValue
        ).toResult(TAG, "getAnimeExplore")
    }

    override suspend fun getPopularAnimes(): Result<GetPopularAnimeResponse> {
        return animeApi.getPopularAnimes().toResult(TAG, "getPopularAnimes")
    }

    override suspend fun getSearchResult(request: GetSearchResultRequest): Result<GetSearchResultResponse> {
        return animeApi.getSearchResult(
            query = request.query,
            lastId = request.lastId,
            size = request.size,
            page = request.page
        ).toResult(TAG, "getSearchResult")
    }

    override suspend fun submitAnimeLog(logUrl: String): Result<Unit> {
        return animeApi.submitLogByUrl(
            url = logUrl
        ).toUnitResult(TAG, "submitAnimeLog")
    }
}