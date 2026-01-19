package com.jparkbro.network.home

import com.jparkbro.model.common.anime.TrendingAnimeDto
import com.jparkbro.model.common.anime.UpcomingReleasesAnimeDto
import com.jparkbro.model.common.review.HomeReviewDto
import com.jparkbro.model.dto.home.detail.ListDataResponse
import com.jparkbro.model.dto.home.main.NextQuarterAnimesResponse
import com.jparkbro.model.dto.home.main.RecommendedAnimesResponse
import com.jparkbro.model.home.HomeDetailRequest
import com.jparkbro.network.util.toResult
import javax.inject.Inject

class RetrofitHomeDataSource @Inject constructor(
    private val homeApi: HomeApi
) : HomeDataSource {
    companion object {
        private const val TAG = "RetrofitHomeDataSource"
    }

    override suspend fun getTrendItems(): Result<List<TrendingAnimeDto>> {
        return homeApi.getTrendItems().toResult(TAG, "getTrendItems")
    }

    override suspend fun getRecommendItems(): Result<RecommendedAnimesResponse> {
        return homeApi.getRecommendItems().toResult(TAG, "getRecommendItems")
    }

    override suspend fun getRecentRecommendItems(animeId: Long): Result<RecommendedAnimesResponse> {
        return homeApi.getRecentRecommendItems(animeId).toResult(TAG, "getRecentRecommendItems")
    }

    override suspend fun getRecentReviews(): Result<List<HomeReviewDto>> {
        return homeApi.getRecentReviews().toResult(TAG, "getRecentReviews")
    }

    override suspend fun getNextQuarterAnimes(): Result<NextQuarterAnimesResponse> {
        return homeApi.getNextQuarterAnimes().toResult(TAG, "getUpcomingSeasonItems")
    }

    override suspend fun getComingSoonItems(): Result<List<UpcomingReleasesAnimeDto>> {
        return homeApi.getComingSoonItems().toResult(TAG, "getComingSoonItems")
    }

    override suspend fun getDetailRecommends(request: HomeDetailRequest): Result<ListDataResponse> {
        return homeApi.getDetailRecommends(
            lastId = request.lastId,
            lastValue = request.lastValue,
            size = request.size
        ).toResult(TAG, "getDetailRecommends")
    }

    override suspend fun getDetailRecentRecommends(request: HomeDetailRequest): Result<ListDataResponse> {
        return homeApi.getDetailRecentRecommends(
            animeId = request.animeId as Long,
            lastId = request.lastId,
            lastValue = request.lastValue,
            size = request.size
        ).toResult(TAG, "getDetailRecentRecommends")
    }

    override suspend fun getDetailRecentReviews(request: HomeDetailRequest): Result<ListDataResponse> {
        return homeApi.getDetailRecentReviews(
            lastId = request.lastId,
            size = request.size
        ).toResult(TAG, "getDetailRecentReviews")
    }

    override suspend fun getDetailComingSoon(request: HomeDetailRequest): Result<ListDataResponse> {
        return homeApi.getDetailComingSoon(
            sort = request.sort,
            lastId = request.lastId,
            lastValue = request.lastValue,
            size = request.size
        ).toResult(TAG, "getDetailComingSoon")
    }
}