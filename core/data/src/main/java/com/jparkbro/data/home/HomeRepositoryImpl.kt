package com.jparkbro.data.home

import com.jparkbro.model.common.anime.Anime
import com.jparkbro.model.dto.home.detail.ListDataResult
import com.jparkbro.model.dto.home.detail.toResult
import com.jparkbro.model.dto.home.main.NextQuarterAnimesResult
import com.jparkbro.model.dto.home.main.RecommendedAnimesResult
import com.jparkbro.model.common.anime.toAnime
import com.jparkbro.model.common.review.Review
import com.jparkbro.model.common.review.toReview
import com.jparkbro.model.dto.home.main.toResult
import com.jparkbro.model.enum.HomeDetailType
import com.jparkbro.model.home.HomeDetailRequest
import com.jparkbro.network.home.HomeDataSource
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val homeDataSource: HomeDataSource
) : HomeRepository {
    override suspend fun getTrendItems(): Result<List<Anime>> {
        return homeDataSource.getTrendItems().map { animeDtos ->
            animeDtos.map { it.toAnime() }
        }
    }

    override suspend fun getRecommendItems(): Result<RecommendedAnimesResult> {
        return homeDataSource.getRecommendItems().map { it.toResult() }
    }

    override suspend fun getRecentRecommendItems(animeId: Long): Result<RecommendedAnimesResult> {
        return homeDataSource.getRecentRecommendItems(animeId).map { it.toResult() }
    }

    override suspend fun getRecentReviews(): Result<List<Review>> {
        return homeDataSource.getRecentReviews().map { reviewDtos ->
            reviewDtos.map { it.toReview() }
        }
    }

    override suspend fun getNextQuarterAnimes(): Result<NextQuarterAnimesResult> {
        return homeDataSource.getNextQuarterAnimes().map { it.toResult() }
    }

    override suspend fun getComingSoonItems(): Result<List<Anime>> {
        return homeDataSource.getComingSoonItems().map { animeDtos ->
            animeDtos.map { it.toAnime() }
        }
    }

    override suspend fun getDetailData(type: HomeDetailType, request: HomeDetailRequest): Result<ListDataResult> {
        return when (type) {
            HomeDetailType.RECOMMENDS -> homeDataSource.getDetailRecommends(request).map { it.toResult() }
            HomeDetailType.RECENT_REVIEWS -> homeDataSource.getDetailRecentReviews(request).map { it.toResult() }
            HomeDetailType.SIMILAR_TO_WATCHED -> homeDataSource.getDetailRecentRecommends(request).map { it.toResult() }
            HomeDetailType.UPCOMING_RELEASE -> homeDataSource.getDetailComingSoon(request).map { it.toResult() }
        }
    }
}