package com.jparkbro.network.anime

import com.jparkbro.model.common.Cursor
import com.jparkbro.model.common.anime.InfoSeriesAnimeDto
import com.jparkbro.model.common.anime.SimpleAnimeDto
import com.jparkbro.model.dto.explore.GetAnimeExploreRequest
import com.jparkbro.model.dto.explore.GetAnimeExploreResponse
import com.jparkbro.model.dto.home.main.RecommendedAnimesResponse
import com.jparkbro.model.dto.info.AnimeInfoResponse
import com.jparkbro.model.dto.info.GetInfoRecommendResponse
import com.jparkbro.model.dto.info.GetInfoSeriesResponse
import com.jparkbro.model.dto.info.WatchStatusRequest
import com.jparkbro.model.dto.mypage.usercontent.GetUserContentRequest
import com.jparkbro.model.dto.mypage.usercontent.GetUserContentResponse
import com.jparkbro.model.dto.ranking.GetAnimeRankingRequest
import com.jparkbro.model.dto.ranking.GetAnimeRankingResponse
import com.jparkbro.model.dto.search.GetPopularAnimeResponse
import com.jparkbro.model.dto.search.GetSearchResultRequest
import com.jparkbro.model.dto.search.GetSearchResultResponse
import com.jparkbro.model.enum.WatchStatus
import com.jparkbro.network.util.safeApiCall
import com.jparkbro.network.util.safeApiCallUnit
import javax.inject.Inject

class RetrofitAnimeDataSource @Inject constructor(
    private val animeApi: AnimeApi
) : AnimeDataSource {
    companion object {
        private const val TAG = "RetrofitAnimeDataSource"
    }

    override suspend fun getRecentRecommendItems(animeId: Long): Result<RecommendedAnimesResponse> {
        return safeApiCall(TAG, "getRecentRecommendItems") { animeApi.getRecentRecommendItems(animeId) }
    }

    override suspend fun getDetailInfo(animeId: Long): Result<AnimeInfoResponse> {
        return safeApiCall(TAG, "getDetailInfo") { animeApi.getDetailInfo(animeId) }
    }

    override suspend fun getDetailSeries(animeId: Long): Result<List<InfoSeriesAnimeDto>> {
        return safeApiCall(TAG, "getDetailSeries") { animeApi.getDetailSeries(animeId) }
    }

    override suspend fun getDetailRecommendation(animeId: Long): Result<List<SimpleAnimeDto>> {
        return safeApiCall(TAG, "getDetailRecommendation") { animeApi.getDetailRecommendation(animeId) }
    }

    override suspend fun setLikeAnime(animeId: Long): Result<Unit> {
        return safeApiCallUnit(TAG, "setLikeAnime") { animeApi.setLikeAnime(animeId) }
    }

    override suspend fun setUnlikeAnime(animeId: Long): Result<Unit> {
        return safeApiCallUnit(TAG, "setUnlikeAnime") { animeApi.setUnlikeAnime(animeId) }
    }

    override suspend fun createWatchStatus(animeId: Long, status: WatchStatus): Result<Unit> {
        return safeApiCallUnit(TAG, "createWatchStatus") {
            animeApi.createWatchStatus(
                animeId = animeId,
                request = WatchStatusRequest(status = status.name)
            )
        }
    }

    override suspend fun updateWatchStatus(animeId: Long, status: WatchStatus): Result<Unit> {
        return safeApiCallUnit(TAG, "updateWatchStatus") {
            animeApi.updateWatchStatus(
                animeId = animeId,
                request = WatchStatusRequest(status = status.name)
            )
        }
    }

    override suspend fun deleteWatchStatus(animeId: Long): Result<Unit> {
        return safeApiCallUnit(TAG, "deleteWatchStatus") { animeApi.deleteWatchStatus(animeId) }
    }

    override suspend fun getAnimeSeries(animeId: Long, cursor: Cursor?): Result<GetInfoSeriesResponse> {
        return safeApiCall(TAG, "getAnimeSeries") {
            animeApi.getAnimeSeries(
                animeId = animeId,
                lastId = cursor?.lastId
            )
        }
    }

    override suspend fun getAnimeRecommends(animeId: Long, cursor: Cursor?): Result<GetInfoRecommendResponse> {
        return safeApiCall(TAG, "getAnimeRecommends") {
            animeApi.getAnimeRecommends(
                animeId = animeId,
                lastId = cursor?.lastId
            )
        }
    }

    override suspend fun loadWatchListAnimes(request: GetUserContentRequest): Result<GetUserContentResponse> {
        return safeApiCall(TAG, "loadWatchListAnimes") {
            animeApi.loadWatchListAnimes(
                status = "WATCHLIST",
                lastId = request.lastId
            )
        }
    }

    override suspend fun loadWatchingAnimes(request: GetUserContentRequest): Result<GetUserContentResponse> {
        return safeApiCall(TAG, "loadWatchingAnimes") {
            animeApi.loadWatchingAnimes(
                status = "WATCHING",
                lastId = request.lastId
            )
        }
    }

    override suspend fun loadFinishedAnimes(request: GetUserContentRequest): Result<GetUserContentResponse> {
        return safeApiCall(TAG, "loadFinishedAnimes") {
            animeApi.loadFinishedAnimes(
                status = "FINISHED",
                lastId = request.lastId
            )
        }
    }

    override suspend fun loadLikedAnimes(request: GetUserContentRequest): Result<GetUserContentResponse> {
        return safeApiCall(TAG, "loadLikedAnimes") {
            animeApi.loadLikedAnimes(
                lastId = request.lastId
            )
        }
    }

    override suspend fun getRealTimeRanking(request: GetAnimeRankingRequest): Result<GetAnimeRankingResponse> {
        return safeApiCall(TAG, "getRealTimeRanking") {
            animeApi.getRealTimeRanking(
                genre = if (request.genre?.id != -1) request.genre?.name else null,
                lastId = request.lastId,
                lastValue = request.lastValue,
                size = request.size,
            )
        }
    }

    override suspend fun getYearSeasonRanking(request: GetAnimeRankingRequest): Result<GetAnimeRankingResponse> {
        return safeApiCall(TAG, "getYearSeasonRanking") {
            animeApi.getYearSeasonRanking(
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
            )
        }
    }

    override suspend fun getAllTimeRanking(request: GetAnimeRankingRequest): Result<GetAnimeRankingResponse> {
        return safeApiCall(TAG, "getAllTimeRanking") {
            animeApi.getAllTimeRanking(
                genre = if (request.genre?.id != -1) request.genre?.name else null,
                lastId = request.lastId,
                lastRank = request.lastRank,
                size = request.size,
            )
        }
    }

    override suspend fun getAnimeExplore(request: GetAnimeExploreRequest): Result<GetAnimeExploreResponse> {
        return safeApiCall(TAG, "getAnimeExplore") {
            animeApi.getAnimeExplore(
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
            )
        }
    }

    override suspend fun getPopularAnimes(): Result<GetPopularAnimeResponse> {
        return safeApiCall(TAG, "getPopularAnimes") { animeApi.getPopularAnimes() }
    }

    override suspend fun getSearchResult(request: GetSearchResultRequest): Result<GetSearchResultResponse> {
        return safeApiCall(TAG, "getSearchResult") {
            animeApi.getSearchResult(
                query = request.query,
                lastId = request.lastId,
                size = request.size,
                page = request.page
            )
        }
    }

    override suspend fun submitAnimeLog(logUrl: String): Result<Unit> {
        return safeApiCallUnit(TAG, "submitAnimeLog") {
            animeApi.submitLogByUrl(
                url = logUrl
            )
        }
    }
}
