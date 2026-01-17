package com.jparkbro.network.anime

import com.jparkbro.model.common.Cursor
import com.jparkbro.model.common.anime.InfoSeriesAnimeDto
import com.jparkbro.model.common.anime.SimpleAnimeDto
import com.jparkbro.model.detail.AnimeSeriesResponse
import com.jparkbro.model.detail.WatchStatusRequest
import com.jparkbro.model.dto.home.main.RecommendedAnimesResponse
import com.jparkbro.model.dto.info.AnimeInfoResponse
import com.jparkbro.model.dto.info.GetInfoRecommendResponse
import com.jparkbro.model.dto.info.GetInfoSeriesResponse
import com.jparkbro.model.dto.mypage.usercontent.GetUserContentRequest
import com.jparkbro.model.dto.mypage.usercontent.GetUserContentResponse
import com.jparkbro.model.enum.WatchStatus
import com.jparkbro.network.detail.RetrofitDetailDataSource
import com.jparkbro.network.home.RetrofitHomeDataSource
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
}