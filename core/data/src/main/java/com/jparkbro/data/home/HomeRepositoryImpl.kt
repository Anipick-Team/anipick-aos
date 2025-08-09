package com.jparkbro.data.home

import com.jparkbro.model.common.DefaultAnime
import com.jparkbro.model.home.ComingSoonItem
import com.jparkbro.model.home.ContentType
import com.jparkbro.model.home.HomeDetailRequest
import com.jparkbro.model.home.HomeDetailResponse
import com.jparkbro.model.home.HomeRecommendResponse
import com.jparkbro.model.home.HomeReviewItem
import com.jparkbro.model.home.UpcomingSeasonItems
import com.jparkbro.network.home.HomeDataSource
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val homeDataSource: HomeDataSource
) : HomeRepository {
    override suspend fun getTrendItems(): Result<List<DefaultAnime>> {
        return homeDataSource.getTrendItems()
    }

    override suspend fun getRecommendItems(): Result<HomeRecommendResponse> {
        return homeDataSource.getRecommendItems()
    }

    override suspend fun getRecentRecommendItems(animeId: Int): Result<HomeRecommendResponse> {
        return homeDataSource.getRecentRecommendItems(animeId)
    }

    override suspend fun getRecentReviews(): Result<List<HomeReviewItem>> {
        return homeDataSource.getRecentReviews()
    }

    override suspend fun getUpcomingSeasonItems(): Result<UpcomingSeasonItems> {
        return homeDataSource.getUpcomingSeasonItems()
    }

    override suspend fun getComingSoonItems(): Result<List<ComingSoonItem>> {
        return homeDataSource.getComingSoonItems()
    }

    override suspend fun getDetailData(type: ContentType, request: HomeDetailRequest): Result<HomeDetailResponse> {
        return when (type) {
            ContentType.RECOMMEND -> homeDataSource.getDetailRecommends(request)
            ContentType.RECENT_RECOMMEND -> homeDataSource.getDetailRecentRecommends(request)
            ContentType.RECENT_REVIEW -> homeDataSource.getDetailRecentReviews(request)
            ContentType.COMING_SOON -> homeDataSource.getDetailComingSoon(request)
        }
    }
}