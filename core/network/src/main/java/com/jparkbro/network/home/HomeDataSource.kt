package com.jparkbro.network.home

import com.jparkbro.model.common.anime.TrendingAnimeDto
import com.jparkbro.model.common.anime.UpcomingReleasesAnimeDto
import com.jparkbro.model.common.review.HomeReviewDto
import com.jparkbro.model.common.review.Review
import com.jparkbro.model.dto.home.detail.ListDataResponse
import com.jparkbro.model.dto.home.main.NextQuarterAnimesResponse
import com.jparkbro.model.dto.home.main.RecommendedAnimesResponse
import com.jparkbro.model.home.HomeDetailRequest

interface HomeDataSource {
    suspend fun getTrendItems(): Result<List<TrendingAnimeDto>>
    suspend fun getRecommendItems(): Result<RecommendedAnimesResponse>
    suspend fun getRecentRecommendItems(animeId: Int): Result<RecommendedAnimesResponse>
    suspend fun getRecentReviews(): Result<List<HomeReviewDto>>
    suspend fun getNextQuarterAnimes(): Result<NextQuarterAnimesResponse>
    suspend fun getComingSoonItems(): Result<List<UpcomingReleasesAnimeDto>>

    suspend fun getDetailRecommends(request: HomeDetailRequest): Result<ListDataResponse>
    suspend fun getDetailRecentRecommends(request: HomeDetailRequest): Result<ListDataResponse>
    suspend fun getDetailRecentReviews(request: HomeDetailRequest): Result<ListDataResponse>
    suspend fun getDetailComingSoon(request: HomeDetailRequest): Result<ListDataResponse>
}