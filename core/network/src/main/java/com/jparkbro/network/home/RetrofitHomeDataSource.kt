package com.jparkbro.network.home

import com.jparkbro.model.common.anime.TrendingAnimeDto
import com.jparkbro.model.common.anime.UpcomingReleasesAnimeDto
import com.jparkbro.model.common.review.HomeReviewDto
import com.jparkbro.model.dto.home.detail.ListDataResponse
import com.jparkbro.model.dto.home.main.NextQuarterAnimesResponse
import com.jparkbro.model.dto.home.main.RecommendedAnimesResponse
import com.jparkbro.model.home.HomeDetailRequest
import com.jparkbro.network.util.safeApiCall
import javax.inject.Inject

class RetrofitHomeDataSource @Inject constructor(
    private val homeApi: HomeApi
) : HomeDataSource {
    companion object {
        private const val TAG = "RetrofitHomeDataSource"
    }

    override suspend fun getTrendItems(): Result<List<TrendingAnimeDto>> {
        return safeApiCall(TAG, "getTrendItems") { homeApi.getTrendItems() }
    }

    override suspend fun getRecommendItems(): Result<RecommendedAnimesResponse> {
        return safeApiCall(TAG, "getRecommendItems") { homeApi.getRecommendItems() }
    }

    override suspend fun getRecentRecommendItems(animeId: Long): Result<RecommendedAnimesResponse> {
        return safeApiCall(TAG, "getRecentRecommendItems") { homeApi.getRecentRecommendItems(animeId) }
    }

    override suspend fun getRecentReviews(): Result<List<HomeReviewDto>> {
        return safeApiCall(TAG, "getRecentReviews") { homeApi.getRecentReviews() }
    }

    override suspend fun getNextQuarterAnimes(): Result<NextQuarterAnimesResponse> {
        return safeApiCall(TAG, "getUpcomingSeasonItems") { homeApi.getNextQuarterAnimes() }
    }

    override suspend fun getComingSoonItems(): Result<List<UpcomingReleasesAnimeDto>> {
        return safeApiCall(TAG, "getComingSoonItems") { homeApi.getComingSoonItems() }
    }

    override suspend fun getDetailRecommends(request: HomeDetailRequest): Result<ListDataResponse> {
        return safeApiCall(TAG, "getDetailRecommends") {
            homeApi.getDetailRecommends(
                lastId = request.lastId,
                lastValue = request.lastValue,
                size = request.size
            )
        }
    }

    override suspend fun getDetailRecentRecommends(request: HomeDetailRequest): Result<ListDataResponse> {
        return safeApiCall(TAG, "getDetailRecentRecommends") {
            homeApi.getDetailRecentRecommends(
                animeId = request.animeId as Long,
                lastId = request.lastId,
                lastValue = request.lastValue,
                size = request.size
            )
        }
    }

    override suspend fun getDetailRecentReviews(request: HomeDetailRequest): Result<ListDataResponse> {
        return safeApiCall(TAG, "getDetailRecentReviews") {
            homeApi.getDetailRecentReviews(
                lastId = request.lastId,
                size = request.size
            )
        }
    }

    override suspend fun getDetailComingSoon(request: HomeDetailRequest): Result<ListDataResponse> {
        return safeApiCall(TAG, "getDetailComingSoon") {
            homeApi.getDetailComingSoon(
                sort = request.sort,
                lastId = request.lastId,
                lastValue = request.lastValue,
                size = request.size
            )
        }
    }
}