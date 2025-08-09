package com.jparkbro.network.home

import com.jparkbro.model.common.DefaultAnime
import com.jparkbro.model.home.ComingSoonItem
import com.jparkbro.model.home.HomeDetailRequest
import com.jparkbro.model.home.HomeDetailResponse
import com.jparkbro.model.home.HomeRecommendResponse
import com.jparkbro.model.home.HomeReviewItem
import com.jparkbro.model.home.UpcomingSeasonItems
import com.jparkbro.network.util.toResult
import javax.inject.Inject

class RetrofitHomeDataSource @Inject constructor(
    private val homeApi: HomeApi
) : HomeDataSource {
    companion object {
        private const val TAG = "RetrofitHomeDataSource"
    }

    override suspend fun getTrendItems(): Result<List<DefaultAnime>> {
        return homeApi.getTrendItems().toResult(TAG, "getTrendItems")
    }

    override suspend fun getRecommendItems(): Result<HomeRecommendResponse> {
        return homeApi.getRecommendItems().toResult(TAG, "getRecommendItems")
    }

    override suspend fun getRecentRecommendItems(animeId: Int): Result<HomeRecommendResponse> {
        return homeApi.getRecentRecommendItems(animeId).toResult(TAG, "getRecentRecommendItems")
    }

    override suspend fun getRecentReviews(): Result<List<HomeReviewItem>> {
        return homeApi.getRecentReviews().toResult(TAG, "getRecentReviews")
    }

    override suspend fun getUpcomingSeasonItems(): Result<UpcomingSeasonItems> {
        return homeApi.getUpcomingSeasonItems().toResult(TAG, "getUpcomingSeasonItems")
    }

    override suspend fun getComingSoonItems(): Result<List<ComingSoonItem>> {
        return homeApi.getComingSoonItems().toResult(TAG, "getComingSoonItems")
    }

    override suspend fun getDetailRecommends(request: HomeDetailRequest): Result<HomeDetailResponse> {
        return homeApi.getDetailRecommends(
            lastId = request.lastId,
            lastValue = request.lastValue,
            size = request.size
        ).toResult(TAG, "getDetailRecommends")
    }

    override suspend fun getDetailRecentRecommends(request: HomeDetailRequest): Result<HomeDetailResponse> {
        return homeApi.getDetailRecentRecommends(
            animeId = request.animeId as Int,
            lastId = request.lastId,
            lastValue = request.lastValue,
            size = request.size
        ).toResult(TAG, "getDetailRecentRecommends")
    }

    override suspend fun getDetailRecentReviews(request: HomeDetailRequest): Result<HomeDetailResponse> {
        return homeApi.getDetailRecentReviews(
            lastId = request.lastId,
            size = request.size
        ).toResult(TAG, "getDetailRecentReviews")
    }

    override suspend fun getDetailComingSoon(request: HomeDetailRequest): Result<HomeDetailResponse> {
        return homeApi.getDetailComingSoon(
            sort = request.sort,
            lastId = request.lastId,
            lastValue = request.lastValue,
            includeAdult = request.includeAdult,
            size = request.size
        ).toResult(TAG, "getDetailComingSoon")
    }
}