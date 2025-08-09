package com.jparkbro.network.home

import com.jparkbro.model.common.DefaultAnime
import com.jparkbro.model.home.ComingSoonItem
import com.jparkbro.model.home.HomeDetailRequest
import com.jparkbro.model.home.HomeDetailResponse
import com.jparkbro.model.home.HomeRecommendResponse
import com.jparkbro.model.home.HomeReviewItem
import com.jparkbro.model.home.UpcomingSeasonItems

interface HomeDataSource {
    suspend fun getTrendItems(): Result<List<DefaultAnime>>
    suspend fun getRecommendItems(): Result<HomeRecommendResponse>
    suspend fun getRecentRecommendItems(animeId: Int): Result<HomeRecommendResponse>
    suspend fun getRecentReviews(): Result<List<HomeReviewItem>>
    suspend fun getUpcomingSeasonItems(): Result<UpcomingSeasonItems>
    suspend fun getComingSoonItems(): Result<List<ComingSoonItem>>

    suspend fun getDetailRecommends(request: HomeDetailRequest): Result<HomeDetailResponse>
    suspend fun getDetailRecentRecommends(request: HomeDetailRequest): Result<HomeDetailResponse>
    suspend fun getDetailRecentReviews(request: HomeDetailRequest): Result<HomeDetailResponse>
    suspend fun getDetailComingSoon(request: HomeDetailRequest): Result<HomeDetailResponse>
}